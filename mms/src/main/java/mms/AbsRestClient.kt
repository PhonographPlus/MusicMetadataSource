/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms

import okhttp3.Cache
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import android.content.Context
import java.io.File


abstract class AbsRestClient<T> private constructor(val apiService: T) {

    protected constructor(
        okHttpClientConfig: OkHttpClient.Builder.() -> Unit,
        headerConfig: Request.Builder.() -> Unit,
        retrofitConfig: Retrofit.Builder.() -> Unit,
        apiServiceClazz: Class<T>,
        customUserAgent: String,
    ) : this(
        defaultRetrofitAdapter(defaultOkHttpClient(okHttpClientConfig, headerConfig, customUserAgent), retrofitConfig),
        apiServiceClazz
    )

    protected constructor(
        client: Call.Factory,
        retrofitConfig: Retrofit.Builder.() -> Unit,
        apiServiceClazz: Class<T>,
    ) : this(
        defaultRetrofitAdapter(client, retrofitConfig),
        apiServiceClazz
    )


    protected constructor(
        retrofit: Retrofit,
        apiServiceClazz: Class<T>,
    ) : this(retrofit.create(apiServiceClazz))

    companion object {

        private fun defaultRetrofitAdapter(
            client: Call.Factory,
            config: Retrofit.Builder.() -> Unit,
        ): Retrofit =
            Retrofit.Builder()
                .callFactory(client)
                .apply(config)
                .build()

        private fun defaultOkHttpClient(
            clientConfig: OkHttpClient.Builder.() -> Unit,
            headerConfig: Request.Builder.() -> Unit,
            customUserAgent: String,
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val modifiedRequest = chain.request().newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", customUserAgent)
                    .apply(headerConfig)
                    .build()
                chain.proceed(modifiedRequest)
            }.apply(clientConfig).build()

        fun defaultCache(context: Context, directory: String): Cache? {
            val cacheDir = File(context.cacheDir.absolutePath, directory)
            return if (cacheDir.mkdirs() || cacheDir.isDirectory) Cache(cacheDir, 1024 * 1024 * 10) else null
        }
    }
}