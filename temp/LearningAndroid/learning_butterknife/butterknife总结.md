#ButterKnife.bind()

绑定操作
ButterKnife.bind(xx)和ButterKnife.bind(xx,xx)
>单参数的绑定：对象有view，Dialog，activity，它们绑定的都是DecorView（getWindow().getDecorView()）
>双参数的绑定：可以指定需要绑定的View而不是默认的DecorView

最终都会调到如下方法:

    public static Unbinder bind(@NonNull Object target, @NonNull View source) {
        Class<?> targetClass = target.getClass();
        if (debug) Log.d(TAG, "Looking up binding for " + targetClass.getName());
        Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(targetClass);
        if (constructor == null) {
          return Unbinder.EMPTY;
        }
        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
          return constructor.newInstance(target, source);
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
          throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
          Throwable cause = e.getCause();
          if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
          }
          if (cause instanceof Error) {
            throw (Error) cause;
          }
          throw new RuntimeException("Unable to create binding instance.", cause);
        }
      }