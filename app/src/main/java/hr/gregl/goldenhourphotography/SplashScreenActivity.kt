package hr.gregl.goldenhourphotography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.gregl.goldenhourphotography.api.TimeWorker
import hr.gregl.goldenhourphotography.databinding.ActivitySplashScreenBinding
import hr.gregl.goldenhourphotography.framework.applyAnimation
import hr.gregl.goldenhourphotography.framework.callDelayed
import hr.gregl.goldenhourphotography.framework.getBooleanPreference
import hr.gregl.goldenhourphotography.framework.isOnline
import hr.gregl.goldenhourphotography.framework.startActivity

private const val DELAY = 3000L

const val DATA_IMPORTED = "hr.gregl.goldenhourphotography.data_imported"

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
        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) {
                startActivity<HostActivity>()
            }
        } else {
            if (isOnline()) {

                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.from(TimeWorker::class.java)
                    )
                }


            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) {
                    finish()
                }
            }
        }
    }
}