package lee.jevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import jevent.core.JEvent;
import jevent.core.JThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "jlee";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_test);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("jlee", "onResume");

        Log.d(TAG, "tid=" + Thread.currentThread().getId());

        // 正常工作
        JEvent.getInstance().regist(this);
        JEvent.getInstance().post("hello jevent");
        JEvent.getInstance().unregist(this);

        // 无法工作
        JEvent.getInstance().post("not work");

        JEvent.getInstance().regist(this);

        // 正常工作
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                JEvent.getInstance().post("thread work", JThreadMode.MAIN);
            }
        });
        thread1.start();

        // 不是同一个线程
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "tid=" + Thread.currentThread().getId());
                JEvent.getInstance().post("thread not the same thread");
            }
        }, "thread2");
        thread2.start();
    }

    public void jevt_Hello(String str) {
        textView.setText(str);
    }
}
