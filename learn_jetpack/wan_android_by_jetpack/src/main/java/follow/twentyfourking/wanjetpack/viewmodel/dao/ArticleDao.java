package follow.twentyfourking.wanjetpack.viewmodel.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticleList(ArticlePageEntity entity);

    @Query("delete from article_list")
    void deleteArticleList();

    @Query("select * from article_list")
    List<ArticlePageEntity> getAllArticle();
}
