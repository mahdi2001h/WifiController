package ir.mahdi_hamzeh.smartHome


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class NodeSettingFragment : Fragment() {

    var TAG="NodeSettingFragment"
    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_setting_node, container, false)
        init()


        return root
    }

    private fun init() {

    }
}