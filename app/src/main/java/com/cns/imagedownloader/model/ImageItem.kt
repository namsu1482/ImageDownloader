package com.cns.imagedownloader.model

import java.io.Serializable

interface ImageItemInfo : Serializable {
    var imgId: String
    var smallUrl: String
    var mainUrl: String
}