package com.rxjava.rxlife;

import io.reactivex.*;

/**
 * User: ljx
 * Date: 2019/4/5
 * Time: 14:06
 */
public interface RxLifeTransformer<T> extends
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {
}
