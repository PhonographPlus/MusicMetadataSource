/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms.musicbrainz

import mms.util.JsonDeserializationRetrofitConverter
import mms.AbsRestClient
import android.content.Context

class MusicBrainzRestClient(context: Context, userAgent: String) : AbsRestClient<MusicBrainzService>(
    okHttpClientConfig = { defaultCache(context, "/okhttp-musicbrainz/") },
    headerConfig = {},
    retrofitConfig = {
        baseUrl(BASE_URL)
        addConverterFactory(JsonDeserializationRetrofitConverter.Factory())
    },
    apiServiceClazz = MusicBrainzService::class.java,
    customUserAgent = userAgent
) {
    companion object {
        const val BASE_URL = "https://musicbrainz.org/ws/2/"
    }
}