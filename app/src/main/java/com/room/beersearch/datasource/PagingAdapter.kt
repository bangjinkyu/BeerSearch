package com.room.beersearch.datasource

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.room.beersearch.R
import com.room.beersearch.databinding.ItemAdapterBinding
import com.room.beersearch.databinding.ItemHeaderBinding
import com.room.beersearch.databinding.ItemSeparatorBinding
import com.room.beersearch.model.DataModel


class PagingAdapter : PagingDataAdapter<DataModel, RecyclerView.ViewHolder>(diffCallback) {

    var word: String = ""

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return -1
        Log.d("PagingAdapter", "getItemViewType $item")
        return when (item) {
            is DataModel.Header -> HEADER
            is DataModel.Data -> DATA
            is DataModel.Separator -> SEPARATOR
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        Log.d("PagingAdapter", "onCreateViewHolder")

        return  when (viewType) {
            HEADER -> PagingHeaderViewHolder(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DATA -> PagingViewHolder(ItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            SEPARATOR -> PagingSeparatorViewHolder(ItemSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)!!
        Log.d("PagingAdapter", "onBindViewHolder")
            when (holder) {
                is PagingHeaderViewHolder -> holder.bind(item as DataModel.Header)
                is PagingViewHolder -> { holder.bind(item as DataModel.Data)
                    holder.itemView.setOnClickListener {
                        val navController = Navigation.findNavController(holder.itemView)
                        val bundle = Bundle()
                        bundle.putString("keyword",word)
                        bundle.putParcelable("beeritem", (item as DataModel.Data).value)
                        navController.navigate(R.id.action_ListFragment_to_DetailFragment, bundle)
                    }
                }
            }
    }


    inner class PagingHeaderViewHolder(
        private val binding: ItemHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataModel.Header) {
            binding.headerTitle.text = data.title
        }

    }


    inner class PagingViewHolder(
        val itemBinding : ItemAdapterBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: DataModel.Data) {
            Log.d("PagingAdapter", "PagingViewHolder")
            itemBinding.item = data.value
        }
    }

    inner class PagingSeparatorViewHolder(binding: ItemSeparatorBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val HEADER = 0
        private const val DATA = 1
        private const val SEPARATOR = 2

        val diffCallback = object : DiffUtil.ItemCallback<DataModel>() {
            override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}