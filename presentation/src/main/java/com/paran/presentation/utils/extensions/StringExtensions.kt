package com.paran.presentation.utils.extensions

fun String.dateToFormat() = this.split("/").joinToString("").toInt().toString()