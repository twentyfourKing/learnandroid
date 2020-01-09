package follow.twentyfourking.wanjetpack.viewmodel.utils;

import follow.twentyfourking.base.IBaseCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DefaultObserver<T> implements Observer<T> {
    IBaseCallback mCallback;

    public DefaultObserver(IBaseCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T o) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
