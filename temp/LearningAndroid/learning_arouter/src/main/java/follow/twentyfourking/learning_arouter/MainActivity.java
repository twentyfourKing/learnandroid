package follow.twentyfourking.learning_arouter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.HashMap;

import follow.twentyfourking.learning_arouter.testinject.TestObj;
import follow.twentyfourking.learning_arouter.testinject.TestParcelable;
import follow.twentyfourking.learning_arouter.testinject.TestSerializable;
import follow.twentyfourking.learning_arouter.testservice.ByWhoService;
import follow.twentyfourking.learning_arouter.testservice.SingleService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Activity activity;

    public static Activity getThis() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        activity = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            default:
                return;
            case 666:
                break;
        }
        String str1 = "activityResult";
        String str2 = String.valueOf(resultCode);
        Log.e(str1, str2);
    }

    @Override
    public void onClick(View view) {
        HashMap hashMap;
        ArrayList arrayList;
        TestObj testObj;
        TestParcelable testParcelable;
        TestSerializable testSerializable;
        ByWhoService byService;
        SingleService singleService;
        char c = '?';
        int id = view.getId();
        switch (id) {
            default:
                return;
            case R.id.openLog://打开日志并打印堆栈
                ARouter.openLog();
                break;
            case R.id.openDebug://开启调试模式(InstantRun需要开启)
                ARouter.openDebug();
                break;
            case R.id.init://初始化
                ARouter.openDebug();
                ARouter.init(getApplication());
                break;
            case R.id.destroy://关闭ARouter
                ARouter.getInstance()
                        .destroy();
                break;
            case R.id.normalNavigation://简单的应用内跳转
                ARouter.getInstance()
                        .build("/test/activity2")
                        .navigation();
                break;
            case R.id.kotlinNavigation://跳转到Kotlin页面
                ARouter.getInstance().build("/kotlin/test")
                        .withString("name", "老王")
                        .withInt("age", 23)
                        .navigation();
                break;
            case R.id.normalNavigation2://跳转ForResult
                ARouter.getInstance()
                        .build("/test/activity2")
                        .navigation(this, 666);
                break;
            case R.id.getFragment://获取Fragment实例
                Fragment fragment = (Fragment) ARouter.getInstance().build("/test/fragment").navigation();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder = stringBuilder.append("找到Fragment:");
                String fragmentStr = fragment.toString();
                Toast.makeText(this, stringBuilder.append(fragmentStr).toString(), Toast.LENGTH_LONG).show();
                break;
            case R.id.normalNavigationWithParams://携带参数的应用内跳转
                ARouter.getInstance()
                        .build(Uri.parse("arouter://m.aliyun.com/test/activity2"))
                        .withString("key1", "value1")
                        .navigation();
                break;
            case R.id.oldVersionAnim://旧版本转场动画
                ARouter.getInstance()
                        .build("/test/activity2")
                        .withTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                        .navigation(this);
                break;
            case R.id.newVersionAnim://新版本转场动画
                int version = Build.VERSION.SDK_INT;
                int width;
                int height;
                if (version >= 16) {
                    width = view.getWidth() / 2;
                    height = view.getHeight() / 2;
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.
                            makeScaleUpAnimation(view, width, height, 0, 0);
                    ARouter.getInstance()
                            .build("/test/activity2").withOptionsCompat(activityOptionsCompat)
                            .navigation();
                }
                Toast.makeText(this, "API < 16,不支持新版本动画", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navByUrl://通过URL跳转
                ARouter.getInstance().build("/test/webview")
                        .withString("url", "file:///android_asset/schame-test.html")
                        .navigation();
                break;
            case R.id.interceptor://拦截器测试
                ARouter.getInstance().build("/test/activity4")
                        .navigation(this, new NavCallback() {
                            public void onArrival(Postcard paramPostcard) {
                                Log.d("ARouter", "onArrival");
                            }

                            public void onInterrupt(Postcard paramPostcard) {
                                Log.d("ARouter", "被拦截了");
                            }

                            @Override
                            public void onFound(Postcard postcard) {
                                super.onFound(postcard);
                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                super.onLost(postcard);
                            }
                        });
                break;
            case R.id.autoInject://依赖注入(参照代码)
                testSerializable = new TestSerializable("Rose", 777);
                testParcelable = new TestParcelable("Titanic", 555);
                testObj = new TestObj("jack", c);
                arrayList = new ArrayList();
                arrayList.add(testObj);
                hashMap = new HashMap();
                hashMap.put("testMap", arrayList);
                ARouter.getInstance()
                        .build("/test/activity1")
                        .withString("name", "老王")
                        .withInt("age", 18)
                        .withBoolean("boy", true)
                        .withLong("high", 170l)
                        .withString("url", "https://a.b.c")
                        .withSerializable("ser", testSerializable)
                        .withParcelable("pac", testParcelable)
                        .withObject("obj", testObj)
                        .withObject("objList", arrayList)
                        .withObject("map", hashMap)
                        .navigation();
                break;
            case R.id.navByName://ByName调用服务(依赖查找)
                byService = (ByWhoService) ARouter.getInstance().navigation(ByWhoService.class);
                byService.byWho("ByName");
                break;
            case R.id.navByType://ByType调用服务(依赖查找)
                byService = (ByWhoService) ARouter.getInstance().build("/service/bywho").navigation();
                byService.byWho("ByType");
                break;
            case R.id.callSingle://调用单类
                singleService = (SingleService) ARouter.getInstance().navigation(SingleService.class);
                singleService.sayHello("Single");
                break;
            case R.id.navToMoudle1://跳转到模块1
                ARouter.getInstance()
                        .build("/module/1")
                        .navigation();
                break;
            case R.id.navToMoudle2://跳转到模块2
                ARouter.getInstance()
                        .build("/module/2", "m2")
                        .navigation();
            case R.id.failNav://跳转失败，单独降级
                ARouter.getInstance().build("/xxx/xxx")
                        .navigation(this, new NavCallback() {
                            public void onArrival(Postcard paramPostcard) {
                                Log.d("ARouter", "跳转完了");
                            }

                            public void onFound(Postcard paramPostcard) {
                                Log.d("ARouter", "找到了");
                            }

                            public void onInterrupt(Postcard paramPostcard) {
                                Log.d("ARouter", "被拦截了");
                            }

                            public void onLost(Postcard paramPostcard) {
                                Log.d("ARouter", "找不到了");
                            }
                        });
                break;

            case R.id.failNav2://跳转失败，全局降级
                ARouter.getInstance()
                        .build("/xxx/xxx")
                        .navigation();
                break;
            case R.id.failNav3://服务调用失败
                ARouter.getInstance().navigation(MainActivity.class);
                break;
        }
    }
}
