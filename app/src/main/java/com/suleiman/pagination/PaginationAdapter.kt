package com.suleiman.pagination

import android.content.Context
import android.net.ParseException
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.suleiman.pagination.models.PastOrderGetResponseDataContent
import com.suleiman.pagination.models.Result
import com.suleiman.pagination.utils.PaginationAdapterCallback
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Suleiman on 19/10/16.
 */

class PaginationAdapter internal constructor(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentOrderList: MutableList<PastOrderGetResponseDataContent>? = null

    private var isLoadingAdded = false
    private var retryPageLoad = false

    private val mCallback: PaginationAdapterCallback

    private var errorMsg: String? = null


    var movies: MutableList<PastOrderGetResponseDataContent>?
        get() = currentOrderList
        set(currentOrderList) {
            this.currentOrderList = currentOrderList
        }

    val isEmpty: Boolean
        get() = itemCount == 0

    init {
        this.mCallback = context as PaginationAdapterCallback
        currentOrderList = ArrayList()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.currentorder_rv_view, parent, false)
                viewHolder = CurrentOrderViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
//            HERO -> {
//                val viewHero = inflater.inflate(R.layout.item_hero, parent, false)
//                viewHolder = HeroVH(viewHero)
//            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val result = currentOrderList!![i] // Movie

        when (getItemViewType(i)) {

//            HERO -> {
//                val heroVh = holder as HeroVH
//
//                heroVh.mMovieTitle.text = result.customerOrderId
////                heroVh.mYear.text = formatYearLabel(result)
////                heroVh.mMovieDesc.text = result.overview
//
////                loadImage(result.backdropPath)
////                        .into(heroVh.mPosterImg)
//            }

            ITEM -> {
                val currentOrderViewHolder = holder as CurrentOrderViewHolder
                

                    currentOrderViewHolder.tvpicktimecurrent!!.text = currentOrderList!![i].deliveryTime.toString()
                    currentOrderViewHolder.tvorderidcurrent!!.text = "ID: #" +
                            currentOrderList!![i].customerOrderId.toString()
                    val datedisplay =
                            currentOrderList!![i].cart!![0].orderCartDate.toString()
                    val date = changeDateFormat("yyyy-MM-dd HH:mm:ss", "MMMM d, hh:mm a", datedisplay)

                    currentOrderViewHolder.tvordertimecurrent!!.text = date

                    if (currentOrderList!![i].cart!!.isEmpty()) {
                        currentOrderViewHolder.tvordertName!!.text = " "
                    } else {

                        var finalitems: StringBuffer?
                        finalitems = StringBuffer()

                        for (k in 0 until currentOrderList!![i].cart!!.size) {
                            var foodname =
                                    currentOrderList!![i].cart!![k].menu!!.foodItemName!!.toString()
                            var foodquantity =
                                    currentOrderList!![i].cart!![k].quantity!!.toString()
                            var itemdisplay = foodname + " X" + foodquantity
                            finalitems.append(itemdisplay)

                            if (k != currentOrderList!![i].cart!!.size - 1) {
                                finalitems.append(", ")
                            }

                        }
                        currentOrderViewHolder.tvordertName!!.text = finalitems
                    }

                    var statusupdate = currentOrderList!![i].status

                    if (statusupdate.equals("Ordered")) {
                        currentOrderViewHolder.progessStatus!!.progress = 1
                        currentOrderViewHolder.tvStatusOrdered!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorAccent
                                )
                        )
                        currentOrderViewHolder.tvStatusConfirm!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorAccent
                                )
                        )
                        currentOrderViewHolder.tvStatusArrived!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorAccent
                                )
                        )
                    } else if (statusupdate.equals("Confirmed")) {
                        currentOrderViewHolder.progessStatus!!.progress = 2
                        currentOrderViewHolder.tvStatusOrdered!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorPrimary
                                )
                        )
                        currentOrderViewHolder.tvStatusConfirm!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorPrimary
                                )
                        )
                        currentOrderViewHolder.tvStatusArrived!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorAccent
                                )
                        )
                    } else if (statusupdate.equals("Arrived")) {
                        currentOrderViewHolder.progessStatus!!.progress = 3
                        currentOrderViewHolder.tvStatusOrdered!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorPrimary
                                )
                        )
                        currentOrderViewHolder.tvStatusConfirm!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorPrimary
                                )
                        )
                        currentOrderViewHolder.tvStatusArrived!!.setTextColor(
                                ContextCompat.getColor(
                                        currentOrderViewHolder.itemView.context!!,
                                        R.color.colorPrimary
                                )
                        )
                    }


                    currentOrderViewHolder.tvorderpricecurrent!!.text = "\u20B9 " +
                            currentOrderList!![i].totalAmount.toString()

                    var orderCartDetailIdIntent = currentOrderList!![i].customerOrderId
//            var myboxstoreId = currentOrderList[i].myboxStore!!.myBoxStoreId

