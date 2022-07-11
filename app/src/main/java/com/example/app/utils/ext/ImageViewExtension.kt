package com.example.app.utils.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


private fun ImageView.loadImageInternal(url: String, onFailed: (() -> Unit)? = null) {
    try {
        Glide.with(this).load(url).listener(object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                onFailed?.invoke(); return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }).centerCrop().into(this)
    } catch (e: Exception) { e.printStackTrace() }
}

fun ImageView.loadImage(url: String?) {
    if(url.isNullOrBlank()) { return } else { this.loadImageInternal(url = url) }
}

fun ImageView.loadImageOrHide(url: String?, relatedView: View? = null) {
    this.isGone = url.isNullOrBlank()
    relatedView?.isGone = url.isNullOrBlank()

    if(!url.isNullOrBlank()) {
        this.loadImageInternal(url = url, onFailed = {
            this.isGone = true; relatedView?.isGone = true
        })
    }
}