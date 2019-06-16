# RxLife

详细介绍：https://juejin.im/post/5cf3e1235188251c064815f1

**Gradle引用**

    dependencies {
       implementation 'com.rxjava.rxlife:rxlife:1.0.7'

       //if you use AndroidX
       implementation 'com.rxjava.rxlife:rxlife-x:1.0.7'
    }


#Usage

### 1、Activity/Fragment

```java
Observable.timer(5, TimeUnit.SECONDS)
        .as(RxLife.as(this))     //此时的this Activity/Fragment对象
        .subscribe(aLong -> {
            Log.e("LJX", "accept =" + aLong);
        });
```

### 2、View

```java
Observable.timer(5, TimeUnit.SECONDS)
        .as(RxLife.as(this))  //此时的this 为View对象
        .subscribe(aLong -> {
            Log.e("LJX", "accept =" + aLong);
        });

```

### 3、ViewModel

ViewModel需要继承`ScopeViewModel`类，如下

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

    //注意:ViewModel
    //ViewModelProviders.of(this).get(MyViewModel.class)
}
```

#### 注意
一定要在Activity/Fragment通过以下方式获取ViewModel对象，否则RxLife接收不到生命周期的回调
```java

MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

```

### 4、任意类

任意类需要继承`BaseScope`类，如P层：

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

### kotlin用户

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


### 小彩蛋

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

**注意**
RxLife类里面as操作符，皆适用于Flowable、ParallelFlowable、Observable、Single、Maybe、Completable这6个被观察者对象





# 更新日志

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






