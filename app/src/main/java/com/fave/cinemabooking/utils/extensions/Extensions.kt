package com.fave.cinemabooking.utils.extensions

//to convert into 2 decimal points
fun Double.to2DecimalPoints():Double = "%.2f".format(this).toDouble()