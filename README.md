[![](https://jitpack.io/v/liujingxing/rxlife.svg)](https://jitpack.io/#liujingxing/rxlife)

***RxHttp&RxLife 交流群：378530627   &nbsp;&nbsp;&nbsp;&nbsp;  个人微信：ljx-studio*** 

# RxLife
[RxLife](https://github.com/liujingxing/RxLife)，相较于[trello/RxLifecycle](https://github.com/trello/RxLifecycle)、[uber/AutoDispose](https://github.com/uber/AutoDispose)，具有如下优势：

 * 直接支持在主线程回调
 * 支持在子线程订阅观察者
 * 简单易用，学习成本低
 * 性能更优，在实现上更加简单
 

**友情提示: RxLife与[RxHttp](https://github.com/liujingxing/RxHttp)搭配使用，味道更佳**

**RxLife详细介绍：https://juejin.im/post/5cf3e1235188251c064815f1**



**Gradle引用**

将`jitpack`添加到项目的`build.gradle`文件中，如下：
```java
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
`注：RxLife 2.1.0 版本起，已全面从JCenter迁移至jitpack`


新版本仅支持AndroidX项目
```java
dependencies {
   //kotlin协程
   implementation 'com.github.liujingxing.rxlife:rxlife-coroutine:2.1.0'

   //rxjava2
   implementation 'com.github.liujingxing.rxlife:rxlife-rxjava2:2.1.0'
       
   //rxjava3
   implementation 'com.github.liujingxing.rxlife:rxlife-rxjava3:2.1.0'
}
```

***注意：RxJava2 使用Rxlife.asXxx方法；RxJava3使用Rxlife.toXxx方法***

**非AndroidX项目**

非AndroidX项目，请使用旧版本RxLife
```java
implementation 'com.rxjava.rxlife:rxlife:2.0.0'
```
由于Google在19年就停止了非AndroidX库的更新，故rxlife旧版本不再维护，请尽快将项目迁移至AndroidX

#Usage

### 1、FragmentActivity/Fragment
FragmentActivity/Fragment销毁时，自动关闭RxJava管道

```java
Observable.timer(5, TimeUnit.SECONDS)
    .as(RxLife.as(this))     //此时的this FragmentActivity/Fragment对象
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
    }
   
    public void test(){
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .as(RxLife.asOnMain(this))  //继承ScopeViewModel后，就可以直接传this
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
    }
    
    public void test(){
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .as(RxLife.as(this)) //继承BaseScope后，就可以直接传this
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


 # **2.2.0**

 ### 2021-09-01

 - Kotlin 升级到1.5.30

 - rxLifeScope标记为过时，正式退出历史舞台
 
 # **2.1.0**

 ### 2021-05-11
 
 - 新增：`RxLife`类下新增`dispose(Disposable)、isDisposed(Disposable)`静态方法
 
 - 修改：由于JCenter停止维护，故全面迁移至jitpack，`groupId`由之前`com.ljx.rxlife`改为`com.github.liujingxing.rxlife`，版本升级时，请注意更改依赖


**2.0.0**

  - 新增RxLifeScope类，用于开启协程，并在FragmentActivity/ViewModel环境下可以自动关闭协程

  - rxlife-x 的lifecycle等组件升级到2.2.0版本

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



# Licenses
```
Copyright 2019 liujingxing

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


