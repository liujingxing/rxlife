package androidx.lifecycle

import android.annotation.SuppressLint
import com.rxlife.coroutine.RxLifeScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
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
private const val JOB_KEY = "androidx.lifecycle.ViewModelRxLifeScope.JOB_KEY"

@Deprecated(message = "", replaceWith = ReplaceWith("lifecycleScope"))
val ViewModel.rxLifeScope: RxLifeScope
    get() {
        val scope: RxLifeScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY, RxLifeScope())
    }

@Deprecated(message = "", replaceWith = ReplaceWith("lifecycleScope"))
val LifecycleOwner.rxLifeScope: RxLifeScope
    get() = lifecycle.rxLifeScope

@Deprecated(message = "", replaceWith = ReplaceWith("lifecycleScope"))
val Lifecycle.rxLifeScope: RxLifeScope
    @SuppressLint("RestrictedApi")
    get() {
        while (true) {
            val existing = mInternalScopeRef.get() as RxLifeScope?
            if (existing != null) {
                return existing
            }
            val newScope = RxLifeScope(this)
            if (mInternalScopeRef.compareAndSet(null, newScope)) {
                return newScope
            }
        }
    }