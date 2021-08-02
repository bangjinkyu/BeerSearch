package com.room.beersearch.datasource

import android.util.Log
import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingState
import androidx.paging.PagingSource
import com.room.beersearch.BeerApplication.Companion.getContext
import com.room.beersearch.model.BeerSearch
import com.room.beersearch.retrofit.BeerService
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class PagingDataSource (
    private val keyword: String,
    private val service: BeerService
) : PagingSource<Int, BeerSearch>() {

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, BeerSearch>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerSearch> {
        val page:Int  = params.key ?: FIRST_PAGE_INDEX
        return try {
            delay(500)
            val response = service.getBooksName(keyword, page, params.loadSize)

            LoadResult.Page(
                data = response,
                prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                nextKey =  if (response.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(Throwable("Paging Error"))
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }
}