package com.bangkit.relaverse.data.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.relaverse.data.remote.response.CampaignData
import com.bangkit.relaverse.databinding.ItemListCampaignBinding
import com.bangkit.relaverse.ui.main.home.DetailsHomeActivity
import com.bumptech.glide.Glide

class CampaignAdapter(private val context: Context) :
    ListAdapter<CampaignData, CampaignAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemListCampaignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(campaignData: CampaignData) {
            binding.apply {
                Glide.with(root.context)
                    .load(campaignData.photoEvent)
                    .into(imageView)

                tvTitle.text = campaignData.title
                tvLocation.text = campaignData.location
                root.setOnClickListener {
                    val intent = Intent(root.context, DetailsHomeActivity::class.java).apply {
                        putExtra(DetailsHomeActivity.CAMPAIGN_ID, campaignData.id)
                    }
                    root.context.startActivity(intent)
                }
            }
        }
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