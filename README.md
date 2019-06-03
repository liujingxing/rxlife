# RxLife

详细介绍：https://juejin.im/post/5cf3e1235188251c064815f1

Gradle引用方法

    dependencies {
       implementation 'com.rxjava.rxlife:rxlife:1.0.6'

       //if you use AndroidX
       implementation 'com.rxjava.rxlife:rxlife-x:1.0.6'
    }

在View的 onDetachedFromWindow方法 自动断开

        //java
        Observable.timer(5, TimeUnit.SECONDS)
                .asOnMain(RxLife.as(this))  //此时的this 为View对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

        //kotlin
        Observable.timer(5, TimeUnit.SECONDS)
                .lifeOnMain(this)  //此时的this 为View对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


在Activity/Fragment 的 onDestroy 自动断开

        //java
        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.as(this))     //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

        //kotlin
        Observable.timer(5, TimeUnit.SECONDS)
                .life(this)  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

Activity/Fragment 的 onStop 自动断开

        //java
        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.as(this, Event.ON_STOP))   //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

        //kotlin
        Observable.timer(5, TimeUnit.SECONDS)
                .life(this, Event.ON_STOP)  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


在Activity/Fragment 的 onDestroy 自动断开，并中断上下游的引用，在主线程回调

        //java
        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this))  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

        //kotlin
        Observable.timer(5, TimeUnit.SECONDS)
                .lifeOnMain(this)  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

                //等价于
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

Activity/Fragment 的 onStop 自动断开，并中断上下游的引用，在主线程回调

        //java
        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this, Event.ON_STOP))  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

        //kotlin
        Observable.timer(5, TimeUnit.SECONDS)
                .lifeOnMain(this, Event.ON_STOP)  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

                //等价于
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this, Event.ON_STOP))  //此时的this Activity/Fragment对象
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


RxLife类里面as操作符，皆适用于Flowable、ParallelFlowable、Observable、Single、Maybe、Completable这6个被观察者对象
使用as操作符，无需担心下游有自己的事件


注意！！！！！！！
结合RxLife使用Observable的lift、compose操作符时，下游除了subscribe操作符外最好不要有其它的操作符，前面讲过，当调用Disposable.dispose()时，它会往上一层一层的调用上游的dispose()方法，如果下游有Disposable对象，是调用不到的，如果此时下游有自己的事件需要发送，那么就无法拦截了
---------------------

更新日志

1.0.6
  - 代码优化
 
1.0.5
  - 引入作用域的概念，支持在View中自动中断RxJava管道

  - 对Kotlin简单适配，在kotlin中可使用life操作符绑定生命周期

1.0.4

  - 新增as操作符，规定下游只能使用subscribe操作符

  - lift、compose 标记为过时，在未来的版本中将会删除，请使用as操作符替代






