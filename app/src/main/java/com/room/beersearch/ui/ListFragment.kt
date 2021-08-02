package com.room.beersearch.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.room.beersearch.datasource.PagingAdapter
import com.room.beersearch.databinding.FragmentListBinding
import com.room.beersearch.datasource.BeerLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private var mbinding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = mbinding!!

    private lateinit var viewModel: ListViewModel

    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mbinding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        initRecyclerView()
        initPaging()

        binding.buttonFirst.setOnClickListener {

            bookServiceSeach(binding.searchEditText.text.toString())
        }
    }

    private fun bookServiceSeach(keyword: String) {
        // Fragment는 viewLifecycleOwner.lifecycleScop 를 사용
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getFromNetwork(keyword).collectLatest { pageData ->
                pagingAdapter.word = keyword
                pagingAdapter.submitData(pageData)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            pagingAdapter = PagingAdapter()
        }
    }

    fun initPaging() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            // header, footer 설정
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = BeerLoadStateAdapter { pagingAdapter.retry()},
                footer = BeerLoadStateAdapter { pagingAdapter.retry()}
            )
        }

        pagingAdapter.addLoadStateListener { loadState  ->
            binding.apply {
                // 로딩 중이지 않을 때 (활성 로드 작업이 없고 에러가 없음)
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                // 로딩 중
                isLoading = loadState.source.refresh is LoadState.Loading

                isError = loadState.source.refresh is LoadState.Error

                Log.d("ListFragment", "itemCount= ${pagingAdapter.itemCount}")

                // 활성 로드 작업이 없고 에러가 없음 & 로드할 수 없음 & 개수 1 미만 (empty)
                if( loadState.source.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached
                    && pagingAdapter.itemCount < 1
                ) {
                    emptyText.isVisible = true
                    recyclerView.isVisible = false
                }  else {
                    emptyText.isVisible = false
                }
            }
        }

        binding.retry.setOnClickListener {
            pagingAdapter.retry()
        }

    }

    override fun onResume() {
        super.onResume()
        val search = arguments?.getString("keyword") ?: ""

        if ( search.isNotEmpty() ) {
            Log.d("ListFragment", "onResume $search")
            binding.searchEditText.setText(search)
            bookServiceSeach(search)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mbinding = null
    }
}