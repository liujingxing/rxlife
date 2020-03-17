package com.rxjava.rxlife;

import android.annotation.SuppressLint;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;

import io.reactivex.disposables.Disposable;

/**
 * Activity/Fragment作用域
 * User: ljx
 * Date: 2019-05-26
 * Time: 18:17
 */
@SuppressLint("RestrictedApi")
public final class LifecycleScope implements Scope, GenericLifecycleObserver {

    private final Lifecycle lifecycle;
    private final Event event;
    private Disposable disposable;

    private LifecycleScope(Lifecycle lifecycle, Event event) {
        this.lifecycle = lifecycle;
        this.event = event;
    }

    static LifecycleScope from(LifecycleOwner owner, Event event) {
        return new LifecycleScope(owner.getLifecycle(), event);
    }

    @Override
    public void onScopeStart(Disposable d) {
        this.disposable = d;
        onScopeEnd();
        final Lifecycle lifecycle = this.lifecycle;
        if (lifecycle == null)
            throw new NullPointerException("lifecycle is null");
        lifecycle.addObserver(this);
    }

    @Override
    public void onScopeEnd() {
        final Lifecycle lifecycle = this.lifecycle;
        if (lifecycle == null)
            throw new NullPointerException("lifecycle is null");
        lifecycle.removeObserver(this);
    }

    @Override
    public void onStateChanged(LifecycleOwner source, Event event) {
        if (event.equals(this.event)) {
            disposable.dispose();
            source.getLifecycle().removeObserver(this);
        }
    }
}
