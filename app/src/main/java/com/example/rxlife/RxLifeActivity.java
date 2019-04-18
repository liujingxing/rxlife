package com.example.rxlife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class RxLifeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_life_activity);
    }

    public void observable(View view) {
        Observable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
                .as(RxLife.asOnMain(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept=" + aLong + " Thread=" + Thread.currentThread());
                });
    }

    public void flowable(View view) {
        Flowable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
                .as(RxLife.asOnMain(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void single(View view) {
        Single.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void maybe(View view) {
        Maybe.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void completable(View view) {
        Completable.timer(5, TimeUnit.SECONDS)
                .as(RxLife.asOnMain(this))
                .subscribe(() -> {
                    Log.e("LJX", "run");
                });
    }

    public void leakcanary(View view) {
        Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(new MyFunction<>())
                .as(RxLife.asOnMain(this))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e("LJX", "accept =" + aLong);
                    }
                });
    }

    static class MyFunction<T> implements Function<T, T> {

        @Override
        public T apply(T t) throws Exception {
            //当dispose时，第一次睡眠会被吵醒,接着便会进入第二次睡眠
            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }

            try {
                Thread.sleep(30000);
            } catch (Exception e) {

            }
            return t;
        }
    }
}
