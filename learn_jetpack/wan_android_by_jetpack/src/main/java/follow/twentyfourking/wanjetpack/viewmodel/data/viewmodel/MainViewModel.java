package follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.TechnicalSystemBean;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<ArticlePageEntity>> articleListData = new MutableLiveData();
    private MutableLiveData<List<ArticlePageEntity>> articleListMoreData = new MutableLiveData();
    private MutableLiveData<Boolean> stateFresh = new MutableLiveData();
    private MutableLiveData<List<TechnicalSystemBean>> childListData = new MutableLiveData<>();

    public MutableLiveData<List<ArticlePageEntity>> getArticleListMoreData() {
        return articleListMoreData;
    }

    public MutableLiveData<List<ArticlePageEntity>> getArticleListData() {
        return articleListData;
    }

    public MutableLiveData<Boolean> getStateFresh() {
        return stateFresh;
    }

    public MutableLiveData<List<TechnicalSystemBean>> getChildListData() {
        return childListData;
    }
}
