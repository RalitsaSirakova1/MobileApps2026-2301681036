package com.example.fitplan2301681036

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitplan2301681036.util.FullscreenUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeActivity : AppCompatActivity() {

    private lateinit var tvQrTitle: TextView
    private lateinit var ivQrCode: ImageView
    private lateinit var tvQrContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FullscreenUtils.enableFullscreen(this)
        setContentView(R.layout.activity_qrcode)

        bindViews()
        showQrCode()
    }

    private fun bindViews() {
        tvQrTitle = findViewById(R.id.tvQrTitle)
        ivQrCode = findViewById(R.id.ivQrCode)
        tvQrContent = findViewById(R.id.tvQrContent)
    }

    private fun showQrCode() {
        val workoutName = intent.getStringExtra(EXTRA_WORKOUT_NAME) ?: "Workout"
        val workoutContent = intent.getStringExtra(EXTRA_WORKOUT_CONTENT) ?: "No workout data"

        tvQrTitle.text = workoutName
        tvQrContent.text = workoutContent

        val qrBitmap = generateQrBitmap(workoutContent)
        ivQrCode.setImageBitmap(qrBitmap)
    }

    private fun generateQrBitmap(content: String): Bitmap {
        val size = 800
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bits[x, y]) Color.BLACK else Color.WHITE
                )
            }
        }

        return bitmap
    }

    companion object {
        const val EXTRA_WORKOUT_NAME = "extra_workout_name"
        const val EXTRA_WORKOUT_CONTENT = "extra_workout_content"
    }
}