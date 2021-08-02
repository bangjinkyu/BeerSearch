package com.room.beersearch.datasource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.room.beersearch.R
import com.room.beersearch.databinding.ItemLoadingBinding

class BeerLoadStateAdapter (
    private val retry: () -> Unit
) : LoadStateAdapter<BeerLoadStateAdapter.BeerLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BeerLoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BeerLoadStateViewHolder(ItemLoadingBinding.inflate(layoutInflater, parent, false), retry)
    }

    override fun onBindViewHolder(
        holder: BeerLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    inner class BeerLoadStateViewHolder(
        private val binding : ItemLoadingBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg = loadState.error.localizedMessage
            }

            binding.retryButton.setOnClickListener { retry.invoke() }
            binding.isLoading = loadState is LoadState.Loading
            binding.isError  = loadState is LoadState.Error
        }
    }
}






