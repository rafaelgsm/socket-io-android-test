package rafaelgsm.socket_io_test

interface WebSocketUpdates {

    fun open(onOpen: () -> Unit)

    fun startListening(onUpdate: (text: String) -> Unit)

    fun stopListening()

    fun onError(onError: (throwable: Throwable) -> Unit)
}