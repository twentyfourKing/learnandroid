package follow.twentyfourking.wanjetpack.viewmodel.api;

import java.util.List;

import follow.twentyfourking.base.BaseResponse;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.TechnicalSystemBean;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.response.ArticleListResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HomeArtApi {
    //获取文章列表。通过分页加载，一次拿取一页的数据
    @GET("article/list/{page}/json")
    Observable<BaseResponse<ArticleListResponse>> getArticleList(@Path("page") int page);

    @GET("tree/json")
    Observable<BaseResponse<List<TechnicalSystemBean>>> getTechnicalSystemList();

    //https://www.wanandroid.com/article/list/0/json?cid=60
    @GET("article/list/{page}/json")
    Observable<BaseResponse<ArticleListResponse>> getChildSystemArticleList(@Path("page") int page, @Query("cid") int cid);
}
