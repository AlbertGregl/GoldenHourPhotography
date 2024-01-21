package hr.gregl.goldenhourphotography

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import hr.gregl.goldenhourphotography.util.LocationUtils

    private const val DELAY = 3000L

    const val DATA_IMPORTED = "hr.gregl.goldenhourphotography.data_imported"

    class SplashScreenActivity : AppCompatActivity() {
        private lateinit var binding: ActivitySplashScreenBinding
        private var isLocationSetupComplete = false
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySplashScreenBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupLocationRequirements()
        }

        private fun setupLocationRequirements() {
            if (!LocationUtils.isLocationEnabled(this)) {
                LocationUtils.promptUserToEnableLocationIfNeeded(this)
            } else if (!LocationUtils.hasLocationPermission(this)) {
                LocationUtils.checkLocationPermission(this)
            } else {
                isLocationSetupComplete = true
                proceedWithAppFlow()
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            LocationUtils.onRequestPermissionsResult(requestCode, grantResults, this)

            if (LocationUtils.isPermissionGranted(requestCode, grantResults)) {
                isLocationSetupComplete = true
                proceedWithAppFlow()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Using default location.",
                    Toast.LENGTH_SHORT
                ).show()
                isLocationSetupComplete = true
                proceedWithAppFlow()
            }
        }

        private fun proceedWithAppFlow() {
            if (isLocationSetupComplete) {
                startAnimations()
                redirect()
            }
        }

        private fun startAnimations() {
            binding.ivSplash.applyAnimation(R.anim.scale)
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