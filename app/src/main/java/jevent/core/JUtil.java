package jevent.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 17/2/21.
 */

public class JUtil {

    private static final String METHOD_PREFIX = "jevt_";

    /**
     * 所有的方法必须以特定的前缀码开头
     *
     * @param subClass
     */
    public static List<JMethod> findMethod(Object subClass) throws InterruptedException {
        List<JMethod> jMethodList = new ArrayList<>();

        Method[] methods = subClass.getClass().getDeclaredMethods();
        if (methods == null) return jMethodList;

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().indexOf(METHOD_PREFIX) != 0) continue;
            if (!Modifier.isPublic(methods[i].getModifiers())) {
                throw new InterruptedException("对象的订阅方法只能为 public");
            }
            if(methods[i].getParameterTypes().length != 1) {
                throw new InterruptedException("对象的订阅方法的参数只能为一个");
            }

            JMethod jMethod = new JMethod(methods[i], subClass);
            jMethodList.add(jMethod);
        }

        return jMethodList;
    }
}
