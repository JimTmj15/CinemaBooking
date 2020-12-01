package com.fave.cinemabooking.view_models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fave.cinemabooking.data_sources.PagingDataSource
import com.fave.cinemabooking.models.domain_models.MovieListDomainModel
import com.fave.cinemabooking.utils.mapper.DomainMapper
import com.fave.cinemabooking.utils.repository.Repository
import kotlinx.coroutines.flow.Flow

class MovieListViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val domainMapper: DomainMapper
): ViewModel() {


    fun movieListData(): Flow<PagingData<MovieListDomainModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PagingDataSource(repository = repository,
                domainMapper = domainMapper) }
        ).flow.cachedIn(viewModelScope)
    }

}