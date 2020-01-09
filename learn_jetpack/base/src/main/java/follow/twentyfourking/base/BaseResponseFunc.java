package follow.twentyfourking.base;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 对于Observable返回数据泛型解析
 * @param <T>
 */
public class BaseResponseFunc<T> implements Function<BaseResponse<T>, Observable<T>> {
    @Override
    public Observable<T> apply(BaseResponse<T> tBaseResponse) throws Exception {
        try {
            Thread.sleep(20000);
            Log.d("TTT", "getArticleListByPage  当前线程 " + Thread.currentThread().getName());
        } catch (Exception e) {

        }
        return Observable.just(tBaseResponse.getData());
    }
}
