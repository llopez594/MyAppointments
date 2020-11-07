package Utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Yosua_Setiawan on 28/07/2017.
 */

object ImageUtil {
    fun loadImageWithGlide(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).placeholder(android.R.color.darker_gray).into(imageView)
    }

    fun loadImageWithGlide(context: Context, drawable: Int, imageView: ImageView) {
        Glide.with(context).load(drawable).apply(RequestOptions.circleCropTransform()).into(imageView)
    }
}
