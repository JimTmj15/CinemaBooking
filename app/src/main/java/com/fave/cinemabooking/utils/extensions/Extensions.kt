package com.fave.cinemabooking.utils.extensions

fun Double.to2DecimalPoints():Double = "%.2f".format(this).toDouble()