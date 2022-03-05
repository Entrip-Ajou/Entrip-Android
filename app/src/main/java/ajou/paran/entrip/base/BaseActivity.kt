package ajou.paran.entrip.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>(
    @LayoutRes private val layoutResId: Int,
    private val bindingFactory: (LayoutInflater) -> T
): AppCompatActivity(layoutResId) {
    private var mBinding: T? = null
    protected val binding
        get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    abstract fun init()
}