package ajou.paran.entrip.screen.trip

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityTriptestBinding
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripTestActivity: BaseActivity<ActivityTriptestBinding>(R.layout.activity_triptest) {
    companion object{
        const val TAG = "[TripTestActivity]"
        const val LOAD_URL = "http://2ntrip.com/traveltest.html?user_id="
    }

    private val viewModel: TripTestActivityViewModel by viewModels()
    private lateinit var webView: WebView

    override fun init(savedInstanceState: Bundle?) {
        webView = binding.tripActWebview
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("$LOAD_URL${viewModel.getUserId()!!}")
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = true

        webView.setOnKeyListener { view, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    webView.let {
                        if(webView.canGoBack()){
                            webView.goBack()
                        } else {
                            this.onBackPressed()
                        }
                    }
                }
            }
            true
        }
    }
}