/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms

sealed class Source(val name: String) {
    object MusicBrainz : Source("MusicBrainz")
    object LastFm : Source("last.fm")
}