package com.rxjava.rxlife

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * User: ljx
 * Date: 2020-03-14
 * Time: 10:30
 */
open class RxLifeScope() : Closeable {

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

    //协程异常回调
    private val exceptionHandler = CoroutineExceptionHandler { coroutine, throwable ->
        catch(coroutine, throwable)
    }

    private var onError: ((Throwable) -> Unit)? = null

    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + exceptionHandler)

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(block, null)
    }

    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        error: ((Throwable) -> Unit)? = null
    ): Job {
        onError = error
        return coroutineScope.launch { block() }
    }

    //出现异常时执行
    private fun catch(coroutine: CoroutineContext, e: Throwable) {
        onError?.invoke(e)
    }

    override fun close() {
        coroutineScope.cancel()
    }
}
