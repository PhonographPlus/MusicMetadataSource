/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms.musicbrainz

import mms.AbsClientDelegate
import mms.musicbrainz.MusicBrainzAction.Target
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MusicBrainzClientDelegate(
    context: Context,
    userAgent: String,
    exceptionHandler: ExceptionHandler,
    val scope: CoroutineScope,
) : AbsClientDelegate<MusicBrainzAction, MusicBrainzResponse>(exceptionHandler) {

    private val musicBrainzRestClient: MusicBrainzRestClient = MusicBrainzRestClient(context, userAgent)

    override fun request(context: Context, action: MusicBrainzAction): Deferred<MusicBrainzResponse?> {
        return scope.async(Dispatchers.IO) {
            val api = musicBrainzRestClient.apiService
            when (action) {
                is MusicBrainzAction.View   -> when (action.target) {
                    Target.ReleaseGroup -> api.getReleaseGroup(action.mbid).process()
                    Target.Release      -> api.getRelease(action.mbid).process()
                    Target.Artist       -> api.getArtist(action.mbid).process()
                    Target.Recording    -> api.getRecording(action.mbid).process()
                }

                is MusicBrainzAction.Search -> when (action.target) {
                    Target.ReleaseGroup -> api.searchReleaseGroup(action.query, 0).process()
                    Target.Release      -> api.searchRelease(action.query, 0).process()
                    Target.Artist       -> api.searchArtist(action.query, 0).process()
                    Target.Recording    -> api.searchRecording(action.query, 0).process()
                }
            }
        }
    }

}