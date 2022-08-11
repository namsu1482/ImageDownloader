package com.cns.imagedownloader.model.pixabayitem

import com.cns.imagedownloader.model.ImageItemInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PixabayImgItem(
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalHits")
    val totalHits: Int,
    @SerializedName("hits")
    val hitsEntity: List<HitsEntity>
)

data class HitsEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("pageURL")
    val pageURL: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("previewURL")
    val previewURL: String,
    @SerializedName("previewWidth")
    val previewWidth: Int,
    @SerializedName("previewHeight")
    val previewHeight: Int,
    @SerializedName("webformatURL")
    val webformatURL: String,
    @SerializedName("webformatWidth")
    val webformatWidth: Int,
    @SerializedName("webformatHeight")
    val webformatHeight: Int,
    @SerializedName("largeImageURL")
    val largeImageURL: String,
    @SerializedName("imageWidth")
    val imageWidth: Int,
    @SerializedName("imageHeight")
    val imageHeight: Int,
    @SerializedName("imageSize")
    val imageSize: Int,
    @SerializedName("views")
    val views: Int,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("collections")
    val collections: Int,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("user")
    val user: String,
    @SerializedName("userImageURL")
    val userImageURL: String


) : Serializable, ImageItemInfo {
    override var imgId: String
        get() = this.id.toString()
        set(value) {

        }

    override var smallUrl: String
        get() = this.previewURL
        set(value) {}

    override var mainUrl: String
        get() = this.largeImageURL
        set(value) {}

}


data class ListData<T>(var items: List<out T> = listOf(), val rowsCount: Int = 0)