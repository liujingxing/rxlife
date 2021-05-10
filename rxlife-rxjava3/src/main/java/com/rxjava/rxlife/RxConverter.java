package com.rxjava.rxlife;


import io.reactivex.rxjava3.core.CompletableConverter;
import io.reactivex.rxjava3.core.FlowableConverter;
import io.reactivex.rxjava3.core.MaybeConverter;
import io.reactivex.rxjava3.core.ObservableConverter;
import io.reactivex.rxjava3.core.SingleConverter;
import io.reactivex.rxjava3.parallel.ParallelFlowableConverter;

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
