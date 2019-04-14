package app.task.pinterestsample.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/*
 * Created by venkatesh on 14-Apr-19.
 */

class NetworkUtil {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = cm.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}