package rafaelgsm.socket_io_test

import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class AdonisWebSocketUpdates : WebSocketUpdates {

    companion object {
        private const val TAG = "AdonisWebSocketUpdates"
        private const val CLOSE_STATUS = 1000

        private const val TOPIC = "new message"
        private const val URL = "http://10.0.2.2:8080"
    }

    private var webSocket: WebSocket? = null

    var onUpdate: (text: String) -> Unit = {}
    var onError: (throwable: Throwable) -> Unit = {}


    //region public methods

    override fun open(onOpen: () -> Unit) {

        val builder = OkHttpClient.Builder()
        val client = builder.build()
        val request = Request.Builder()
            .url(URL)
//            .url("http://10.0.2.2:8080")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {

                onOpen()
                Log.d(TAG, "Connection accepted!")
            }


            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Receiving : $text")

                onUpdate(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Receiving bytes : " + bytes.hex())

                onUpdate(bytes.hex())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                stopListening()
                Log.d(TAG, "Closing : $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {

                onError(t)

                Log.d(TAG, "Error : " + t.message)
            }

        })

    }

    override fun startListening(onUpdate: (text: String) -> Unit) {
        this.onUpdate = onUpdate
        webSocket?.send(getSubscribeObject())
    }

    override fun stopListening() {
        webSocket?.close(CLOSE_STATUS, null)
        webSocket = null

        onError = {}
        onUpdate = {}
    }

    override fun onError(onError: (throwable: Throwable) -> Unit) {
        this.onError = onError
    }

    //endregion public methods

    //...
    private fun getSubscribeObject(): String {
        val subscribeObject = JSONObject()
        val topics = JSONObject()
        topics.put("topic", TOPIC)
        subscribeObject.put("t", 1)
        subscribeObject.put("d", topics)

        return subscribeObject.toString()
    }
}