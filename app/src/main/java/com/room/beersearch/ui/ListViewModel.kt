package com.room.beersearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.room.beersearch.datasource.PagingDataSource
import com.room.beersearch.model.BeerSearch
import com.room.beersearch.model.DataModel
import com.room.beersearch.retrofit.BeerService
import com.room.beersearch.retrofit.BeerServiceFactory
import kotlinx.coroutines.flow.*

class ListViewModel () : ViewModel(){

    var retroService: BeerService = BeerServiceFactory.create()

    fun getFromNetwork(keyword: String) : Flow<PagingData<DataModel>>
    =  Pager(config = PagingConfig(
        pageSize = 20,
        maxSize = 100,
        enablePlaceholders = false),
        pagingSourceFactory = { PagingDataSource(keyword, retroService) })
        .flow.map { paginddata ->
                 paginddata.map <BeerSearch, DataModel> { DataModel.Data(it) }
                    .insertSeparators { before: DataModel?, after: DataModel? ->
                        if (before is DataModel.Header || after is DataModel.Header)
                            DataModel.Separator
                        else
                            null
                    }
            }
        .cachedIn(viewModelScope)
}