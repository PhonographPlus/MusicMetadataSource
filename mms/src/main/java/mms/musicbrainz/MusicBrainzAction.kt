/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms.musicbrainz

import mms.Action
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface MusicBrainzAction : Action {

    enum class Target(val urlName: String) {
        ReleaseGroup("release-group"),
        Release("release"),
        Artist("artist"),
        Recording("recording"),
        ;

        fun link(mbid: String): String = "https://musicbrainz.org/${urlName}/$mbid"
    }

    @Parcelize
    data class Search(val target: Target, val query: String) : MusicBrainzAction, Parcelable
    @Parcelize
    data class View(val target: Target, val mbid: String) : MusicBrainzAction, Parcelable
}