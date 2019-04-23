package com.suleiman.pagination.utils

import android.content.Context

import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * Pagination
 * Created by Suleiman19 on 2/16/19.
 * Copyright (c) 2019. Suleiman Ali Shakir. All rights reserved.
 */
@GlideModule
class PaginationGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions(
                RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // cache both original & resized image
        )
    }
}
