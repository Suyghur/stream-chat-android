package io.getstream.chat.android.ui.pinned.list.internal

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.chat.android.ui.common.extensions.internal.asMention
import io.getstream.chat.android.ui.common.extensions.internal.context
import io.getstream.chat.android.ui.common.extensions.internal.streamThemeInflater
import io.getstream.chat.android.ui.databinding.StreamUiItemMentionListBinding
import io.getstream.chat.android.ui.message.preview.MessagePreviewStyle
import io.getstream.chat.android.ui.pinned.list.PinnedMessageListView.PinnedMessageSelectedListener
import io.getstream.chat.android.ui.pinned.list.internal.PinnedMessageListAdapter.MessagePreviewViewHolder

internal class PinnedMessageListAdapter(
    private val chatDomain: ChatDomain,
) : ListAdapter<Message, MessagePreviewViewHolder>(MessageDiffCallback) {

    private var pinnedMessageSelectedListener: PinnedMessageSelectedListener? = null

    var messagePreviewStyle: MessagePreviewStyle? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagePreviewViewHolder {
        return StreamUiItemMentionListBinding
            .inflate(parent.streamThemeInflater, parent, false)
            .let { binding ->
                messagePreviewStyle?.let(binding.root::styleView)
                MessagePreviewViewHolder(binding)
            }
    }

    override fun onBindViewHolder(holder: MessagePreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setPinnedMessageSelectedListener(pinnedMessageSelectedListener: PinnedMessageSelectedListener?) {
        this.pinnedMessageSelectedListener = pinnedMessageSelectedListener
    }

    inner class MessagePreviewViewHolder(
        private val binding: StreamUiItemMentionListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var message: Message

        init {
            binding.root.setOnClickListener {
                pinnedMessageSelectedListener?.onPinnedMessageSelected(message)
            }
        }

        internal fun bind(message: Message) {
            this.message = message
            binding.root.setMessage(message, chatDomain.user.value?.asMention(context))
        }
    }

    private object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            // Comparing only properties used by the ViewHolder
            return oldItem.id == newItem.id &&
                oldItem.createdAt == newItem.createdAt &&
                oldItem.createdLocallyAt == newItem.createdLocallyAt &&
                oldItem.text == newItem.text &&
                oldItem.user == newItem.user
        }
    }
}
