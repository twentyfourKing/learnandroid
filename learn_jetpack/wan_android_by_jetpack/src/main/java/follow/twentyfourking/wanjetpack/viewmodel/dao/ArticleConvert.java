package follow.twentyfourking.wanjetpack.viewmodel.dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.ArticleTagBean;

public class ArticleConvert {

    @TypeConverter
    public List<ArticleTagBean> stringToObject(String value) {
        TypeToken<List<ArticleTagBean>> listType = new TypeToken<List<ArticleTagBean>>() {
        };
        return new Gson().fromJson(value, listType.getType());
    }

    @TypeConverter
    public String objectToString(List<ArticleTagBean> data) {
        return new Gson().toJson(data);
    }
}
