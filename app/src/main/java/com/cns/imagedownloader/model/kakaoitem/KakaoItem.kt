package com.cns.imagedownloader.model.kakaoitem

import com.cns.imagedownloader.model.ImageItemInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KakaoItem(
    @SerializedName("collection")
    val collection: String,
    @SerializedName("datetime")
    val dateTime: String,
    @SerializedName("display_sitename")
    val displaySiteName: String,
    @SerializedName("doc_url")
    val docUrl: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("width")
    val width: Int

) : Serializable, ImageItemInfo {
    override var imgId: String
        get() = this.imageUrl
        set(value) {}
    override var smallUrl: String
        get() = this.thumbnailUrl
        set(value) {}
    override var mainUrl: String
        get() = this.imageUrl
        set(value) {}
}

data class KakaoMetaData(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCnt: Int,
    @SerializedName("total_count")
    val totalCnt: Int

)

data class KakaoImgList(
    @SerializedName("documents")
    val kakaoItemList: List<KakaoItem>,
    @SerializedName("meta")
    val metaData: KakaoMetaData

)