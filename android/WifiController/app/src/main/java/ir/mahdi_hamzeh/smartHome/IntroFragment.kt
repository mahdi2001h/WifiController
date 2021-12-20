package ir.mahdi_hamzeh.smartHome

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ui.home.HomeViewModel

class IntroFragment : Fragment() {

    var TAG="IntroFragment"
    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         root = inflater.inflate(R.layout.fragment_intro, container, false)
        init()


        return root
    }

    private fun init() {

    }
}