/*
                    currentOrderViewHolder.imgClick!!.setOnClickListener {

                        val intentfrom = Intent(currentOrderViewHolder.itemView.context!!, OrderDetailsActivity::class.java)
                        intentfrom.putExtra("orderItemId", orderCartDetailIdIntent)
//                intentfrom.putExtra("myboxstoreId", myboxstoreId)
                        currentOrderViewHolder.itemView.context!!.startActivity(intentfrom)
                    }
*/


                }

            LOADING -> {
                val loadingVH = holder as LoadingVH

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.visibility = View.VISIBLE
                    loadingVH.mProgressBar.visibility = View.GONE

                    loadingVH.mErrorTxt.text = if (errorMsg != null)
                        errorMsg
                    else
                        context.getString(R.string.error_msg_unknown)

                } else {
                    loadingVH.mErrorLayout.visibility = View.GONE
                    loadingVH.mProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun changeDateFormat(currentFormat: String, requiredFormat: String, dateString: String): String {
        var result = ""
        val formatterOld = SimpleDateFormat(currentFormat, Locale.getDefault())
        val formatterNew = SimpleDateFormat(requiredFormat, Locale.getDefault())
        var date: Date? = null
        try {
            date = formatterOld.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date != null) {
            result = formatterNew.format(date)
        }
        return result
    }


    override fun getItemCount(): Int {
        return if (currentOrderList == null) 0 else currentOrderList!!.size
    }

    override fun getItemViewType(position: Int): Int {

//        return if (isLoadingAdded) {
           return if (position == currentOrderList!!.size - 1) LOADING else ITEM
//        } else {
//            ITEM
//        }

//        return if (position == 0) {
//            HERO
//        } else {
//            if (position == currentOrderList!!.size - 1 && isLoadingAdded) LOADING else ITEM
//        }
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

    /**
     * @param result
     * @return [releasedate] | [2letterlangcode]
     */
    private fun formatYearLabel(result: Result): String {
        return (result.releaseDate.substring(0, 4)  // we want the year only

                + " | "
                + result.originalLanguage.toUpperCase())
    }

    /**
     * Using Glide to handle image loading.
     * Learn more about Glide here:
     * [](http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/)
     *
     *
     * //     * @param posterPath from [Result.getPosterPath]
     *
     * @return Glide builder
     */
    //    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
    //        return Glide
    //                .with(context)
    //                .load(BASE_URL_IMG + posterPath)
    //                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
    //                .centerCrop()
    //                .crossFade();

    //    }
/*
    private fun loadImage(posterPath: String): GlideRequest<Drawable> {
        return GlideApp
                .with(context)
                .load(BASE_URL_IMG + posterPath)
                .centerCrop()
    }
*/


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    fun add(r: PastOrderGetResponseDataContent) {
        currentOrderList!!.add(r)
        notifyItemInserted(currentOrderList!!.size - 1)
    }

    fun addAll(moveResults: List<PastOrderGetResponseDataContent>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: PastOrderGetResponseDataContent?) {
        val position = currentOrderList!!.indexOf(r)
        if (position > -1) {
            currentOrderList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(PastOrderGetResponseDataContent())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = currentOrderList!!.size - 1
        val result = getItem(position)

        if (result != null) {
            currentOrderList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): PastOrderGetResponseDataContent? {
        return currentOrderList!!.get(position)
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(currentOrderList!!.size - 1)

        if (errorMsg != null) this.errorMsg = errorMsg
    }


    /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
//     inner class HeroVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
//         val mMovieTitle: TextView
//        val mMovieDesc: TextView
//         val mYear: TextView // displays "year | language"
//         val mPosterImg: ImageView
//
//        init {
//
//            mMovieTitle = itemView.findViewById(R.id.movie_title)
//            mMovieDesc = itemView.findViewById(R.id.movie_desc)
//            mYear = itemView.findViewById(R.id.movie_year)
//            mPosterImg = itemView.findViewById(R.id.movie_poster)
//        }
//    }

    /**
     * Main list's content ViewHolder
     */
    class CurrentOrderViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        internal var session: UserSession

        internal var tvorderidcurrent: TextView? = null
        internal var tvpicktimecurrent: TextView? = null
        internal var tvorderpricecurrent: TextView? = null
        internal var tvordertimecurrent: TextView? = null
        internal var tvStatusOrdered: TextView? = null
        internal var tvStatusArrived: TextView? = null
        internal var tvStatusConfirm: TextView? = null
        internal var tvordertName: TextView? = null
        internal var progessStatus: ProgressBar? = null
        internal var imgClick: ImageView? = null


        init {
            tvorderidcurrent = itemView.findViewById(R.id.tv_curr_orderid)
            tvorderpricecurrent = itemView.findViewById(R.id.tv_curr_price)
            tvordertimecurrent = itemView.findViewById(R.id.tv_current_ordertime)
            tvordertName = itemView.findViewById(R.id.tv_curr_ordername)
            tvpicktimecurrent = itemView.findViewById(R.id.tv_curr_time)
            progessStatus = itemView.findViewById(R.id.horizontal_progress_status)
            imgClick = itemView.findViewById(R.id.img_current_order)
            tvStatusArrived = itemView.findViewById(R.id.tv_arrived_status)
            tvStatusConfirm = itemView.findViewById(R.id.tv_confirm_status)
            tvStatusOrdered = itemView.findViewById(R.id.tv_order_status)

//            session = UserSession(itemView.context!!)
        }
    }


     inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
         val mProgressBar: ProgressBar
         val mRetryBtn: ImageButton
         val mErrorTxt: TextView
         val mErrorLayout: LinearLayout

        init {

            mProgressBar = itemView.findViewById(R.id.loadmore_progress)
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry)
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt)
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout)

            mRetryBtn.setOnClickListener(this)
            mErrorLayout.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.loadmore_retry, R.id.loadmore_errorlayout -> {

                    showRetry(false, null)
                    mCallback.retryPageLoad()
                }
            }
        }
    }

    companion object {

        // View Types
        private val ITEM = 0
        private val LOADING = 1
//        private val HERO = 2

        private val BASE_URL_IMG = "https://image.tmdb.org/t/p/w200"
    }

}
