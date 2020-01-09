package follow.twentyfourking.base.retrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static RetrofitFactory INSTANCE;
    private Retrofit mRetrofit;

    private RetrofitFactory() {
        init();
    }

    public static RetrofitFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitFactory();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 构建具体的API 实例
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    /**
     * 初始化retrofit实例
     */
    private void init() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://www.wanandroid.com/")//基础url
                    .addConverterFactory(GsonConverterFactory.create())//数据解析器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
    }

    /**
     * 返回Client
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                        return responseInterceptor(chain);
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return client;
    }

    /**
     * 拦截主体返回
     *
     * @param chain
     * @return
     * @throws IOException
     */
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

    /****添加拦截器***/
    //设置Log相关的拦截器
    private static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
