
### 从网络的url检索数据

HttpUrlFetcher
> implements DataFetcher<InputStream> 

loadData()

```java

    public void loadData(
      @NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
      
          
    }
```

### 从本地类型的Uri检索数据

StreamLocalUriFetcher
> extends LocalUriFetcher<InputStream>

> public abstract class LocalUriFetcher<T> implements DataFetcher<T> 

### 从an asset path 检索数据
如 AssetManager

StreamAssetPathFetcher
> extends AssetPathFetcher<InputStream>

> public abstract class AssetPathFetcher<T> implements DataFetcher<T>


FileDescriptorAssetPathFetcher
> extends AssetPathFetcher<ParcelFileDescriptor>

### 从MediaStore检索

ThumbFetcher
> implements DataFetcher<InputStream>


DataUrlLoader.DataUriFetcher

FileLoader.FileFetcher

MediaStoreFileLoader.FilePathFetcher

ByteBufferFileLoader.ByteBufferFetcher

ByteArrayLoader.Fetcher

MultiModelLoader.MultiFetcher

UnitModelLoader.UnitFetcher

