package com.woojun.emoji.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.emoji.databinding.DiaryItemBinding

class DiaryAdapter(private val diaryList: List<Diary>): RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding).also { handler ->

        }
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(diaryList[position])
    }

    class DiaryViewHolder(private val binding: DiaryItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodInfo: Diary) {
            binding.apply {

            }
        }
    }

}