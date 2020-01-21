package follow.twentyfourking.learning_eventbus;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import follow.twentyfourking.learning_eventbus.event.ErrorEvent;

public class MainActivity extends AppCompatActivity {


    ImageView img, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        img2 = findViewById(R.id.img2);
//        EventBus.builder().
        String date =  "2019-08-06 16:23:25";
        long lon = getStringToDate(date,"yyyy-MM-dd HH:mm:ss");
        Log.d("TTT","");
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public void startProcess(View view) {
    }

    public void click(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                while (count < 6) {
//                    try {
//                        Thread.sleep(5000);
//                        ErrorEvent errorEvent = new ErrorEvent();
//                        ++count;
//                        errorEvent.setMessage(count + "");
//                        EventBus.getDefault().post(errorEvent);
//                    } catch (Exception e) {
//
//                    }
//                }

                ErrorEvent errorEvent = new ErrorEvent();
                errorEvent.setMessage("messageinfo");
                EventBus.getDefault().post(errorEvent);
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 2)
    public void handleEvent(ErrorEvent event) {
        // do something
        Log.d("TTT", " message = " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void handleEvent2(ErrorEvent event) {
        Log.d("TTT", "message = " + event.getMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
