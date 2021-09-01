package com.rxlife.coroutine

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * User: ljx
 * Date: 2020-03-14
 * Time: 10:30
 */


/**
 * rxLifeScope已被废弃，请使用 [lifecycleScope]或[viewModelScope] 替代
 * 如
 * ```
 * //lifecycleScope
 * implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
 * //viewModelScope
 * implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
 * ```
 * ```
 * lifecycleScope.launch {
 *     //执行业务逻辑
 * }
 * 或
 * viewModelScope.launch {
 *     //执行业务逻辑
 * }
 * ```
 *
 * 为啥被废弃？
 *
 * 1、同一个FragmentActivity/Fragment下，[rxLifeScope]与[lifecycleScope]不能共用；同一个ViewModel下，[rxLifeScope]与[viewModelScope]不能共用
 *
 * 2、配合[RxHttp]发请求时，每次都要开启一个协程来捕获异常，这对于再次封装的人，非常不友好；
 * 目前[RxHttp]的[awaitResult]操作符一样可以捕获异常，所以[rxLifeScope]就没了用武之地，是时候退出历史舞台了
 *
 * 3、不能同[lifecycleScope]或[viewModelScope]一样，开启协程时，传入[CoroutineContext]或[CoroutineStart]参数
 * 亦没有一系列[launchXxx]方法
 *
 * 4、[rxLifeScope]配合[RxHttp] v2.6.6及以上版本发请求时，调用[async]方法将导致请求结束回调不被调用
 *
 *
 *
 *
 * rxLifeScope配合RxHttp请求时，请求开始/请求结束/请求异常回调如何替代？如下
 *
 * ```
 * lifecycleScope.launch {
 *      //请求开始
 *     RxHttp.get("...")
 *         .toStr()
 *         .awaitResult {
 *              //请求成功
 *         }.onFailure {
 *              //请求异常
 *         }
 *     //请求结束
 * }
 * ```
 */

@Deprecated(message = "", replaceWith = ReplaceWith("lifecycleScope"))
class RxLifeScope() : Closeable {

    constructor(
        owner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this(owner.lifecycle, lifeEvent)

    constructor(
        lifecycle: Lifecycle,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this() {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) {
                    close()
                    lifecycle.removeObserver(this)
                }
            }
        })
    }

    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(block, null)
    }

    /**
     * @param block     协程代码块，运行在UI线程
     * @param onError   异常回调，运行在UI线程
     * @param onStart   协程开始回调，运行在UI线程
     * @param onFinally 协程结束回调，不管成功/失败，都会回调，运行在UI线程
     */
    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((Throwable) -> Unit)? = null,
        onStart: (() -> Unit)? = null,
        onFinally: (() -> Unit)? = null
    ): Job {
        return coroutineScope.launch {
            try {
                coroutineScope {
                    onStart?.invoke()
                    block()
                }
            } catch (e: Throwable) {
                if (onError != null && isActive) {
                    try {
                        onError(e)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    e.printStackTrace()
                }
            } finally {
                onFinally?.invoke()
            }
        }
    }

    override fun close() {
        coroutineScope.cancel()
    }
}
