package starbright.com.projectegg.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import starbright.com.projectegg.R
import starbright.com.projectegg.features.ingredients.IngredientsActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(
            {
                startActivity(IngredientsActivity.newIntent(this))
            },
            1000
        )
    }
}
