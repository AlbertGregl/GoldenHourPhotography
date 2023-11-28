package hr.gregl.goldenhourphotography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import hr.gregl.goldenhourphotography.databinding.ActivitySplashScreenBinding
import hr.gregl.goldenhourphotography.framework.applyAnimation
import hr.gregl.goldenhourphotography.framework.startActivity

private const val DELAY = 3000L

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.applyAnimation(R.anim.rotate)
        binding.tvSplash.applyAnimation(R.anim.blink)
    }

    private fun redirect() {



        Handler(Looper.getMainLooper()).postDelayed(
            { startActivity<HostActivity>() },
            DELAY
        )
    }
}