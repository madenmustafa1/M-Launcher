package com.maden.mlauncher.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.maden.mlauncher.R
import com.maden.mlauncher.databinding.ActivitySettingsBinding
import com.maden.mlauncher.util.setUserWallpaper

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setView()
    }

    private fun setView() {
        binding.changeWallpaper.setOnClickListener { pickImage() }
        binding.backButton.setOnClickListener { finish() }
        binding.setAnimation.setOnClickListener {
            Toast.makeText(
                this,
                "Coming soon!",
                Toast.LENGTH_LONG
            ).show()
        }
        transparentStatusBar()
    }

    private fun transparentStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
    }

    private fun pickImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("scale", true)
        intent.putExtra("aspectX", 16)
        intent.putExtra("aspectY", 9)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    val imageBitmap = uriToBitmap(uri)

                    try {
                        if (applicationContext.setUserWallpaper(imageBitmap)) {
                            Toast.makeText(
                                this,
                                getString(R.string.error),
                                Toast.LENGTH_LONG
                            ).show()
                            return@registerForActivityResult
                        }

                        Toast.makeText(
                            this,
                            getString(R.string.success),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            getString(R.string.unknown_exception),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }
}