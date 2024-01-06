package com.woojun.emoji.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.woojun.emoji.R
import com.woojun.emoji.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val text = helloText.text
            val spannableString = SpannableString(text)
            val color = ContextCompat.getColor(requireContext(), R.color.primary)

            val endIndex = text.indexOf("님")
            if (endIndex != -1) {
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            helloText.text = spannableString

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}