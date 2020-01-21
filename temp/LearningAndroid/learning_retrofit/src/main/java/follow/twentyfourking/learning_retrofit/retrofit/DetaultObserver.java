package follow.twentyfourking.learning_retrofit.retrofit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DetaultObserver<T> implements Observer<T> {
    BaseView mView;

    public DetaultObserver(BaseView mView) {
        this.mView = mView;
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
        mView.showData();
    }
}
