package jevent.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

/**
 * 将事件发送给主线程或者当前线程
 */

public class JEventPool {

    private static JEventPool instance = new JEventPool();

    public static JEventPool getInstance() {
        return instance;
    }

    Handler mainHandler = new Handler(Looper.getMainLooper());

    private JEventPool() {
    }

    public void excute(Object event) {
        excute(event, JThreadMode.NONE);
    }

    public void excute(final Object event, JThreadMode jThreadMode) {
        if (jThreadMode == JThreadMode.MAIN) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    process(event);
                }
            });
        } else if(jThreadMode == JThreadMode.NONE) {
            process(event);
        }
    }

    private void process(Object event) {
        try {
            if(!JEvent.eventToMethodList.containsKey(event.getClass())) {
                Log.e("jlee", "no submethod to find");
                return;
            }

            List<JMethod> jMethodList = JEvent.eventToMethodList.get(event.getClass());
            for(JMethod jMethod : jMethodList) {
                jMethod.getMethod().invoke(jMethod.getClazz(), event);
            }
        } catch (Exception ex) {
            Log.e("jlee", ex.toString());
        }
    }
}
