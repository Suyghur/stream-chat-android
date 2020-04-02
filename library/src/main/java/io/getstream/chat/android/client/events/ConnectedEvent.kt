package io.getstream.chat.android.client.events

import com.google.gson.annotations.SerializedName
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.Reaction
import io.getstream.chat.android.client.models.User

class ConnectedEvent : ChatEvent() {

    lateinit var me: User
    var online = false

    @SerializedName("connection_id")
    var connectionId: String = ""
    @SerializedName("client_id")
    var clientId: String = ""
    @SerializedName("watcher_count")
    val watcherCount: Number = 0
    @SerializedName("clear_history")
    var clearHistory: Boolean = false

    val isChannelEvent: Boolean
        get() = cid != "*"

    val isAnonymous: Boolean
        get() = if (me != null) {
            me.id == "!anon"
        } else true
}