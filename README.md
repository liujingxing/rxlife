# RxLife
为什么要重复造轮子

熟悉RxJava的同学应该都知道trello/RxLifecycle 项目，它在目前的3.0.0版本中通过Lifecycle感知Activity/Fragment 的生命周期变化，并通过BehaviorSubject类及compose、takeUntil操作符来实现管道的中断，这种实现原理有一点不足的是，它在管道断开后，始终会往下游发送一个onComplete事件，这对于在onComplete事件中有业务逻辑的同学来说，无疑是致命的。那为什么会这样呢？因为takeUntil操作符内部实现机制就是这样的，有兴趣的同学可以去阅读takeUntil操作符的源码，这里不展开。而RxLife就不会有这样问题，因为在原理上RxLife就与trello/RxLifecycle不同，并且RxLife还在lift操作都的基础上提供了一些额外的api，能有效的避免因RxJava内部类持有Activity/Fragment的引用，而造成的内存泄漏问题


博客地址：https://blog.csdn.net/liujingxing93/article/details/89042768

Gradle引用方法

    dependencies {
       implementation 'com.rxjava.rxlife:rxlife:1.0.4'
    }

在Activity/Fragment 的 onDestroy 自动断开

        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

Activity/Fragment 的 onStop 自动断开

        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.as(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


在Activity/Fragment 的 onDestroy 自动断开，并中断上下游的引用，在主线程回调

        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

                //等价于
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

Activity/Fragment 的 onStop 自动断开，并中断上下游的引用，在主线程回调

        Observable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });

                //等价于
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });


RxLife类里面as操作符，皆适用于Flowable、ParallelFlowable、Observable、Single、Maybe、Completable这6个被观察者对象
使用as操作符，无需担心下游有自己的事件


注意！！！！！！！
结合RxLife使用Observable的lift、compose操作符时，下游除了subscribe操作符外最好不要有其它的操作符，前面讲过，当调用Disposable.dispose()时，它会往上一层一层的调用上游的dispose()方法，如果下游有Disposable对象，是调用不到的，如果此时下游有自己的事件需要发送，那么就无法拦截了
--------------------- 

1.0.4 更新日志

  1、新增as操作符，规定下游只能使用subscribe操作符

  2、lift、compose 标记为过时，在未来的版本中将会删除，请使用as操作符替代






