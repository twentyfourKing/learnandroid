package follow.twentyfourking.learning_arouter.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;

import java.lang.reflect.Type;

@Route(path = "/provider/json")
public class JsonServiceImpl implements SerializationService {
    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        Gson gson = new Gson();
        return gson.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        Gson gson = new Gson();
        return gson.fromJson(input, clazz);
    }

    @Override
    public void init(Context context) {

    }
}
