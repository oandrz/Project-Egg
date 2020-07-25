/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.ads

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_ads.*
import kotlinx.android.synthetic.main.content_recipe_detail_body.adView
import starbright.com.projectegg.BuildConfig
import starbright.com.projectegg.R

class AdsActivity : AppCompatActivity() {

    private val rewardedAds: RewardedAd by lazy {
        RewardedAd(this, BuildConfig.REWARD_ADS_ID)
    }
    private val adCallback = object: RewardedAdCallback() {
        override fun onRewardedAdOpened() {}
        override fun onRewardedAdClosed() {}
        override fun onUserEarnedReward(reward: RewardItem) {}
        override fun onRewardedAdFailedToShow(errorCode: Int) {}
    }

    private val intersitialAd: InterstitialAd by lazy  {
        InterstitialAd(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)
        adView.loadAd(AdRequest.Builder().build())
        rewardedAds.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onRewardedAdFailedToLoad(p0: Int) {
                super.onRewardedAdFailedToLoad(p0)
                progress.visibility = View.GONE
                btn_intersitial_ads.visibility = View.VISIBLE
                btn_ads_video.visibility = View.GONE
                Log.v("Rewards Ads", "Load Failed with error $p0")
            }

            override fun onRewardedAdLoaded() {
                super.onRewardedAdLoaded()
                progress.visibility = View.GONE
                btn_ads_video.visibility = View.VISIBLE
                btn_intersitial_ads.visibility = View.VISIBLE
                Log.v("Rewards Ads", "Load Success")
            }
        })

        btn_ads_video.setOnClickListener {
            if (rewardedAds.isLoaded) {
                rewardedAds.show(this, adCallback)
            }
        }

        intersitialAd.apply {
            adUnitId = BuildConfig.INTERSTITIAL_ADS_ID
            loadAd(AdRequest.Builder().build())
        }
        btn_intersitial_ads.setOnClickListener {
            if (intersitialAd.isLoaded) {
                intersitialAd.show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AdsActivity::class.java)
    }
}
