package com.example.objectdetectiontest

import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import java.io.IOException
import java.io.InputStream

object Util {

    fun initNative()
    {
        NativeLibraryLoader.load()
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width: Int = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height: Int = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    fun showToast(c: Context, message: String) {
        try {
            if (!(c as Activity).isFinishing) {
                c.runOnUiThread { //show your Toast here..
                    Toast.makeText(c.applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}