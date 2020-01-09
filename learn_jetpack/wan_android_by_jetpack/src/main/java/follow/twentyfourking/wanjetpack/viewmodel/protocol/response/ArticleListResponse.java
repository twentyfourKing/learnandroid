package follow.twentyfourking.wanjetpack.viewmodel.protocol.response;

import java.util.List;

import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;

public class ArticleListResponse {
    //            "curPage":2,
//            "datas":[],
//            "offset":20,
//            "over":false,
//            "pageCount":368,
//            "size":20,
//            "total":7348
    private int curPage;
    private List<ArticlePageEntity> datas;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<ArticlePageEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<ArticlePageEntity> datas) {
        this.datas = datas;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
