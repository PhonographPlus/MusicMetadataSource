/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms

import retrofit2.Call
import mms.util.RestResult
import mms.util.emit
import android.content.Context
import kotlinx.coroutines.Deferred

abstract class AbsClientDelegate<A : Action, R>(var exceptionHandler: ExceptionHandler) {

    abstract fun request(context: Context, action: A): Deferred<R?>

    /**
     * unwrap [RestResult]
     */
    protected suspend fun <T> Call<RestResult<T>?>.process(): T? {
        when (val result = emit<T>()) {
            is RestResult.ParseError   -> exceptionHandler.reportError(result.exception, TAG, "Parse error!")
            is RestResult.NetworkError -> exceptionHandler.reportError(result.exception, TAG, "Network error!")
            is RestResult.RemoteError  -> exceptionHandler.warning(TAG, result.message)
            is RestResult.Success      -> return result.data
        }
        return null
    }

    interface ExceptionHandler {
        fun reportError(e: Throwable, tag: String, message: String)
        fun warning(tag: String, message: String)
    }

    protected companion object {
        private const val TAG = "WebSearch"
    }
}