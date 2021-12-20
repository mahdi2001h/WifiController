package ir.mahdi_hamzeh.smartHome

import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.content.Intent
import android.content.ServiceConnection
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.IBinder
import android.view.View
import android.view.View.*
import android.widget.Toast


class SplashActivity : AppCompatActivity() {

    var TAG = "SplashActivity"
    lateinit var mService: ConnectService
    var mBound: Boolean = false
    var isChecking: Boolean = false
    var connectFragment: ConnectFragment = ConnectFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        supportFragmentManager.beginTransaction().add(R.id.main_frame, IntroFragment()).commit()


        Handler().postDelayed({
            kotlin.run {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, connectFragment)
                    .commit()

            }
        }, 1000)
    }

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as ConnectService.LocalBinder
            mService = binder.getService()
            mBound = true
            connect()

        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, ConnectService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    fun connect() {
        if (!isChecking) {
            isChecking = true

            mService.connect()
            Handler().postDelayed({
                kotlin.run {
                    isChecking = false
                    if (mService.isConnect) {
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))

                    } else {
                        connectFragment.btnAgain.visibility = VISIBLE
                        connectFragment.wifi.loop(false)

                    }
                }
            }, 1200)
        }

    }

    interface onStatusChange {
        fun statusChange(state: Boolean)

    }
}
