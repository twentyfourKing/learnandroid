package follow.twentyfourking.base;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 通过Rxjava封装基础请求
 */
public abstract class BaseRequest {
    public void executeRequest(Observer observer, Observable observable) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}

