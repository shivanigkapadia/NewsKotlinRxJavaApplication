package com.example.newskotlinapplication.model

import java.util.*

data class NewsResponseClass(
    val status: String,
    val code: String,
    val message: String,
    val totalResults: Int,
    val articles: ArrayList<NewsDataResponseClass>
)

data class NewsDataResponseClass(
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

/* @SerializedName("status")
 private val status: String? = null

 @SerializedName("code")
 private val code: String? = null

 @SerializedName("message")
 @Expose
 private val message: String? = null

 @SerializedName("totalResults")
 private val totalResults = 0

 @SerializedName("articles")
 private val NewsDataResponseClass: ArrayList<NewsDataResponseClass>? = null*/

