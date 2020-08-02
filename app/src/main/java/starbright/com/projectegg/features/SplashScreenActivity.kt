/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import starbright.com.projectegg.R
import starbright.com.projectegg.features.home.HomeActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(
            {
                startActivity(HomeActivity.newIntent(this))
                finish()
            },
            1000
        )
    }
}
