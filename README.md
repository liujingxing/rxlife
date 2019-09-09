[ ![Download](https://api.bintray.com/packages/32774707/maven/rxlife/images/download.svg) ](https://bintray.com/32774707/maven/rxlife/_latestVersion)

# RxLife
[RxLife](https://github.com/liujingxing/RxLife)，相较于[trello/RxLifecycle](https://github.com/trello/RxLifecycle)、[uber/AutoDispose](https://github.com/uber/AutoDispose)，具有如下优势：

 * 直接支持在主线程回调
 * 支持在子线程订阅观察者
 * 简单易用，学习成本低
 * 性能更优，在实现上更加简单
 
**RxHttp&RxLife 交流群：378530627**

**友情提示: RxLife与[RxHttp](https://github.com/liujingxing/RxHttp)搭配使用，味道更佳**

**RxLife详细介绍：https://juejin.im/post/5cf3e1235188251c064815f1**



**Gradle引用**

    dependencies {
       implementation 'com.rxjava.rxlife:rxlife:1.1.0'
       
       //if you use AndroidX
       implementation 'com.rxjava.rxlife:rxlife-x:1.1.0'
    }


#Usage

### 1、Activity/Fragment
Activity/Fragment销毁时，自动关闭RxJava管道

```java
Observable.timer(5, TimeUnit.SECONDS)
    .as(RxLife.as(this))     //此时的this Activity/Fragment对象
    .subscribe(aLong -> {
        Log.e("LJX", "accept =" + aLong);
    });
```

### 2、View
View被移除时，自动关闭RxJava管道
```java
Observable.timer(5, TimeUnit.SECONDS)
    .as(RxLife.as(this))  //此时的this 为View对象
    .subscribe(aLong -> {
        Log.e("LJX", "accept =" + aLong);
    });

```

### 3、ViewModel
Activity/Fragment销毁时，自动关闭RxJava管道，ViewModel需要继承`ScopeViewModel`类，如下

```java
public class MyViewModel extends ScopeViewModel {

    public MyViewModel(@NonNull Application application) {
        super(application);
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .as(RxLife.asOnMain(this))
            .subscribe(aLong -> {
                Log.e("LJX", "MyViewModel aLong=" + aLong);
            });
    }
}
```

**注意:** 一定要在Activity/Fragment通过以下方式获取ViewModel对象，否则RxLife接收不到生命周期的回调

```java

MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

```

### 4、任意类
Activity/Fragment销毁时，自动关闭RxJava管道，任意类需要继承`BaseScope`类，如P层：

```java
public class Presenter extends BaseScope {

    public Presenter(LifecycleOwner owner) {
        super(owner); //添加生命周期监听
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .as(RxLife.as(this)) //这里的this 为Scope接口对象
            .subscribe(aLong -> {
                Log.e("LJX", "accept aLong=" + aLong);
            });
    }
}
```

### 5、kotlin用户

由于`as`是kotlin中的一个关键字，所以在kotlin中，我们并不能直接使用`as(RxLife.as(this))`,可以如下编写

```java
Observable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
    .`as`(RxLife.`as`(this))
    .subscribe { aLong ->
        Log.e("LJX", "accept=" + aLong)
    }
```

当然，相信没多少人会喜欢这种写法，故，RxLife针对kotlin用户，新增更为便捷的写法，如下：

```java
Observable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
    .life(this)
    .subscribe { aLong ->
        Log.e("LJX", "accept=" + aLong)
    }
```
使用`life` 操作符替代`as`操作符即可，其它均一样


### 6、小彩蛋

#### asOnMain操作符
RxLife还提供了`asOnMain`操作符，它可以指定下游的观察者在主线程中回调，如下：
```java
Observable.timer(5, TimeUnit.SECONDS)
    .as(RxLife.asOnMain(this))
    .subscribe(aLong -> {
        //在主线程回调
       Log.e("LJX", "accept =" + aLong);
    });

        //等价于
Observable.timer(5, TimeUnit.SECONDS)
    .observeOn(AndroidSchedulers.mainThread())
    .as(RxLife.as(this))
    .subscribe(aLong -> {
        //在主线程回调
        Log.e("LJX", "accept =" + aLong);
    });

```

kotlin 用户使用`lifeOnMain`替代`asOnMain`操作符，其它均一样

**注意:** RxLife类里面as操作符，皆适用于Flowable、ParallelFlowable、Observable、Single、Maybe、Completable这6个被观察者对象


### 混淆

RxLife作为开源库，可混淆，也可不混淆，如果不希望被混淆，请在proguard-rules.pro文件添加以下代码

```java
-keep class com.rxjava.rxlife.**{*;}
```


# 更新日志

**1.1.0**

  - RxLife类增加as(View,boolean)、asOnMain(View,boolean)方法

  - 删除过时的lift、compose等方法

**1.0.9**

  - kotlin中，支持在ViewModel及任意类使用life、lifeOnMain操作符

**1.0.8**

  - 修复在Activity/Fragment中，子线程订阅事件时，偶现观察者没有回调问题

**1.0.7**

  - BaseScope、ScopeViewModel两个类加入RxLife 此版本中

**1.0.6**
  - 代码优化
 
**1.0.5**
  - 引入作用域的概念，支持在View中自动中断RxJava管道

  - 对Kotlin简单适配，在kotlin中可使用life操作符绑定生命周期

**1.0.4**

  - 新增as操作符，规定下游只能使用subscribe操作符

  - lift、compose 标记为过时，在未来的版本中将会删除，请使用as操作符替代






