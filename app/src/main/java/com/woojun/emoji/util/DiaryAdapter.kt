package com.woojun.emoji.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.emoji.R
import com.woojun.emoji.databinding.DiaryItemBinding

class DiaryAdapter(private val diaryList: List<Diary>, private val context: Context): RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding).also { handler ->
            binding.apply {
                diaryButton.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("diary", diaryList[handler.position])
                    parent.findNavController().navigate(R.id.innerFragment, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(diaryList[position], context)
    }

    class DiaryViewHolder(private val binding: DiaryItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(diary: Diary, context: Context) {
            binding.apply {
                title.text = diary.title
                imageView.setImageBitmap(stringToBitmap(diary.image))
            }
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


}