/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms.musicbrainz

import mms.QueryParameter

data class MusicbrainzQueryParameter(var target: MusicBrainzAction.Target, var query: String) : QueryParameter {
    override fun check(): Boolean = query.isNotEmpty()
    override fun toAction(): MusicBrainzAction.Search = MusicBrainzAction.Search(target, query)
}