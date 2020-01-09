package follow.twentyfourking.wanjetpack.viewmodel.data.repository;

import com.blankj.utilcode.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticleDatabase;
import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;
import follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel.ArticleListViewModel;
import follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel.MainViewModel;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.TechnicalSystemBean;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.response.ArticleListResponse;
import follow.twentyfourking.wanjetpack.viewmodel.utils.AppExecutors;
import follow.twentyfourking.wanjetpack.viewmodel.utils.MainService;

public class MainRepository<T> implements MainService.IMainServiceCallback {
    private T mViewModel;
    private MainService mMainService;
    private ArticleDatabase mArticleDatabase;
    private AppExecutors mAppExecutors;

    public MainRepository(AppExecutors appExecutors, T viewModel, ArticleDatabase database) {
        this.mAppExecutors = appExecutors;
        this.mViewModel = viewModel;
        mMainService = new MainService(this);
        mArticleDatabase = database;
    }

    /***********************回调处理--start***************************/
    @Override
    public void onLineArticleList(ArticleListResponse data) {
        if (mViewModel instanceof MainViewModel) {
            ((MainViewModel) mViewModel).getStateFresh().setValue(false);//去掉更新状态
        } else if (mViewModel instanceof ArticleListViewModel) {
            ((ArticleListViewModel) mViewModel).getRefreshState().setValue(false);//去掉更新状态
        }

        if (data != null) {
            if (mViewModel instanceof MainViewModel) {
                ((MainViewModel) mViewModel).getArticleListData().setValue(data.getDatas());
                saveArticleListInit(data.getDatas());
            } else if (mViewModel instanceof ArticleListViewModel) {
                ((ArticleListViewModel) mViewModel).getArticleListDataInit().setValue(data.getDatas());
            }

        }
    }

    @Override
    public void onLineArticleMainListMore(ArticleListResponse data) {
        if (data != null) {
            if (mViewModel instanceof MainViewModel) {
                ((MainViewModel) mViewModel).getArticleListMoreData().setValue(data.getDatas());
            } else if (mViewModel instanceof ArticleListViewModel) {
                ((ArticleListViewModel) mViewModel).getArticleListDataMore().setValue(data.getDatas());
            }

        }
    }

    @Override
    public void onLineTechnicalSystemBeanList(List<TechnicalSystemBean> data) {
        if (data != null) {
            if (mViewModel instanceof MainViewModel) {
                ((MainViewModel) mViewModel).getChildListData().setValue(data);
            }

        }
    }

    /***********************回调处理--end***************************/

    /*************************数据处理--start********************************/

    //data from online

    /**
     * onLineArticleList
     */
    public void getArticleListInit(int pageNumber) {
        //网络检测
        boolean isConnected = NetworkUtils.isConnected();
        if (isConnected) {
            mMainService.getArticleList(pageNumber);
        } else {
            getArticleLocal();
        }
    }

    /**
     * onLineArticleMainListMore
     */
    public void getArticleListMore(int pageNumber, int type) {
        mMainService.getArticleList(pageNumber, 0);
    }

    /**
     * onLineTechnicalSystemBeanList
     */
    public void getTechSystemList() {
        mMainService.getTechnicalSystemList();
    }

    //data from local database

    public synchronized void getArticleLocal() {
        if (mArticleDatabase == null) {
            //说明不需要本地数据的加载
            return;
        }
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<ArticlePageEntity> articleLiveData = mArticleDatabase.getDao().getAllArticle();
                List<ArticlePageEntity> data = new ArrayList<>();
                if (articleLiveData != null) {
                    if (mViewModel instanceof MainViewModel) {
                        ((MainViewModel) mViewModel).getArticleListData().postValue(articleLiveData);
                        ((MainViewModel) mViewModel).getStateFresh().postValue(false);
                    }
                }
            }
        });
    }

    //保存数据
    public synchronized void saveArticleListInit(List<ArticlePageEntity> data) {
        if (mArticleDatabase == null) {
            //说明不需要本地数据的加载
            return;
        }
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (ArticlePageEntity entity : data) {
                    mArticleDatabase.getDao().insertArticleList(entity);
                }
            }
        });
    }

    /**
     * 获取体系下子体系的文章
     *
     * @param page
     * @param cId
     * @param type
     */
    public void getChildSystemArticleList(int page, int cId, int type) {
        mMainService.getArticleListById(page, cId, type);
    }


    /*************************数据处理--start********************************/
}
