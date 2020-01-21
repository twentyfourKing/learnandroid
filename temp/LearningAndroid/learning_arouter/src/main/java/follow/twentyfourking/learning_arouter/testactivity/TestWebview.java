package follow.twentyfourking.learning_arouter.testactivity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;

import follow.twentyfourking.learning_arouter.R;

@Route(path = "/test/webview")
public class TestWebview extends Activity {
    WebView webview;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_test_webview);
        WebView webView = (WebView) findViewById(R.id.hello_webview);
        this.webview = webView;
        webView = this.webview;
        String str = getIntent().getStringExtra("url");
        webView.loadUrl(str);
    }
}
