/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 27/8/2018.
 */

package starbright.com.projectegg.features.base

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import starbright.com.projectegg.R

class WebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        setupToolbar()
        setupWebView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
//        setSupportActionBar(toolbar)
//        supportActionBar?.run {
//            title = intent.getStringExtra(EXTRA_URL)
//            setDisplayHomeAsUpEnabled(true)
//            setHomeButtonEnabled(true)
//        }
    }

    private fun setupWebView() {
//        val webSettings = webview.settings
//        webSettings.javaScriptEnabled = true
//        webSettings.domStorageEnabled = true
//        webview.webViewClient = object : WebViewClient() {
//            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
//                progress_bar.visibility = View.VISIBLE
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                progress_bar.visibility = View.GONE
//            }
//        }
//        webview.overScrollMode = WebView.OVER_SCROLL_NEVER
//        webview.loadUrl(intent.extras!!.getString(EXTRA_URL))
    }

    companion object {

        val EXTRA_URL = "EXTRA_URL"

        fun newIntent(context: Context, url: String): Intent {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }
}
