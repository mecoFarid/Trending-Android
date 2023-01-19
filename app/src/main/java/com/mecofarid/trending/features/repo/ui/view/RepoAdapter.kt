package com.mecofarid.trending.features.repo.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mecofarid.trending.databinding.ItemTrendingBinding

class RepoAdapter(
    private val provideLifecycleOwner: LifecycleOwner
) : ListAdapter<RepoView, RepoAdapter.ViewHolder>(RepoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrendingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            lifecycleOwner = provideLifecycleOwner
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))


    class ViewHolder(private val binding: ItemTrendingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(repoView: RepoView) {
            binding.repoView = repoView
        }
    }

    class RepoDiffCallback: DiffUtil.ItemCallback<RepoView>() {
        override fun areItemsTheSame(oldItem: RepoView, newItem: RepoView): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: RepoView, newItem: RepoView): Boolean =
            oldItem == newItem
    }
}
