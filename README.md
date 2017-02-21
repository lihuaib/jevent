JEvent
========
JEvent 是一个发布／订阅 的简单事件框架， 现在推荐用 Eventbus 来代替.<br/>
<img src="EventBus-Publish-Subscribe.png" width="500" height="187"/>

JEvent...

 * 简化了组件之间的通讯，避免了生命周期的复杂调用
 * 让你的代码更简单
 * 非常小的实现，只有几个类
 * 实现了主线程和当前线程

JEvent 的使用
-------------------
1. 在名字叫 TestActivity 的类上去订阅事件:

    ```java
    public void jevt_Hello(String event) {/* Do something */};
    ```
    最好能在activity 的生命周期内去注册或者注销

   ```java
    @Override
    public void onStart() {
        super.onStart();
        JEvent.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        JEvent.getInstance().unregister(this);
    }
    ```

3. 发出事件:

   ```java
    JEvent.getInstance().post("hello");  // 当前线程

    JEvent.getInstance().post("hello", JThreadMode.MAIN); // 主线程
    ```