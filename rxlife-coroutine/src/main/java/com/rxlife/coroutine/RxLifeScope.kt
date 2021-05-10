package com.rxlife.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import java.io.Closeable

/**
 * 此类用于开启协程，并自动捕获异常
 *
 * 在FragmentActivity、 ViewModel环境下，使用 [rxLifeScope.launch] 方式开启协程，会在页面销毁时，自动关闭协程  (注意：这里的rxLifeScope是变量，不是类名)
 *
 * 其它环境下，需要拿到 [RxLifeScope.launch]方法的返回值后，手动调用[Job.cancel]方法关闭协程
 * User: ljx
 * Date: 2020-03-14
 * Time: 10:30
 */
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
