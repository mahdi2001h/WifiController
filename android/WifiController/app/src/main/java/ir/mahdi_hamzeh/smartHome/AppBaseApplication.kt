package ir.mahdi_hamzeh.smartHome

import `in`.myinnos.customfontlibrary.TypefaceUtil
import android.app.Application

class AppBaseApplication:Application(){

    override fun onCreate() {
        super.onCreate()
        TypefaceUtil.overrideFont(applicationContext,"SERIF", "fonts/IRANSansMobile.ttf")
    }
}