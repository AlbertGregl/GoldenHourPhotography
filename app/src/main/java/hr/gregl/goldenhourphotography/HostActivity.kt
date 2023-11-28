package hr.gregl.goldenhourphotography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.gregl.goldenhourphotography.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}