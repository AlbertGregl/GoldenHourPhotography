package hr.gregl.goldenhourphotography

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.gregl.goldenhourphotography.framework.setBooleanPreference
import hr.gregl.goldenhourphotography.framework.startActivity

class TimeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()

    }
}