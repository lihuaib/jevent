package jevent.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过该类可以将不同线程的方法抛到安卓主线程
 */

public class JEvent {

    private static volatile JEvent instance;

    public static JEvent getInstance() {
        if(instance == null) {
            synchronized (JEvent.class) {
                if(instance == null) {
                    instance = new JEvent();
                }
            }
        }
        return instance;
    }

    /**
     * String: 指的是 JMethod 的 getKey
     * JMethod: 被订阅的类和方法的集合
     */
    private static ConcurrentHashMap<String, JMethod> keyToMethod = new ConcurrentHashMap<>();

    /**
     * Object: 订阅的类
     * List JMethod: 类中的被订阅的方法
     */
    private static HashMap<Object, List<JMethod>> objToMethodList = new HashMap<>();

    /**
     * Class: 抛出的事件类型
     * List JMethod: 订阅的方法
     */
    public static HashMap<Class, List<JMethod>> eventToMethodList = new HashMap<>();

    public void regist(Object subClass) {
        try {
            List<JMethod> jMethodList = JUtil.findMethod(subClass);

            for (JMethod jMethod : jMethodList) {
                JMethod item = keyToMethod.putIfAbsent(jMethod.getKey(), jMethod);
                if(item == null) {
                    Class eventTypeClass = jMethod.getMethod().getParameterTypes()[0];
                    if (!eventToMethodList.containsKey(eventTypeClass)) {
                        eventToMethodList.put(eventTypeClass, new ArrayList<JMethod>());
                    }
                    eventToMethodList.get(eventTypeClass).add(jMethod);
                }
            }

            objToMethodList.put(subClass, jMethodList);
        } catch (Exception e) {
            Log.e("jlee", e.toString());
        }
    }

    public void unregist(Object subClass) {
        if(!objToMethodList.containsKey(subClass)) return;

        List<JMethod> jMethodList = objToMethodList.get(subClass);
        for(JMethod jMethod : jMethodList) {
            if(keyToMethod.containsKey(jMethod.getKey())) {
                keyToMethod.remove(jMethod.getKey());
            }

            for(Map.Entry<Class, List<JMethod>> item : eventToMethodList.entrySet()) {
                List<JMethod> jlist = item.getValue();
                if(jlist.contains(jMethod)) jlist.remove(jMethod);
            }
        }
    }

    public void post(Object event, JThreadMode jThreadMode) {
        JEventPool.getInstance().excute(event, jThreadMode);
    }

    public void post(Object event) {
        JEventPool.getInstance().excute(event);
    }
}
