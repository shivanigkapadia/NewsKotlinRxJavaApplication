package com.example.newskotlinapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

class Constants{
    companion object{
        const val API_KEY = "YOUR_API_KEY"
        const val SORT_BY = "publishedAt"
        const val QUERY = "bitcoin"
        const val DATE_FORMAT = "yyyy-dd-MM"

         fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun displayToast(
            context: Context?,
            message: String?
        ) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
