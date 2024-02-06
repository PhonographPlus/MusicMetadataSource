/*
 *  Copyright (c) 2023~2024 chr_56
 */
@file:JvmName("LastFMUtil")

package mms.lastfm

fun List<LastFmImage>.largestUrl(): String? = maxByOrNull { it.size.ordinal }?.text

fun List<LastFmImage>.largestUrl(max: LastFmImage.ImageSize?): String? =
    if (max != null) filter { it.size.ordinal <= max.ordinal }.largestUrl() else largestUrl()
