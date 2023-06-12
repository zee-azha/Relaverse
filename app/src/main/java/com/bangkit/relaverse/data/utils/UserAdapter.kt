package com.bangkit.relaverse.data.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.relaverse.data.remote.response.User
import com.bangkit.relaverse.databinding.ItemListUserBinding


class UserAdapter : ListAdapter<User, UserAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(holder.itemView.context, data)
        }
    }

    class ListViewHolder(private val binding: ItemListUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, lisUser: User) {
            with(binding) {
                tvUser.text = lisUser.name
                root.setOnClickListener {

                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
