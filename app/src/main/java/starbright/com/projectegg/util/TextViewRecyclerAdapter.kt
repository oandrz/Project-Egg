/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 19/8/2018.
 */

/**
 * Created by Andreas on 19/8/2018.
 */

/**
 * Created by Andreas on 19/8/2018.
 */

package starbright.com.projectegg.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import starbright.com.projectegg.databinding.ItemTextOnlyBinding

class TextViewRecyclerAdapter(private val mDataSource: List<String>) : RecyclerView.Adapter<TextViewRecyclerAdapter.ViewHolder>() {

    private var binding: ItemTextOnlyBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTextOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(mDataSource[position])
    }

    override fun getItemCount(): Int {
        return mDataSource.size
    }

    inner class ViewHolder(private val binding: ItemTextOnlyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(text: String) {
            binding.tvText.text = text
        }
    }
}
