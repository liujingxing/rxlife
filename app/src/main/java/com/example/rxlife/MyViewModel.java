package com.example.rxlife;

import android.app.Application;
import androidx.annotation.NonNull;
import android.util.Log;

import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;


/**
 * User: ljx
 * Date: 2019-05-31
 * Time: 21:50
 */
public class MyViewModel extends ScopeViewModel {

    public MyViewModel(@NonNull Application application) {
        super(application);
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .to(RxLife.toMain(this))
            .subscribe(aLong -> {
                Log.e("LJX", "MyViewModel aLong=" + aLong);
            });
    }
}
