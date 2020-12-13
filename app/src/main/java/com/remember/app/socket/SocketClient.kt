package com.remember.app.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket

class SocketClient {
    private val TAG = "SOCKET_CLIENT"
    private val socket: Socket
    private var events: ISignalingEvents

    constructor(url: String, token: String, events: ISignalingEvents) {
        val options = IO.Options()
        options.transports = arrayOf(WebSocket.NAME)
        //options.query = "token=my custom token"
        this.socket = IO.socket(url, options)
      //  this.mSocket = IO.socket(url)
        this.events = events

        socket.on(Socket.EVENT_CONNECT) {
            events.onConnect(token)
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            events.onDisconnect(it)
        }
//        mSocket.on("err") {
//            val json = it[0] as JSONObject
//            val error = json.optString("error")
//            Log.d(TAG, json.toString())
//            Log.d(TAG, error.toString())
//        }
//        mSocket.on("chat.new") {
//            val json = it[0] as JSONObject
//            val error = json.optString("error")
//            Log.d(TAG, json.toString())
//            Log.d(TAG, error.toString())
//        }
    }

    fun connect() {
        Log.d(TAG, "CONNECTING")
        socket.connect()
    }

    fun disconnect() {
        Log.d(TAG, "DISCONNECTING")
        socket.disconnect()
    }

    fun authorization(token: String) {
        socket.emit("auth", token)
    }

    interface ISignalingEvents {
        fun onConnect(token: String)
        fun onDisconnect(it : Array<Any>)
        fun onMessage(message: ChatMessage)
    }

    fun getSocket() = socket

}