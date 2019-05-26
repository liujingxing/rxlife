package com.rxjava.rxlife;

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
public class ScopeLifecycle implements Scope, GenericLifecycleObserver {

    private Lifecycle  lifecycle;
    private Event      event;
    private Disposable disposable;

    private ScopeLifecycle(Lifecycle lifecycle, Event event) {
        this.lifecycle = lifecycle;
        this.event = event;
    }

    static ScopeLifecycle from(LifecycleOwner owner, Event event) {
        return new ScopeLifecycle(owner.getLifecycle(), event);
    }

    @Override
    public void addScopeListener(Disposable d) {
        this.disposable = d;
        removeScopeListener();
        final Lifecycle lifecycle = this.lifecycle;
        if (lifecycle == null)
            throw new NullPointerException("lifecycle is null");
        lifecycle.addObserver(this);
    }

    @Override
    public void removeScopeListener() {
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
