package ajou.paran.entrip.screen.community

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentCommunityBinding
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community){
    companion object{
        const val TAG = "[CommunityFragment]"
//        const val LOAD_URL = "http://2ntrip.com/traveltest.html?user_id="
        const val LOAD_URL = "file:///android_asset/index_free.html"
    }

    private val viewModel: CommunityFragmentViewModel by viewModels()

    private lateinit var webView: WebView
    private lateinit var user_id: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        user_id = viewModel.getUserId()!!
        webView = binding.communityFragWeVbiew
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
//        webView.loadUrl("$LOAD_URL${viewModel.getUserId()!!}")
        webView.loadUrl(LOAD_URL)
        webView.setOnKeyListener { view, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    webView.let {
                        if(webView.canGoBack()){
                            webView.goBack()
                        } else {
                            activity!!.onBackPressed()
                        }
                    }
                }
            }
            true
        }
        webView.addJavascriptInterface(CommunityWebInterface(context!!), "Entrip")
    }

    inner class CommunityWebInterface constructor(private val mContext: Context){

        @JavascriptInterface
        fun getUserId(): String{
            Log.d(TAG,"$user_id 호출되었습니다.")
            return user_id
        }

        @JavascriptInterface
        fun showToast(message: String) {
            Log.d(TAG, message)
        }
    }
}