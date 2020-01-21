package follow.twentyfourking.learning_retrofit.retrofit;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    Retrofit retrofit;

    private RetrofitFactory() {

    }

    private static RetrofitFactory retrofitFactory;

    public static RetrofitFactory getRetrofit() {
        if (retrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofitFactory == null) {
                    retrofitFactory = new RetrofitFactory();
                }
            }
        }
        return retrofitFactory;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    public void init() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.wanandroid.com")//基础url
                    .addConverterFactory(GsonConverterFactory.create())//数据解析器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
//                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        return responseInterceptor(chain);
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return client;
    }

    /****添加拦截器***/
    //设置Log相关的拦截器
    private static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    //设置header相关的拦截器
//    private static Interceptor interceptor = chain -> {
//        Request request = chain.request()
//                .newBuilder()
//                .addHeader("Content-Type", "application/json")
//                .addHeader("charset", "UTF-8")
//                .addHeader("Authorization", AppPrefsUtils.getString(BaseConstant.KEY_SP_TOKEN))
//                .addHeader("Request-Version", requestVersion)
//                .removeHeader("User-Agent")
//                .addHeader("User-Agent", getUserAgent())
//                .build();
//        return chain.proceed(request);
//    };

    private static okhttp3.Response responseInterceptor(Interceptor.Chain chain) throws
            IOException {
        okhttp3.Response response = null;
        try {
            response = chain.proceed(chain.request());
        } catch (SocketTimeoutException exception) {
            exception.printStackTrace();
        }

        return response;
    }

    public void getDataSynchronize() {
        ApiService request = retrofit.create(ApiService.class);
        Call<List<User>> call = request.getUsers(1, 10);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                parseResponse(response);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("TAG", " error = " + t.getMessage());
            }
        });
    }

    public void getDataAsynchronize() {
        ApiService request = retrofit.create(ApiService.class);
        try {
            List<User> data = request.getUsers(1, 10).execute().body();
            if (data != null && !data.isEmpty()) {
                Log.d("TAG", "同步获取的数据如下：");
                for (User user : data) {
                    Log.d("TAG", " 用户名 = " + user.getLogin() + " 头像地址 = " + user.getAvatarUrl());
                }
            }
        } catch (Exception e) {
            Log.d("TAG", " e = " + e.getMessage());
        }
    }

    public void parseResponse(Response<List<User>> response) {
        List<User> data = response.body();
        if (data != null && !data.isEmpty()) {
            Log.d("TAG", "异步获取的数据如下：");
            for (User user : data) {
                Log.d("TAG", " 用户名 = " + user.getLogin() + " 头像地址 = " + user.getAvatarUrl());
            }
        }
    }
}
