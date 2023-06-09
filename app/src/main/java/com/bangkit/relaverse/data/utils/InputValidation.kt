package com.bangkit.relaverse.data.utils

import android.text.TextUtils
import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}

fun String.isValidPhoneNumber(): Boolean {
    val regex = Regex("^08[0-9]{8,10}\$")
    return regex.matches(this)
}