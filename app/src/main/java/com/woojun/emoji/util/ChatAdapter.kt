package com.woojun.emoji.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.emoji.R
import com.woojun.emoji.databinding.ChatItemBinding

class ChatAdapter(private val chatList: MutableList<Chat>, private val lifecycleOwner: LifecycleOwner, private val viewModel: MainViewModel): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var diary: Diary? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding).also { handler ->
            binding.apply {
                viewModel.isPhotoTaken.observe(lifecycleOwner) { boolean ->
                    aiChat.setOnClickListener {
                        if (!boolean) {
                            parent.findNavController().navigate(R.id.cameraFragment)
                        }
                        if (chatList[chatList.size - 1].isOnButton) {
                            parent.findNavController().navigate(R.id.diaryFragment)
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position], chatList)
    }

    class ChatViewHolder(private val binding: ChatItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat, chatList: List<Chat>) {
            binding.apply {
                if (chat.isAi) {
                    myChat.visibility = View.GONE
                    aiChat.visibility = View.VISIBLE

                    aiChatText.text = chat.text

                    if (chatList.size == 1) {
                        cameraButton.visibility = View.VISIBLE
                    } else if (chat.text == "마지막으로\n그림 일기를 작성해주세요!") {
                        cameraButton.visibility = View.VISIBLE
                        buttonText.text = "그림일기 작성"
                    }
                } else {
                    myChat.visibility = View.VISIBLE
                    aiChat.visibility = View.GONE

                    myChatText.text = chat.text
                }
            }
        }
    }

    fun addChat(chat: Chat) {
        chatList.add(chat)
        notifyDataSetChanged()
    }

    fun getChat(): List<Chat> {
        return chatList
    }



}