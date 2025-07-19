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

import starbright.com.projectegg.databinding.ItemTextOnlyBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import starbright.com.projectegg.R

class TextViewRecyclerAdapter(private val mContext: Context, private val mDataSource: List<String>) : RecyclerView.Adapter<TextViewRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTextOnlyBinding.inflate(
            LayoutInflater.from(mContext), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvText.text = mDataSource[position]
    }

    override fun getItemCount(): Int {
        return mDataSource.size
    }

    inner class ViewHolder(val binding: ItemTextOnlyBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
