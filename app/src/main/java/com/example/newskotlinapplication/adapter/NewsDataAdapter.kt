package com.example.newskotlinapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newskotlinapplication.R
import com.example.newskotlinapplication.model.NewsDataResponseClass
import kotlinx.android.synthetic.main.row_layout.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NewsDataAdapter(
    val context: Context,
    var newsResponseArrayList: ArrayList<NewsDataResponseClass>,
    val mOnCLickListener:onCLickListener
) : RecyclerView.Adapter<NewsDataAdapter.ViewHolder>() {

    fun updateData(newsDataResponseArrayList: ArrayList<NewsDataResponseClass>) {
        newsResponseArrayList = newsDataResponseArrayList
        notifyDataSetChanged()
    }

    interface onCLickListener {
        fun onCLick(view: View?, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvNewsDetail = itemView.cvNewsDetail
        var ivNewsImage = itemView.ivNewsImage
        var tvTitle = itemView.tvTitle
        var tvDetail = itemView.tvDetail
        var tvAuthorName = itemView.tvAuthorName
        var tvDash = itemView.tvDash
        var tvPublishedAt = itemView.tvPublishedAt
        var ivMore = itemView.ivMore

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsDataAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsResponseArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //convert date
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        var date: Date? = null
        try {
            date = dateFormat.parse(
                newsResponseArrayList.get(position).publishedAt
            ) //You will get date object relative to server/client timezone wherever it is parsed
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        holder.tvPublishedAt.setText(formatter.format(date))

        if (newsResponseArrayList.get(position)
                .author != null && !newsResponseArrayList.get(position).author
                .equals("")
        ) {
            holder.tvAuthorName.setText(newsResponseArrayList.get(position).author)
        } else {
            holder.tvDash.setVisibility(View.GONE)
            holder.tvAuthorName.setVisibility(View.GONE)
        }
        holder.tvTitle.setText(newsResponseArrayList.get(position).title)
        var lineCount = intArrayOf(2)
        holder.tvTitle.post(Runnable {
            lineCount[0] = holder.tvTitle.getLineCount()
            // Use lineCount here
            if (lineCount[0] == 1) {
                holder.tvDetail.setMaxLines(2)
            } else {
                holder.tvDetail.setMaxLines(1)
            }
        })

        holder.tvDetail.setText(newsResponseArrayList.get(position).description)
        holder.cvNewsDetail.getLayoutParams().height =
            ((context.getResources().getDisplayMetrics().heightPixels / 3.5).toInt())
        holder.cvNewsDetail.setOnClickListener { v ->
            mOnCLickListener.onCLick(
                v,
                position
            )
        }

        holder.ivMore.setOnClickListener { v ->
            mOnCLickListener.onCLick(
                v,
                position
            )
        }
        Glide.with(context)
            .load(newsResponseArrayList.get(position).urlToImage)
            .centerCrop() //                .placeholder(R.drawable.news_placeholder)
            .into(holder.ivNewsImage)
    }
}