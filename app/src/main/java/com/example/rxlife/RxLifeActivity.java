package com.example.rxlife;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle.Event;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import io.reactivex.*;

@SuppressLint("CheckResult")
public class RxLifeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_life_activity);
    }

    public void observable(View view) {
        Observable.timer(5, TimeUnit.SECONDS)
//                .lift(RxLife.lift(this))
                .compose(RxLife.compose(this, Event.ON_STOP))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void flowable(View view) {
        Flowable.timer(5, TimeUnit.SECONDS)
//                .lift(RxLife.lift(this))
                .compose(RxLife.compose(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void single(View view) {
        Single.timer(5, TimeUnit.SECONDS)
//                .lift(RxLife.lift(this))
                .compose(RxLife.compose(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void maybe(View view) {
        Maybe.timer(5, TimeUnit.SECONDS)
//                .lift(RxLife.lift(this))
                .compose(RxLife.compose(this))
                .subscribe(aLong -> {
                    Log.e("LJX", "accept =" + aLong);
                });
    }

    public void completable(View view) {
        Completable.timer(5, TimeUnit.SECONDS)
//                .lift(RxLife.lift(this))
                .compose(RxLife.compose(this))
                .subscribe(() -> {
                    Log.e("LJX", "run");
                });
    }
}
