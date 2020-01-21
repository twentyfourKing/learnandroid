package follow.twentyfourking.learning_retrofit.retrofit;

import java.util.List;


import follow.twentyfourking.learning_retrofit.data.HotCodesBean;
import io.reactivex.Observable;

public class GithubRequestImpl extends BaseRequest {

    public GithubRequestImpl() {

    }

    public Observable<List<HotCodesBean>> getHotCodes() {
        return RetrofitFactory.getRetrofit().create(ApiService.class).getHotCodes()
                .flatMap(new BaseResponseFunc<List<HotCodesBean>>());
    }

}
