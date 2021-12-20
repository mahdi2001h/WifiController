package ir.mahdi_hamzeh.smartHome

interface SerialListener {
    fun onSerialRead(text: String)
}