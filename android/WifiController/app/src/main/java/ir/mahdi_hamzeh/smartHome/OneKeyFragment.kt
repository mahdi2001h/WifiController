package ir.mahdi_hamzeh.smartHome

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import utils.TcpClient
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView


class OneKeyFragment : Fragment() {

    private lateinit var btnToggle: Button
    private lateinit var light: LottieAnimationView
    private lateinit var root: View

    var TAG = "OneKeyFragment"
    var status = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_one_key, container, false)
        init()

        Handler().postDelayed({
            kotlin.run {
                (activity as MainActivity).mService.setMyListener(object : SerialListener {
                    override fun onSerialRead(text: String) {
                        Log.d(TAG, "Received: $text")
                        val items = text.split(",")
                        for (item in items) {
                            val field = item.split(":")
                            activity!!.runOnUiThread {
                                when (field[0]) {
                                    "state" -> {
                                        if (field[1] == "1")
                                            setState(true)
                                        else if (field[1] == "0")
                                            setState(false)

                                    }
                                }
                            }
                        }
                    }
                })
            }
        }, 100)

        btnToggle.setOnClickListener {
            status = !status
            if (status)
                send("state:1")
            else
                send("state:0")

        }

        return root
    }

    private fun setState(state: Boolean) {
        status = state
        if (state) {
            btnToggle.text = "خاموش"
            light.progress = 1f
        } else {
            btnToggle.text = "روشن"
            light.progress = 0f
        }
    }

    private fun init() {
        btnToggle = root.findViewById(R.id.btn_toggle)
        light = root.findViewById(R.id.lot_wifi)


    }

    private fun send(text: String) {
        (activity as MainActivity).mService.send(text)
        val v: Vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(50)

    }


}