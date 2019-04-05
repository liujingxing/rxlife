# RxLife
创建管理RxJava生命周期的项目 RxLife

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












