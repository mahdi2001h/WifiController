package ir.mahdi_hamzeh.smartHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ui.home.HomeViewModel
import android.os.Handler
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView


class ConnectFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel

    var mHandler: Handler? = null
    var continue_or_stop: Boolean = false
    lateinit var root: View
    lateinit var btnAgain: Button
    lateinit var wifi: LottieAnimationView
    companion object {
        const val TAG = "ConnectFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_connect, container, false)

        init()


        btnAgain.setOnClickListener {
            (activity as SplashActivity).connect()
            btnAgain.visibility = View.INVISIBLE
            wifi.loop(true)
            wifi.playAnimation()
        }
        return root
    }

    private fun init() {
        btnAgain = root.findViewById(R.id.btn_again)
        wifi = root.findViewById(R.id.lot_wifi)
    }


}