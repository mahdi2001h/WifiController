package utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient {

    public static final String SERVER_IP = "192.168.4.1"; //server IP address
    public static final int SERVER_PORT = 8888;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    public boolean run = false;
    public boolean connect = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    String TAG = "TcpClient";
    Socket socket;

    public TcpClient() {
    }

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {

                    mBufferOut.println(message + "\r\n");
                    mBufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        run = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        run = true;

        try {

            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.i("TCP Client", "C: Connecting...");


            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(SERVER_IP), SERVER_PORT), 1000);

            try {

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                //in this while the client listens for the messages sent by the server
                while (run) {
                    if (socket.isConnected() && !connect)
                        mMessageListener.messageReceived("", true);
                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage, true);
                    }

                }

                Log.e(TAG, "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.e(TAG, "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            mMessageListener.messageReceived("", false);
            Log.e(TAG, "C: Error", e);

        }


    }

    void checkStatus() {

    }

    public interface OnMessageReceived {
        public void messageReceived(String message, boolean state);

    }

}