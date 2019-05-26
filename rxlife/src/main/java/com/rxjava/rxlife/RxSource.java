/**
 * Copyright (c) 2016-present, RxJava Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.rxjava.rxlife;

import io.reactivex.disposables.Disposable;

/**
 * User: ljx
 * Date: 2019/4/18
 * Time: 18:40
 */
abstract class RxSource<E> {

    Scope   scope;
    boolean onMain;

    RxSource(Scope scope, boolean onMain) {
        this.scope = scope;
        this.onMain = onMain;
    }

    public abstract Disposable subscribe();

    /**
     * Subscribes the given Observer to this ObservableSource instance.
     *
     * @param observer the Observer, not null
     * @throws NullPointerException if {@code observer} is null
     */
    public abstract void subscribe(E observer);

    public <O extends E> O subscribeWith(O observer) {
        subscribe(observer);
        return observer;
    }
}
