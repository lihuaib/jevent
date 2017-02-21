package jevent.core;

import java.lang.reflect.Method;

/**
 * 包装被订阅的方法和对象
 */

public class JMethod {

    private Method method;
    private Object clazz;

    public JMethod(Method method, Object clazz) {
        this.method = method;
        this.clazz = clazz;
    }

    public String getKey() {
        return clazz.hashCode() + "-" + clazz.getClass().getName() + "-" + method.getName();
    }

    public Method getMethod() {
        return this.method;
    }

    public Object getClazz() {
        return this.clazz;
    }
}
