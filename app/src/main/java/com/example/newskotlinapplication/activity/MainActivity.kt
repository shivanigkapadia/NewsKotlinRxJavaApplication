package com.example.newskotlinapplication.activity

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newskotlinapplication.R
import com.example.newskotlinapplication.adapter.NewsDataAdapter
import com.example.newskotlinapplication.adapter.NewsDataAdapter.onCLickListener
import com.example.newskotlinapplication.listener.PaginationListener
import com.example.newskotlinapplication.model.NewsDataResponseClass
import com.example.newskotlinapplication.network.ApiService
import com.example.newskotlinapplication.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var isLoading = false
    private var isLastPage = false
    private var currentPage =0

    private val activity = this@MainActivity
    private var newsDataAdapter:NewsDataAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var disposable: Disposable? = null

    private var date:String = ""

    private var newsResponseArrayList: ArrayList<NewsDataResponseClass> =
        ArrayList<NewsDataResponseClass>()

    private val apiService by lazy {
        ApiService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initListeners()
        callNewsApi()
    }

    private fun initViews() {
        date = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
            .format(Date())
        Log.e("date::",date)

        rvNewsList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        rvNewsList.setLayoutManager(layoutManager)
    }

    private fun initListeners() {

        //for pagination
        rvNewsList.addOnScrollListener(object :
            PaginationListener((layoutManager as LinearLayoutManager?)!!) {

            override fun loadMoreItems() {
                this@MainActivity.isLoading = true
                callNewsApi()
            }

            override val isLastPage: Boolean
                get() = this@MainActivity.isLastPage
            override val isLoading: Boolean
                get() = this@MainActivity.isLoading

        })
    }

    private fun getPreviousDayDate(): String? {
        val dateFormat =
            SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        var date1: Date? = null
        try {
            date1 = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date1
        calendar.add(Calendar.DATE, -1)
        return dateFormat.format(calendar.time)
    }

    private fun callNewsApi() {

        if(Constants.isNetworkAvailable(activity)) {
            isLoading = true
            currentPage += 1

            disposable = apiService.getNewsData(
                Constants.QUERY,
                date,
                Constants.SORT_BY,
                Constants.API_KEY,
                currentPage
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        for (news in result.articles) {
                           newsResponseArrayList.add(news)
                        }
                        if (newsDataAdapter != null) {
                            newsDataAdapter!!.updateData(newsResponseArrayList)
                        } else {
                            newsDataAdapter = NewsDataAdapter(activity, newsResponseArrayList,
                                object : onCLickListener {
                                    override fun onCLick(view: View?, position: Int) {
                                    }
                                })

                            rvNewsList.adapter = newsDataAdapter
                        }
                        isLoading = false
                        if (newsResponseArrayList.size < PAGE_SIZE || newsResponseArrayList.size == 0 || result.articles.size == 0
                        ) {
                            isLoading = true
                            date = getPreviousDayDate()!!
                            currentPage = 0
                            callNewsApi()
                        }

                        Log.e("API Result", "result :" + result)
                    },
                    { error ->
                        isLoading = false
                        Log.e(" API Error", error.message)
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    }
                )
        }else{
            Constants.displayToast(activity, resources.getString(R.string.no_internet))
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
