package com.example.objectdetectiontest.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.example.objectdetectiontest.*
import com.example.objectdetectiontest.databinding.ActivityMainBinding
import com.example.objectdetectiontest.utils.ImageUtils
import com.example.objectdetectiontest.utils.Util
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    val workerThread: ExecutorService = Executors.newCachedThreadPool()
    val workerHandler = Handler(Looper.getMainLooper())

    private var mainBitmap: Bitmap? = null
    private var faceBitmap: Bitmap? = null

    var mCount = 0
    private var cutBit: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.processorBtn.setOnClickListener {

            mainBinding.cropProgressBar.visibility = View.VISIBLE

            workerThread.execute {

                StoreManager.setCurrentCropedBitmap(this, null)
                StoreManager.setCurrentCroppedMaskBitmap(this, null)

                mainBitmap = ContextCompat.getDrawable(this, R.drawable.n)?.let { it1 ->
                    Util.drawableToBitmap(
                        it1
                    )
                }

                if (mainBitmap != null) {
                    StoreManager.setCurrentOriginalBitmap(this, mainBitmap)
                    settingBitmap(mainBitmap!!)

                } else {
                    Util.showToast(this, "Bitmap is null")
                }
            }

        }

    }

    private fun settingBitmap(bitmap: Bitmap) {

        faceBitmap = ImageUtils.getBitmapResize(
            bitmap,
            mainBinding.objectImage.width,
            mainBinding.objectImage.height
        )

        workerHandler.post {
            mainBinding.rootLayout.layoutParams = ConstraintLayout.LayoutParams(
                faceBitmap!!.width,
                faceBitmap!!.height
            )

            cutmaskNew()
        }

    }

    private fun cutmaskNew() {

        mainBinding.cropProgressBar.visibility = View.GONE

        object : CountDownTimer(5000, 1000) {
            override fun onFinish() {}
            override fun onTick(j: Long) {
                mCount++
                if (mainBinding.cropProgressBar.progress <= 90) {
                    mainBinding.cropProgressBar.progress = mCount * 5
                }
            }
        }.start()

        MLCropAsyncTask({ bitmap, bitmap2, i, i2 ->

            val width: Int = faceBitmap!!.getWidth()
            val height: Int = faceBitmap!!.getHeight()
            val i3 = width * height
            faceBitmap!!.getPixels(IntArray(i3), 0, width, 0, 0, width, height)
            val iArr = IntArray(i3)
            val createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            createBitmap.setPixels(iArr, 0, width, 0, 0, width, height)
            cutBit = ImageUtils.getMask(faceBitmap, createBitmap, width, height)
            cutBit = Bitmap.createScaledBitmap(
                bitmap!!,
                cutBit!!.getWidth(),
                cutBit!!.getHeight(),
                false
            )
            runOnUiThread(Runnable {
                if (Palette.from(cutBit!!).generate().dominantSwatch == null) {
                    Toast.makeText(
                        this,
                        " this@MainActivity.getString(R.string.txt_not_detect_human)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mainBinding.objectImage.setImageBitmap(cutBit)
            })
        }, this, mainBinding.cropProgressBar).execute()

    }
}