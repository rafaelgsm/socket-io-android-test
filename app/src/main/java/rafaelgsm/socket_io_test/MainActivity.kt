package rafaelgsm.socket_io_test

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Implementation is only for LISTENING (Not sending)
 * Thus EditText is not used anymore.
 */
open class MainActivity : AppCompatActivity() {

    private val webSocketUpdates: WebSocketUpdates by lazy { AdonisWebSocketUpdates() }
//    private val webSocketUpdates: WebSocketUpdates by lazy { SocketIOWebSocketUpdates() }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webSocketUpdates.open {
            webSocketUpdates.startListening {
                runOnUiThread {
                    tv_output?.text = tv_output?.text.toString() + "\n" + it
                }
            }

            webSocketUpdates.onError {
                tv_output?.text = it.message
            }
        }

        btn_send?.setOnClickListener {
            val message = edt_input?.text.toString()

            if (message.isEmpty()) {
                return@setOnClickListener
            }

            edt_input?.setText("")
//            webSocket.send(message)

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        webSocketUpdates.stopListening()
    }
}
