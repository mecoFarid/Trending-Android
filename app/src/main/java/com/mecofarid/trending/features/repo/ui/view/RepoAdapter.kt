package com.mecofarid.trending.features.repo.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mecofarid.trending.R
import com.mecofarid.trending.common.ext.toNA
import com.mecofarid.trending.databinding.ItemTrendingBinding

class RepoAdapter : ListAdapter<RepoView, RepoAdapter.ViewHolder>(RepoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val repoView = getItem(bindingAdapterPosition)
                repoView.isExpanded = !repoView.isExpanded
                toggleExtrasView(repoView)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))


    class ViewHolder(private val binding: ItemTrendingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(repoView: RepoView) {
            val repo = repoView.repo
            binding.apply {
                name.text = repo.name
                owner.text = repo.owner.login
                description.text = repo.description.toNA()
                language.text = repo.language.toNA()
                starCount.text = repo.stargazersCount.toString()
                Glide.with(avatar)
                    .load(repo.owner.avatarUrl)
                    .placeholder(R.drawable.circle_placeholder_bg)
                    .circleCrop()
                    .into(avatar)
            }

            toggleExtrasView(repoView)
        }

        fun toggleExtrasView(repoView: RepoView) {
            binding.extraHolder.isVisible = repoView.isExpanded
        }
    }

    class RepoDiffCallback: DiffUtil.ItemCallback<RepoView>() {
        override fun areItemsTheSame(oldItem: RepoView, newItem: RepoView): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: RepoView, newItem: RepoView): Boolean =
            oldItem == newItem
    }
}
