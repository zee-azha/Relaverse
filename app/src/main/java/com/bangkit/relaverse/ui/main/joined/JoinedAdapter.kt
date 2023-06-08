package com.bangkit.relaverse.ui.main.joined

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.relaverse.data.remote.response.EventList
import com.bangkit.relaverse.databinding.ItemListCampaignBinding
import com.bumptech.glide.Glide

class JoinedAdapter : ListAdapter<EventList, JoinedAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: JoinedAdapter.OnItemClickCallback

    inner class ViewHolder(private val binding: ItemListCampaignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(eventList: EventList) {
            binding.apply {
                Glide.with(root.context)
                    .load(eventList.campaignPhoto)
                    .into(imageView)

                tvTitle.text = eventList.title
//                tvLocation.text = campaignData.location
                tvDate.text = eventList.date

                root.setOnClickListener {
//                    onItemClickCallback.onItemClicked(eventList)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(eventList: EventList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinedAdapter.ViewHolder {
        val binding =
            ItemListCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JoinedAdapter.ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventList>() {
            override fun areItemsTheSame(oldItem: EventList, newItem: EventList): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: EventList,
                newItem: EventList,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}