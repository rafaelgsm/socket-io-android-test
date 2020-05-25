package rafaelgsm.socket_io_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {

    private val CHAT = "chat message"

    private var mSocket: Socket? = null

    init {
        try {
            mSocket = IO.socket("http://10.0.2.2:8080")
        } catch (e: URISyntaxException) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSocket?.on(CHAT, onNewMessage);
        mSocket?.connect();


        btn_send?.setOnClickListener {
            val message = edt_input?.text.toString()

            if (message.isEmpty()) {
                return@setOnClickListener
            }

            edt_input?.setText("")
            mSocket?.emit(CHAT, message)

        }
    }

    //region listener
    private var onNewMessage: Emitter.Listener? = Emitter.Listener { args ->
        runOnUiThread {

            tv_output?.text = tv_output?.text.toString() + "\n" + "${args[0]}"
        }
    }
    //endregion listener

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
        mSocket?.off(CHAT, onNewMessage)
    }
}
