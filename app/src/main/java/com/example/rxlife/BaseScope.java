package com.example.rxlife;

import com.rxjava.rxlife.Scope;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * User: ljx
 * Date: 2019-05-31
 * Time: 21:50
 */
public class BaseScope implements Scope {

    private CompositeDisposable mDisposables;

    @Override
    public void onScopeStart(Disposable d) {
        addDisposable(d);
    }

    @Override
    public void onScopeEnd() {

    }

    private void addDisposable(Disposable disposable) {
        CompositeDisposable disposables = mDisposables;
        if (disposables == null) {
            disposables = mDisposables = new CompositeDisposable();
        }
        disposables.add(disposable);
    }

    public void dispose() {
        final CompositeDisposable disposables = mDisposables;
        if (disposables == null) return;
        disposables.dispose();
    }
}
