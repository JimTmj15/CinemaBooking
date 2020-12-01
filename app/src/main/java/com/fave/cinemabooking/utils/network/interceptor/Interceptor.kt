package com.fave.cinemabooking.utils.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/*
* This class functions to intercept network status before making a network request
* to backend via API interfaces. An exception will be threw if the phone is not connected
* to Internet (via WiFi or Cellular)
*
* This class as a singleton instance for okhttp client to intercept.
*/
@Singleton
class Interceptor @Inject constructor(@ApplicationContext private val context: Context) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnectionOn()) {
            throw NoConnectivityException()
        }

        return chain.proceed(chain.request())
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null) {
            return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
        }
        return false
    }

    class NoConnectivityException : IOException() {
        override val message: String
            get() = "No network available, please check your WiFi or Mobile connection"
    }
}
