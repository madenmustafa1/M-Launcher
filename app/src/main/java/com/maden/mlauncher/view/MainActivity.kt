package com.maden.mlauncher.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.maden.mlauncher.R
import com.maden.mlauncher.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_EXTERNAL_STORAGE = 5

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        requestPermissions()
        hideActionBar()
        loadFragment(MainPageFragment())

    }

    private fun hideActionBar(){
        try {
            supportActionBar?.hide();
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } catch (e: Exception) {

        }
    }


    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            return true
        }
        return false
    }


    override fun onBackPressed() {
        loadFragment(MainPageFragment())
    }

    //Get System wallpaper
    @SuppressLint("MissingPermission")
    private fun retrieveLockScreenWallpaper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(this);
            val wallpaperDrawable: Drawable = wallpaperManager.drawable;
            binding.mainActivtyLaout.background = wallpaperDrawable
        }
    }

    private fun requestPermissions(){
        var permission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
        permission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_CODE_EXTERNAL_STORAGE
            )
        } else {
            retrieveLockScreenWallpaper()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveLockScreenWallpaper()
                }
            }
        }
    }
}