package com.room.beersearch

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object  BeerBindgAdapter {

    @BindingAdapter("imageUrl")
    @JvmStatic
     fun loadImage(view: ImageView, url: String?) {
        // Glide 사용 하기
        Glide.with(view.context)
            .load(url)
            .error(R.drawable.emptyimage)
            .into(view)
    }
}