package follow.twentyfourking.learning_retrofit.retrofit;

import java.util.List;

import follow.twentyfourking.learning_retrofit.data.HotCodesBean;
import follow.twentyfourking.learning_retrofit.data.ResponseData;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Call<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    @Headers({HEADER_API_VERSION})
    @GET("/hotkey/json")
    Observable<BaseResponse<List<HotCodesBean>>> getHotCodes();
}
