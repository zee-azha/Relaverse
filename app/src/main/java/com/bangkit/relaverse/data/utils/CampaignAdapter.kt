package com.bangkit.relaverse.data.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.relaverse.data.remote.response.CampaignData
import com.bangkit.relaverse.databinding.ItemListCampaignBinding
import com.bumptech.glide.Glide

class CampaignAdapter : ListAdapter<CampaignData, CampaignAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val binding: ItemListCampaignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(campaignData: CampaignData) {
            binding.apply {
                Glide.with(root.context)
                    .load(campaignData.photoEvent)
                    .into(imageView)

                tvTitle.text = campaignData.title
                tvLocation.text = campaignData.location
                tvDate.text = campaignData.date.withDateFormat()

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(campaignData)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(campaignData: CampaignData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CampaignData>() {
            override fun areItemsTheSame(oldItem: CampaignData, newItem: CampaignData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CampaignData,
                newItem: CampaignData,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}