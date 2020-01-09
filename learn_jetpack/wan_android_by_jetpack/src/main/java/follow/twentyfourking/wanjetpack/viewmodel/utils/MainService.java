package follow.twentyfourking.wanjetpack.viewmodel.utils;

import android.util.Log;

import java.util.List;

import follow.twentyfourking.base.IBaseCallback;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.TechnicalSystemBean;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.response.ArticleListResponse;

public class MainService {
    private MainRequest mMainRequest;
    private MainService.IMainServiceCallback mCall;

    public MainService(MainService.IMainServiceCallback call) {
        mCall = call;
        mMainRequest = new MainRequest();
    }

    public void getArticleList(int page) {
        Log.d("", "");
        mMainRequest.executeRequest(new DefaultObserver<ArticleListResponse>(mCall) {
                                        @Override
                                        public void onNext(ArticleListResponse data) {
                                            mCall.onLineArticleList(data);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            super.onError(e);
                                            mCall.onLineArticleList(null);
                                        }
                                    }
                , mMainRequest.getArticleListByPage(page));
    }

    //多态，加载更多数据
    public void getArticleList(int page, int type) {
        mMainRequest.executeRequest(new DefaultObserver<ArticleListResponse>(mCall) {
                                        @Override
                                        public void onNext(ArticleListResponse data) {
                                            mCall.onLineArticleMainListMore(data);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
//                                            super.onError(e);
                                            mCall.onLineArticleMainListMore(null);
                                        }
                                    }
                , mMainRequest.getArticleListByPage(page));
    }

    //获取体系标签
    public void getTechnicalSystemList() {
        mMainRequest.executeRequest(new DefaultObserver<List<TechnicalSystemBean>>(mCall) {
                                        @Override
                                        public void onNext(List<TechnicalSystemBean> data) {
                                            mCall.onLineTechnicalSystemBeanList(data);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
//                                            super.onError(e);
                                            mCall.onLineTechnicalSystemBeanList(null);
                                        }
                                    }
                , mMainRequest.getTechnicalSystemList());
    }

    /**
     * @param page
     * @param cId
     * @param type 确定是首页数据，还是根据数据加载 0 首页 1 更多
     */
    public void getArticleListById(int page, int cId, final int type) {
        mMainRequest.executeRequest(new DefaultObserver<ArticleListResponse>(mCall) {
                                        @Override
                                        public void onNext(ArticleListResponse data) {
                                            if (type == 0) {
                                                mCall.onLineArticleList(data);
                                            } else {
                                                mCall.onLineArticleMainListMore(data);
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            super.onError(e);
                                            if (type == 0) {
                                                mCall.onLineArticleList(null);
                                            } else {
                                                mCall.onLineArticleMainListMore(null);
                                            }
                                        }
                                    }
                , mMainRequest.getArticleListById(page, cId));
    }

    public interface IMainServiceCallback extends IBaseCallback {
        void onLineArticleList(ArticleListResponse data);

        void onLineArticleMainListMore(ArticleListResponse data);

        void onLineTechnicalSystemBeanList(List<TechnicalSystemBean> data);
    }
}
