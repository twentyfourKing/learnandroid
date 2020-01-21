
Registry 
===

## Registry基本信息

> Registry类用于Glide初始化注册，编码、解码、加载等类型的操作类

具体的操作类型如下
```java
      private final ModelLoaderRegistry modelLoaderRegistry;
      private final EncoderRegistry encoderRegistry;
      private final ResourceDecoderRegistry decoderRegistry;
      private final ResourceEncoderRegistry resourceEncoderRegistry;
      private final DataRewinderRegistry dataRewinderRegistry;
      private final TranscoderRegistry transcoderRegistry;
      private final ImageHeaderParserRegistry imageHeaderParserRegistry;
```

与操作类对应的是 Registry.appen(xx)的重载方法

**ResourceDecoder**
```java
    public <Data, TResource> Registry append(
      @NonNull Class<Data> dataClass,
      @NonNull Class<TResource> resourceClass,
      @NonNull ResourceDecoder<Data, TResource> decoder) {
      
        append(BUCKET_APPEND_ALL, dataClass, resourceClass, decoder);
        return this;
  }
```
**Encoder**
```java
    public <Data> Registry append(@NonNull Class<Data> dataClass, @NonNull Encoder<Data> encoder) {
        encoderRegistry.append(dataClass, encoder);
        return this;
  }
```
**ModelLoaderFactory**
```java
    public <Model, Data> Registry append(
      @NonNull Class<Model> modelClass,
      @NonNull Class<Data> dataClass,
      @NonNull ModelLoaderFactory<Model, Data> factory) {
        modelLoaderRegistry.append(modelClass, dataClass, factory);
        return this;
  }
```
**ResourceEncoder**
```java
    public <TResource> Registry append(
      @NonNull Class<TResource> resourceClass, @NonNull ResourceEncoder<TResource> encoder) {
        resourceEncoderRegistry.append(resourceClass, encoder);
        return this;
  }
```
**ResourceDecoder**
```java
    public <Data, TResource> Registry append(
      @NonNull String bucket,
      @NonNull Class<Data> dataClass,
      @NonNull Class<TResource> resourceClass,
      @NonNull ResourceDecoder<Data, TResource> decoder) {
        decoderRegistry.append(bucket, decoder, dataClass, resourceClass);
        return this;
  }
```

## 分析ModelLoaderRegistry

实际的操作类是 MultiModelLoaderFactory

如：
```java
    // ModelLoaderRegistry.java
    public synchronized <Model, Data> void append(
      @NonNull Class<Model> modelClass,
      @NonNull Class<Data> dataClass,
      @NonNull ModelLoaderFactory<? extends Model, ? extends Data> factory) {
        multiModelLoaderFactory.append(modelClass, dataClass, factory);
        cache.clear();
  }
```

```java
    // MultiModelLoaderFactory.java
    
    synchronized <Model, Data> void append(
      @NonNull Class<Model> modelClass,
      @NonNull Class<Data> dataClass,
      @NonNull ModelLoaderFactory<? extends Model, ? extends Data> factory) {
        add(modelClass, dataClass, factory, /*append=*/ true);
  }
```

```java
    // MultiModelLoaderFactory.java
    
    private <Model, Data> void add(
      @NonNull Class<Model> modelClass,
      @NonNull Class<Data> dataClass,
      @NonNull ModelLoaderFactory<? extends Model, ? extends Data> factory,
      boolean append) {
        Entry<Model, Data> entry = new Entry<>(modelClass, dataClass, factory);
        entries.add(append ? entries.size() : 0, entry);
    }
```
Entry 是MultiModelLoaderFactory 内部类，用于保存具体的 modelClass, dataClass, factory 类型
```java
    private static class Entry<Model, Data> {
        private final Class<Model> modelClass;
        @Synthetic final Class<Data> dataClass;
        @Synthetic final ModelLoaderFactory<? extends Model, ? extends Data> factory;
    
        public Entry(
            @NonNull Class<Model> modelClass,
            @NonNull Class<Data> dataClass,
            @NonNull ModelLoaderFactory<? extends Model, ? extends Data> factory) {
          this.modelClass = modelClass;
          this.dataClass = dataClass;
          this.factory = factory;
        }
    
        public boolean handles(@NonNull Class<?> modelClass, @NonNull Class<?> dataClass) {
          return handles(modelClass) && this.dataClass.isAssignableFrom(dataClass);
        }
    
        public boolean handles(@NonNull Class<?> modelClass) {
          return this.modelClass.isAssignableFrom(modelClass);
        }
  }
```

比如：Glide中通过appen注册的

```java
        .append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
        .append(String.class, InputStream.class, new StringLoader.StreamFactory())
        .append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
```
最后在 MultiModelLoaderFactory 的 Entry  map中

生成的Entry对应关系是：

