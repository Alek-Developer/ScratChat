package com.gsixacademy.android.scratchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_sign_in.*


class SplashActivity : AppCompatActivity() {

    // TODO Step 5 - for Rotate text animation !!!
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // TODO Step 4 - for Rotate text animation !!!
        image = findViewById(R.id.nut_image)


        // TODO Step 2 - for Rotate text animation !!!
        animatenut_image()

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }

    // TODO Step 3 - for Rotate text animation !!!
    private fun animatenut_image() {
        val translate = AnimationUtils.loadAnimation(this, R.anim.image_animation_rotate)
        image.animation = translate
    }


}
