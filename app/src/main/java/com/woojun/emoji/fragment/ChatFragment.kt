package com.woojun.emoji.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.emoji.databinding.FragmentChatBinding
import com.woojun.emoji.util.Chat
import com.woojun.emoji.util.ChatAdapter
import com.woojun.emoji.util.Diary
import com.woojun.emoji.util.DiaryPost
import com.woojun.emoji.util.EmotionViewModel
import com.woojun.emoji.util.GptSolution
import com.woojun.emoji.util.MainViewModel
import com.woojun.emoji.util.RetrofitAPI
import com.woojun.emoji.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val chatList = mutableListOf(Chat("안녕하세요.\n오늘의 감정을 확인 하시겠습니까?", true, true))
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModel2: EmotionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setupKeyboardVisibilityListener(a, cardView, 700)

            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            viewModel2 = ViewModelProvider(requireActivity())[EmotionViewModel::class.java]
            val emotion = viewModel2.getEmotion()

            val chatAdapter = ChatAdapter(chatList, requireActivity(), viewModel)
            chattingList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {}

            if (emotion != "") {
                chatAdapter.addChat(Chat("당신의 감정은 $emotion 입니다.", true))
                chatAdapter.addChat(Chat("당신이 느끼고 있는 감정이 맞나요?\n맞다면 채팅창에 ‘네’를 작성하시고,\n아니라면 당신의 감정을 작성해 주세요.", true))
                viewModel.onAllowChat()
            }

            chattingList.adapter = chatAdapter
            viewModel.isAllowChat.observe(requireActivity()) { boolean ->
                if (boolean) {
                    input.hint = "감정을 입력하세요."
                    buttonIcon1.visibility = View.VISIBLE
                    buttonIcon2.visibility = View.INVISIBLE

                    button.setOnClickListener {
                        viewModel.offAllowChat()
                        if (input.text.isNotEmpty()) {
                            val text = input.text.toString()
                            if (chatAdapter.getChat().size == 3) {
                                chatAdapter.addChat(Chat(text, false))

                                if (text == "네") {
                                    viewModel2.setEmotion(emotion)
                                } else {
                                    viewModel2.setEmotion(text)
                                }
                                chatAdapter.addChat(Chat("왜 그런 감정을 느끼셨나요?", true))
                                input.isEnabled = true
                                viewModel.onAllowChat()
                            } else {
                                chatAdapter.addChat(Chat(text, false))
                                val retrofit = RetrofitClient.getInstance()
                                val apiService = retrofit.create(RetrofitAPI::class.java)

                                apiService.postDiary(DiaryPost("0", viewModel2.getEmotion(), text)).enqueue(object :
                                    Callback<GptSolution> {
                                    override fun onResponse(call: Call<GptSolution>, response: Response<GptSolution>) {
                                        if (response.isSuccessful) {
                                            chatAdapter.addChat(Chat(response.body()!!.solution, true, false, true))
                                            viewModel2.updateData(Diary("", "${getCurrentDateString()}\n${emotion}",text, response.body()!!.solution))
                                            chatAdapter.addChat(Chat("마지막으로\n그림 일기를 작성해주세요!", true, false, true))
                                            input.isEnabled = false
                                            viewModel.offAllowChat()
                                        }
                                    }

                                    override fun onFailure(call: Call<GptSolution>, t: Throwable) {
                                        Toast.makeText(requireContext(), "다시 해주세요", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }
                        input.text.clear()
                    }
                }
                else {
                    input.hint = "AI가 작성 중입니다."
                    input.isEnabled = false
                    buttonIcon1.visibility = View.INVISIBLE
                    buttonIcon2.visibility = View.VISIBLE
                }
            }






        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getCurrentDateString(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return LocalDate.now().format(formatter)
    }

    fun setupKeyboardVisibilityListener(rootLayout: View, editText: View, marginSize: Int) {
        var originalMarginBottom = 0
        val layoutParams = editText.layoutParams as ViewGroup.MarginLayoutParams
        originalMarginBottom = layoutParams.bottomMargin

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (isVisible) {
                // 키보드가 올라왔을 때
                layoutParams.bottomMargin = originalMarginBottom + marginSize
            } else {
                // 키보드가 내려갔을 때
                layoutParams.bottomMargin = originalMarginBottom
            }
            editText.layoutParams = layoutParams
            insets
        }
    }
}