|model|dataClass | factory |
| ---- | ---- | ---- |
| String |InputStream| DataUrlLoader.StreamFactory |
| String | InputStream | StringLoader.StreamFactory |
| String | ParcelFileDescriptor |StringLoader.FileDescriptorFactory|


说说 Registry.getModelLoaders
```java
    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(@NonNull Model model) {
        List<ModelLoader<Model, ?>> result = modelLoaderRegistry.getModelLoaders(model);
        if (result.isEmpty()) {
          throw new NoModelLoaderAvailableException(model);
        }
        return result;
  }
```
实际操作还是
```java
    // ModelLoaderRegistry.java
    public <A> List<ModelLoader<A, ?>> getModelLoaders(@NonNull A model) {
        List<ModelLoader<A, ?>> modelLoaders = getModelLoadersForClass(getClass(model));
        int size = modelLoaders.size();
        boolean isEmpty = true;
        List<ModelLoader<A, ?>> filteredLoaders = Collections.emptyList();
        //noinspection ForLoopReplaceableByForEach to improve perf
        for (int i = 0; i < size; i++) {
          ModelLoader<A, ?> loader = modelLoaders.get(i);
          if (loader.handles(model)) {
            if (isEmpty) {
              filteredLoaders = new ArrayList<>(size - i);
              isEmpty = false;
            }
            filteredLoaders.add(loader);
          }
        }
        return filteredLoaders;
  }
```
找到满足model的 loader
```java
    private synchronized <A> List<ModelLoader<A, ?>> getModelLoadersForClass(
      @NonNull Class<A> modelClass) {
        List<ModelLoader<A, ?>> loaders = cache.get(modelClass);
        if (loaders == null) {
          loaders = Collections.unmodifiableList(multiModelLoaderFactory.build(modelClass));
          cache.put(modelClass, loaders);
        }
        return loaders;
  }
```
先从 cache中取 ModelLoaderCache cache = new ModelLoaderCache();

存在直接使用

如果不存在，通过
```java
        loaders = Collections.unmodifiableList(multiModelLoaderFactory.build(modelClass));
        cache.put(modelClass, loaders);
```
```java
    // MultiModelLoaderFactory.java
    synchronized <Model> List<ModelLoader<Model, ?>> build(@NonNull Class<Model> modelClass) {
        try {
          List<ModelLoader<Model, ?>> loaders = new ArrayList<>();
          for (Entry<?, ?> entry : entries) {
          
            if (alreadyUsedEntries.contains(entry)) {
              continue;
            }
            if (entry.handles(modelClass)) {
              alreadyUsedEntries.add(entry);
              loaders.add(this.<Model, Object>build(entry));
              alreadyUsedEntries.remove(entry);
            }
          }
          return loaders;
        } catch (Throwable t) {
          alreadyUsedEntries.clear();
          throw t;
        }
  }
```

遍历注册阶段存储的Entry，找到与该modelClass匹配的Entry ，通过Entry.handles

匹配后，直接build该Entry
```java
    private <Model, Data> ModelLoader<Model, Data> build(@NonNull Entry<?, ?> entry) {
        return (ModelLoader<Model, Data>) Preconditions.checkNotNull(entry.factory.build(this));
   }
```
用Entry中保存的factory进行操作

如 Entry保存的数据如下：
|model|dataClass | factory |
| ---- | ---- | ---- |
| String | InputStream | StringLoader.StreamFactory |

那么build的过程就是调用 StringLoader.StreamFactory 的build方法
如下：
```java
    public ModelLoader<String, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new StringLoader<>(multiFactory.build(Uri.class, InputStream.class));
    }
```
multiFactory 是 MultiModelLoaderFactory，又回到 MultiModelLoaderFactory.build()

```java
    public synchronized <Model, Data> ModelLoader<Model, Data> build(
      @NonNull Class<Model> modelClass, @NonNull Class<Data> dataClass) {
        try {
          List<ModelLoader<Model, Data>> loaders = new ArrayList<>();
          boolean ignoredAnyEntries = false;
          for (Entry<?, ?> entry : entries) {
           
            if (alreadyUsedEntries.contains(entry)) {
              ignoredAnyEntries = true;
              continue;
            }
            if (entry.handles(modelClass, dataClass)) {
              alreadyUsedEntries.add(entry);
              loaders.add(this.<Model, Data>build(entry));
              alreadyUsedEntries.remove(entry);
            }
          }
          if (loaders.size() > 1) {
            return factory.build(loaders, throwableListPool);
          } else if (loaders.size() == 1) {
            return loaders.get(0);
          } else {
            if (ignoredAnyEntries) {
              return emptyModelLoader();
            } else {
              throw new NoModelLoaderAvailableException(modelClass, dataClass);
            }
          }
        } catch (Throwable t) {
          alreadyUsedEntries.clear();
          throw t;
        }
  }
```
经过上述的逻辑可能会得到下面3种Loader

```text
    MultiModelLoader 

    EmptyModelLoader

    DataUrlLoader
```






