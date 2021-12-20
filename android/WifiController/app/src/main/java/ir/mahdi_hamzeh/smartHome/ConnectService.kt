package ir.mahdi_hamzeh.smartHome

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*
import android.os.CountDownTimer
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import utils.TcpClient
import android.os.AsyncTask
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class ConnectService : Service() {
    // Binder given to clients
    private val binder = LocalBinder()
    var isConnecting = false
    private lateinit var tcpClient: TcpClient
    var isConnect = false
    lateinit var listener: SerialListener
    private val mGenerator = Random()
    val TAG = "ConnectService"

    /** method for clients  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    var number = 0

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {

        // Return this instance of LocalService so clients can call public methods
        fun getService(): ConnectService = this@ConnectService
    }

    fun connect() {
        ConnectTask().execute("")
        isConnecting = true
    }


    fun setMyListener(listener: SerialListener) {
        this.listener = listener
    }

    fun send(text: String) {
        tcpClient?.sendMessage(text)
    }


    override fun onBind(intent: Intent): IBinder {

        //    Toast.makeText(applicationContext, "number: $num", Toast.LENGTH_SHORT).show()


        return binder
    }

    inner class ConnectTask : AsyncTask<String, String, TcpClient>() {

        override fun doInBackground(vararg message: String): TcpClient? {

            //here the messageReceived method is implemented
            tcpClient =
                TcpClient(TcpClient.OnMessageReceived { message, state ->
                    isConnect = state
                    Log.d(TAG, "Received: $message")
                    if (::listener.isInitialized)
                        listener.onSerialRead(message)
                })

            tcpClient?.run()

            return null
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)


        }
    }
}
