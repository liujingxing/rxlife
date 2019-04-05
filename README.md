# RxLife
为什么要重复造轮子

熟悉RxJava的同学应该都知道trello/RxLifecycle 项目，它在目前的3.0.0版本中通过Lifecycle感知Activity/Fragment 的生命周期变化，并通过BehaviorSubject类及compose、takeUntil操作符来实现管道的中断，这种实现原理有一点不足的是，它在管道断开后，始终会往下游发送一个onComplete事件，这对于在onComplete事件中有业务逻辑的同学来说，无疑是致命的。那为什么会这样呢？因为takeUntil操作符内部实现机制就是这样的，有兴趣的同学可以去阅读takeUntil操作符的源码，这里不展开。而RxLife就不会有这样问题，因为在原理上RxLife就与trello/RxLifecycle不同，并且RxLife还在lift操作都的基础上提供了一些额外的api，能有效的避免因RxJava内部类持有Activity/Fragment的引用，而造成的内存泄漏问题


博客地址：https://blog.csdn.net/liujingxing93/article/details/89042768

Gradle引用方法

    dependencies {
       implementation 'com.rxjava.rxlife:rxlife:1.0.2'
    }

在Activity/Fragment 的 onDestroy 自动断开

        Observable.timer(5, TimeUnit.SECONDS)
                .lift(RxLife.lift(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

Activity/Fragment 的 onStop 自动断开

        Observable.timer(5, TimeUnit.SECONDS)
                .lift(RxLife.lift(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


在Activity/Fragment 的 onDestroy 自动断开，并中断上下游的引用

        Observable.timer(5, TimeUnit.SECONDS)
                .compose(RxLife.compose(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

         //等同于
        Observable.timer(5, TimeUnit.SECONDS)
                .onTerminateDetach()
                .lift(RxLife.lift(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

Activity/Fragment 的 onStop 自动断开，并中断上下游的引用

        Observable.timer(5, TimeUnit.SECONDS)
                .compose(RxLife.compose(this,Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

         //等同于
        Observable.timer(5, TimeUnit.SECONDS)
                .onTerminateDetach()
                .lift(RxLife.lift(this,Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


Activity/Fragment 的 onStop 自动断开，并中断上下游的引用，并在主线程回调

        Observable.timer(5, TimeUnit.SECONDS)
                .compose(RxLife.composeOnMain(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

         //等同于
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(RxLife.lift(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });



RxLife类里面的6个静态方法，皆适用于Flowable、Observable、Single、Maybe、Completable这5个被观察者对象


注意！！！！！！！
结合RxLife使用Observable的lift、compose操作符时，下游除了subscribe操作符外最好不要有其它的操作符，前面讲过，当调用Disposable.dispose()时，它会往上一层一层的调用上游的dispose()方法，如果下游有Disposable对象，是调用不到的，如果此时下游有自己的事件需要发送，那么就无法拦截了
--------------------- 








