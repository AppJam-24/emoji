package com.woojun.emoji.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woojun.emoji.databinding.FragmentInnerBinding
import com.woojun.emoji.util.Diary
import com.woojun.emoji.util.EmotionViewModel

class InnerFragment : Fragment() {
    private var _binding: FragmentInnerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel2: EmotionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val diary = arguments?.getParcelable<Diary>("diary")!!

            val text = diary.title.split("\n")

            date.text = text[0]
            emotion.text = text[1]

            imageView.setImageBitmap(stringToBitmap(diary.image))


            emotionHistory.text = diary.context
            answer.text = diary.solution

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun stringToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}