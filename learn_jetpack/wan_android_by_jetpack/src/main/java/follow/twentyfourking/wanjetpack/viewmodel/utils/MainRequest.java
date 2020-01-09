package follow.twentyfourking.wanjetpack.viewmodel.utils;

import java.util.List;

import follow.twentyfourking.base.BaseRequest;
import follow.twentyfourking.base.BaseResponseFunc;
import follow.twentyfourking.base.retrofit.RetrofitFactory;
import follow.twentyfourking.wanjetpack.viewmodel.api.HomeArtApi;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.TechnicalSystemBean;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.response.ArticleListResponse;
import io.reactivex.Observable;

public class MainRequest extends BaseRequest {

    public MainRequest() {

    }

    /**
     * 获取article 列表
     *
     * @param page
     * @return
     */
    public Observable<ArticleListResponse> getArticleListByPage(int page) {
        return RetrofitFactory.getInstance().create(HomeArtApi.class)
                .getArticleList(page)
                .flatMap(new BaseResponseFunc<ArticleListResponse>());
    }

    /**
     * 获取系统标签
     *
     * @return
     */
    public Observable<List<TechnicalSystemBean>> getTechnicalSystemList() {
        return RetrofitFactory.getInstance().create(HomeArtApi.class)
                .getTechnicalSystemList()
                .flatMap(new BaseResponseFunc<List<TechnicalSystemBean>>());
    }

    /**
     * 根据体系分类id获取文章列表
     * @param page
     * @param cId
     * @return
     */
    public Observable<ArticleListResponse> getArticleListById(int page, int cId) {
        return RetrofitFactory.getInstance().create(HomeArtApi.class)
                .getChildSystemArticleList(page, cId)
                .flatMap(new BaseResponseFunc<ArticleListResponse>());
    }
}
