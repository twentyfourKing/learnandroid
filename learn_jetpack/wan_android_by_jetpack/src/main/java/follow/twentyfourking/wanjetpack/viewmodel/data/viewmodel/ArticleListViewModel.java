package follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;

public class ArticleListViewModel extends ViewModel {
    private MutableLiveData<List<ArticlePageEntity>> articleListDataInit = new MutableLiveData();
    private MutableLiveData<List<ArticlePageEntity>> articleListDataMore = new MutableLiveData();
    private MutableLiveData<Boolean> refreshState = new MutableLiveData<>();

    public MutableLiveData<Boolean> getRefreshState() {
        return refreshState;
    }

    public MutableLiveData<List<ArticlePageEntity>> getArticleListDataInit() {
        return articleListDataInit;
    }

    public MutableLiveData<List<ArticlePageEntity>> getArticleListDataMore() {
        return articleListDataMore;
    }
}
