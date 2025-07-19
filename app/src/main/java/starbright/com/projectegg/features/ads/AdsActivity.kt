/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */

package starbright.com.projectegg.features.ads

import starbright.com.projectegg.databinding.ActivityAdsBinding
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
// TODO: Update to new Google Ads SDK
// import com.google.android.gms.ads.interstitial.InterstitialAd
// import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
// import com.google.android.gms.ads.rewarded.RewardedAd
// import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import starbright.com.projectegg.BuildConfig
import starbright.com.projectegg.R

class AdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdsBinding


    // TODO: Update to new Google Ads SDK implementation
    // private var rewardedAd: RewardedAd? = null
    // private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Load banner ad
        binding.adView.loadAd(AdRequest.Builder().build())
        
        // TODO: Update to new Google Ads SDK implementation
        // Hide buttons until ads implementation is updated
        binding.progress.visibility = View.GONE
        binding.btnAdsVideo.visibility = View.GONE
        binding.btnIntersitialAds.visibility = View.GONE
        
        /* TODO: Implement with new SDK
        // Load Rewarded Ad
        RewardedAd.load(this, BuildConfig.REWARD_ADS_ID, 
            AdRequest.Builder().build(), 
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    // Show button
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle error
                }
            })
            
        // Load Interstitial Ad  
        InterstitialAd.load(this, BuildConfig.INTERSTITIAL_ADS_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    // Show button
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle error
                }
            })
        */
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AdsActivity::class.java)
    }
}
