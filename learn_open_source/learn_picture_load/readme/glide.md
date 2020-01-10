
[glide](https://github.com/bumptech/glide)

gradle引入

```text
    repositories {
      mavenCentral()
      google()
    }

    dependencies {
      implementation 'com.github.bumptech.glide:glide:4.10.0'
      annotationProcessor 'com.github.bumptech.glide:compiler:4.10.0'
    }
```

```java

public final class GlideBuilder {
    ...
    //线程
    private GlideExecutor sourceExecutor;
    private GlideExecutor diskCacheExecutor;
    private GlideExecutor animationExecutor;
    ...
}
```

分析下Glide的流程

Glide.with(target).

with方法的参数target可以是很多种类型，with()方法被重载了很多种主要是target类型不同

target：
```text
    View 
    android.app.Fragment
    Fragment
    FragmentActivity
    Activity
    Context
```
返回的都是 RequestManager

核心逻辑

```java

    public static RequestManager with(@NonNull Fragment fragment) {
        return getRetriever(fragment.getContext()).get(fragment);
    }
      
    
  public static RequestManager with(@NonNull View view) {
      return getRetriever(view.getContext()).get(view);
    }
```
```java
    private static RequestManagerRetriever getRetriever(@Nullable Context context) {
        // Context could be null for other reasons (ie the user passes in null), but in practice it will
        // only occur due to errors with the Fragment lifecycle.
        Preconditions.checkNotNull(
            context,
            "You cannot start a load on a not yet attached View or a Fragment where getActivity() "
                + "returns null (which usually occurs when getActivity() is called before the Fragment "
                + "is attached or after the Fragment is destroyed).");
        return Glide.get(context).getRequestManagerRetriever();
      }
```
Glide.get(context)  获取Glide实例

```java
    public static Glide get(@NonNull Context context) {
        if (glide == null) {
          GeneratedAppGlideModule annotationGeneratedModule =
              getAnnotationGeneratedGlideModules(context.getApplicationContext());
          synchronized (Glide.class) {
            if (glide == null) {
              checkAndInitializeGlide(context, annotationGeneratedModule);//说明1
            }
          }
        }
    
        return glide;
      }
```
> 说明1

如果glide为空，那么构建实例，并初始化Glide

GlideBuilder 核心类 

```java
    private static void initializeGlide(
          @NonNull Context context, @Nullable GeneratedAppGlideModule generatedAppGlideModule) {
        initializeGlide(context, new GlideBuilder(), generatedAppGlideModule);
      }
```

```java
    //初始化逻辑
    private static void initializeGlide(
          @NonNull Context context,
          @NonNull GlideBuilder builder,
          @Nullable GeneratedAppGlideModule annotationGeneratedModule) {
        Context applicationContext = context.getApplicationContext();
        List<com.bumptech.glide.module.GlideModule> manifestModules = Collections.emptyList();
        if (annotationGeneratedModule == null || annotationGeneratedModule.isManifestParsingEnabled()) {
          manifestModules = new ManifestParser(applicationContext).parse();
        }
    
        if (annotationGeneratedModule != null
            && !annotationGeneratedModule.getExcludedModuleClasses().isEmpty()) {
          Set<Class<?>> excludedModuleClasses = annotationGeneratedModule.getExcludedModuleClasses();
          Iterator<com.bumptech.glide.module.GlideModule> iterator = manifestModules.iterator();
          while (iterator.hasNext()) {
            com.bumptech.glide.module.GlideModule current = iterator.next();
            if (!excludedModuleClasses.contains(current.getClass())) {
              continue;
            }
            if (Log.isLoggable(TAG, Log.DEBUG)) {
              Log.d(TAG, "AppGlideModule excludes manifest GlideModule: " + current);
            }
            iterator.remove();
          }
        }
    
        if (Log.isLoggable(TAG, Log.DEBUG)) {
          for (com.bumptech.glide.module.GlideModule glideModule : manifestModules) {
            Log.d(TAG, "Discovered GlideModule from manifest: " + glideModule.getClass());
          }
        }
    
        RequestManagerRetriever.RequestManagerFactory factory =
            annotationGeneratedModule != null
                ? annotationGeneratedModule.getRequestManagerFactory()
                : null;
        builder.setRequestManagerFactory(factory);
        for (com.bumptech.glide.module.GlideModule module : manifestModules) {
          module.applyOptions(applicationContext, builder);
        }
        if (annotationGeneratedModule != null) {
          annotationGeneratedModule.applyOptions(applicationContext, builder);
        }
        Glide glide = builder.build(applicationContext);// 说明 构建Glide
        for (com.bumptech.glide.module.GlideModule module : manifestModules) {
          try {
            module.registerComponents(applicationContext, glide, glide.registry);
          } catch (AbstractMethodError e) {
            throw new IllegalStateException(
                "Attempting to register a Glide v3 module. If you see this, you or one of your"
                    + " dependencies may be including Glide v3 even though you're using Glide v4."
                    + " You'll need to find and remove (or update) the offending dependency."
                    + " The v3 module name is: "
                    + module.getClass().getName(),
                e);
          }
        }
        if (annotationGeneratedModule != null) {
          annotationGeneratedModule.registerComponents(applicationContext, glide, glide.registry);
        }
        applicationContext.registerComponentCallbacks(glide);
        Glide.glide = glide;
      }
```
> 说明 构建Glide

> Glide glide = builder.build(applicationContext);

```java
    Glide build(@NonNull Context context) {
        //代码相对较多，只简述流程
        1.创建 ExecutorService 子类 ，工作线程3个
        2.创建 MemorySizeCalculator 功能类
        3.构建缓存数据
        
        ...
    
    }
```

回到 getRetriever(view.getContext()).get(view)。根据target的类型不同，
get的实现方法不尽相同
```java
    public RequestManager get(@NonNull View view) {
        if (Util.isOnBackgroundThread()) {//如果不是主线程
          return get(view.getContext().getApplicationContext());
        }
    
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(
            view.getContext(), "Unable to obtain a request manager for a view without a Context");
        Activity activity = findActivity(view.getContext());
        // The view might be somewhere else, like a service.
        if (activity == null) {
          return get(view.getContext().getApplicationContext());
        }
    
        // Support Fragments.
        // Although the user might have non-support Fragments attached to FragmentActivity, searching
        // for non-support Fragments is so expensive pre O and that should be rare enough that we
        // prefer to just fall back to the Activity directly.
        if (activity instanceof FragmentActivity) {
          Fragment fragment = findSupportFragment(view, (FragmentActivity) activity);
          return fragment != null ? get(fragment) : get((FragmentActivity) activity);
        }
    
        // Standard Fragments.
        android.app.Fragment fragment = findFragment(view, activity);
        if (fragment == null) {
          return get(activity);
        }
        return get(fragment);
      }
```



