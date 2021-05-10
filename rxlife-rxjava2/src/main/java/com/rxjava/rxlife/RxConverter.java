package com.rxjava.rxlife;

import io.reactivex.*;
import io.reactivex.parallel.ParallelFlowableConverter;

/**
 * User: ljx
 * Date: 2019/4/18
 * Time: 18:40
 */
public interface RxConverter<T> extends
        ObservableConverter<T, ObservableLife<T>>,
        FlowableConverter<T, FlowableLife<T>>,
        ParallelFlowableConverter<T, ParallelFlowableLife<T>>,
        MaybeConverter<T, MaybeLife<T>>,
        SingleConverter<T, SingleLife<T>>,
        CompletableConverter<CompletableLife> {
}
