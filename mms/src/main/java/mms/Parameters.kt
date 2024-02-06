/*
 *  Copyright (c) 2023~2024 chr_56
 */

package mms

interface QueryParameter {
    fun check(): Boolean
    fun toAction(): Action
}

