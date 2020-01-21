package follow.twentyfourking.learning_retrofit.retrofit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class BaseResponseFunc<T> implements Function<BaseResponse<T>, Observable<T>> {
    @Override
    public Observable<T> apply(BaseResponse<T> tBaseResponse) throws Exception {
        if ("Sucess".equals(tBaseResponse.getStatus())) {
            return Observable.error(new BaseResponseException(
                    tBaseResponse.getErrorCode(),
                    tBaseResponse.getErrorMsg()));
        }
        return Observable.just(tBaseResponse.getData());
    }
}
