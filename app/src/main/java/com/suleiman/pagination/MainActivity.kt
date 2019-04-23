package com.suleiman.pagination

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.suleiman.pagination.api.MovieApi
import com.suleiman.pagination.api.MovieService
import com.suleiman.pagination.models.PastOrderGetResponse
import com.suleiman.pagination.models.PastOrderGetResponseDataContent
import com.suleiman.pagination.models.Result
import com.suleiman.pagination.utils.PaginationAdapterCallback
import com.suleiman.pagination.utils.PaginationScrollListener
import java.util.concurrent.TimeoutException

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), PaginationAdapterCallback {

    internal var adapter: PaginationAdapter?=null
    internal var linearLayoutManager: LinearLayoutManager?=null
//    var currentOrderList: MutableList<PastOrderGetResponseDataContent>? = null
    internal var rv: RecyclerView?=null
    internal var progressBar: ProgressBar?=null
    internal var errorLayout: LinearLayout?=null
    internal var btnRetry: Button?=null
    internal var txtError: TextView?=null

    private var isLoading = false
    private var isLastPage = false
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private var TOTAL_PAGES:Int?=null
    private var currentPage = 0



    private var movieService: MovieService? = null

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.main_recycler)
        progressBar = findViewById(R.id.main_progress)
        errorLayout = findViewById(R.id.error_layout)
        btnRetry = findViewById(R.id.error_btn_retry)
        txtError = findViewById(R.id.error_txt_cause)

        adapter = PaginationAdapter(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv!!.layoutManager = linearLayoutManager
        rv!!.itemAnimator = DefaultItemAnimator()
        rv!!.adapter = adapter


        rv!!.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager!!) {
            override val totalPageCount: Int
                 get() = TOTAL_PAGES!!

            override val isLastPage: Boolean = false

//                get() =  this.isLastPage

            override var isLoading: Boolean = false
//                get() = false
//                set(value) {
////                    this.isLoading=isLoading
//                }

            override fun loadMoreItems() {
                isLoading = true
                currentPage ++

                loadPage()
            }

        })

        //init service and load data
        movieService = MovieApi.client.create(MovieService::class.java)

        loadPage()

        btnRetry!!.setOnClickListener { loadPage() }

    }

//    fun addOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
//        this.mOnLoadMoreListener = mOnLoadMoreListener
//    }


    private fun loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ")

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView()

        callTopRatedMoviesApi().enqueue(object : Callback<PastOrderGetResponse> {
            override fun onResponse(call: Call<PastOrderGetResponse>, response: Response<PastOrderGetResponse>) {
                // Got data. Send it to adapter

                hideErrorView()

                val results = fetchResults(response)

                progressBar!!.visibility = View.GONE
                adapter!!.addAll(results)

                if (currentPage <TOTAL_PAGES!!)
                    adapter!!.addLoadingFooter()
                else
                    isLastPage = true
                isLoading=false
            }

            override fun onFailure(call: Call<PastOrderGetResponse>, t: Throwable) {
                t.printStackTrace()
                showErrorView(t)
            }
        })
    }
    private fun loadPage() {
        Log.d(TAG, "loadFirstPage: ")

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView()

        callTopRatedMoviesApi().enqueue(object : Callback<PastOrderGetResponse> {
            override fun onResponse(call: Call<PastOrderGetResponse>, response: Response<PastOrderGetResponse>) {
                // Got data. Send it to adapter

                hideErrorView()

                val results = fetchResults(response)

                progressBar!!.visibility = View.GONE
                if (currentPage != PAGE_START) adapter!!.removeLoadingFooter()
                adapter!!.addAll(results)
                if (currentPage <=TOTAL_PAGES!!)
                    adapter!!.addLoadingFooter()
                else
                    adapter!!.removeLoadingFooter()
                isLastPage = true
                isLoading=false
            }

            override fun onFailure(call: Call<PastOrderGetResponse>, t: Throwable) {
                t.printStackTrace()
                showErrorView(t)
            }
        })
    }

    /**
     * @param response extracts List<[&gt;][Result] from response
     * @return
     */
    private fun fetchResults(response: Response<PastOrderGetResponse>): List<PastOrderGetResponseDataContent> {
        val topRatedMovies = response.body()
        TOTAL_PAGES=response.body()!!.data!!.totalPages
        isLastPage=response.body()!!.data!!.last!!
        return topRatedMovies!!.data!!.content!!
    }

    private fun loadNextPage() {
        Log.d(TAG, "loadNextPage: $currentPage")

        callTopRatedMoviesApi().enqueue(object : Callback<PastOrderGetResponse> {
            override fun onResponse(call: Call<PastOrderGetResponse>, response: Response<PastOrderGetResponse>) {
                adapter!!.removeLoadingFooter()
                isLoading = false

                val results = fetchResults(response)
                adapter!!.addAll(results)

                if (currentPage != TOTAL_PAGES)
                    adapter!!.addLoadingFooter()
                else
                    isLastPage = true
            }

            override fun onFailure(call: Call<PastOrderGetResponse>, t: Throwable) {
                t.printStackTrace()
                adapter!!.showRetry(true, fetchErrorMessage(t))
            }
        })
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As [.currentPage] will be incremented automatically
     * by @[PaginationScrollListener] to load next page.
     */
    private fun callTopRatedMoviesApi(): Call<PastOrderGetResponse> {
        return movieService!!.getCustomerOderDetail(1536,"Ordered,Confirmed,Arrived",
                currentPage,20
        )
    }


    override fun retryPageLoad() {
        loadPage()
    }


    /**
     * @param throwable required for [.fetchErrorMessage]
     * @return
     */
    private fun showErrorView(throwable: Throwable) {

        if (errorLayout!!.visibility == View.GONE) {
            errorLayout!!.visibility = View.VISIBLE
            progressBar!!.visibility = View.GONE

            txtError!!.text = fetchErrorMessage(throwable)
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private fun fetchErrorMessage(throwable: Throwable): String {
        var errorMsg = resources.getString(R.string.error_msg_unknown)

        if (!isNetworkConnected) {
            errorMsg = resources.getString(R.string.error_msg_no_internet)
        } else if (throwable is TimeoutException) {
            errorMsg = resources.getString(R.string.error_msg_timeout)
        }

        return errorMsg
    }

    // Helpers -------------------------------------------------------------------------------------


    private fun hideErrorView() {
        if (errorLayout!!.visibility == View.VISIBLE) {
            errorLayout!!.visibility = View.GONE
            progressBar!!.visibility = View.VISIBLE
        }
    }

    companion object {

        private val TAG = "MainActivity"

        private val PAGE_START = 0
    }
}
