
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