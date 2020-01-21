package follow.twentyfourking.learning_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import follow.twentyfourking.learning_retrofit.data.HotCodesBean;
import follow.twentyfourking.learning_retrofit.retrofit.BaseView;
import follow.twentyfourking.learning_retrofit.retrofit.DetaultObserver;
import follow.twentyfourking.learning_retrofit.retrofit.GithubRequestImpl;
import follow.twentyfourking.learning_retrofit.retrofit.RetrofitFactory;

public class MainActivity extends AppCompatActivity implements BaseView {

    private GithubRequestImpl mGithubRequestImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGithubRequestImpl = new GithubRequestImpl();
        RetrofitFactory.getRetrofit().init();
    }

    public void jump1(View view) {
        RetrofitFactory.getRetrofit().getDataSynchronize();
    }

    public void jump2(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RetrofitFactory.getRetrofit().getDataAsynchronize();
            }
        }).start();
    }

    public void jump3(View view) {
        mGithubRequestImpl.executeRequest(
                new DetaultObserver<List<HotCodesBean>>(this) {
                    @Override
                    public void onNext(List<HotCodesBean> o) {
                        Log.d("TAG", "请求成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "请求失败 e = " + e.getMessage());
                    }
                }
                , mGithubRequestImpl.getHotCodes());
    }

    @Override
    public void showData() {
        Log.d("TAG", " showData = chegngong");
    }
}
