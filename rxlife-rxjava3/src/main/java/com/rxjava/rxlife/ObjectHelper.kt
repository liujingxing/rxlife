package com.rxjava.rxlife

/**
 * User: ljx
 * Date: 2020/4/12
 * Time: 12:15
 */

object ObjectHelper {

    @JvmStatic
    fun <T> requireNonNull(any: T?, message: String): T {
        if (any == null) {
            throw NullPointerException(message)
        }
        return any
    }
}