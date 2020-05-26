package rafaelgsm.socket_io_test

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketIOWebSocketUpdates : WebSocketUpdates {

    companion object {

        private val CHAT = "chat message"
    }

    private var mSocket: Socket? = null

    private var onUpdate: (text: String) -> Unit = {}
    private var onError: (throwable: Throwable) -> Unit = {}

    init {
        try {
            mSocket = IO.socket("http://10.0.2.2:8080")
        } catch (e: URISyntaxException) {
        }
    }

    //region public methods
    override fun open(onOpen: () -> Unit) {
        mSocket?.connect()

        onOpen()
    }

    override fun startListening(onUpdate: (text: String) -> Unit) {
        this.onUpdate = onUpdate
        mSocket?.on(CHAT, onNewMessage)
    }

    override fun stopListening() {

        this.onUpdate = {}

        mSocket?.disconnect()
        mSocket?.off(CHAT, onNewMessage)
    }

    override fun onError(onError: (throwable: Throwable) -> Unit) {
        this.onError = onError
    }

    //endregion public methods

    //region listener
    private var onNewMessage: Emitter.Listener? = Emitter.Listener { args ->

        onUpdate(args[0].toString())
    }
    //endregion listener
}