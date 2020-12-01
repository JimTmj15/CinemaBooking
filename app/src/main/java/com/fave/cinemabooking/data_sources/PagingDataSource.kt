package com.fave.cinemabooking.data_sources

import androidx.paging.PagingSource
import com.fave.cinemabooking.models.domain_models.MovieListDomainModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.constant.Constants
import com.fave.cinemabooking.utils.helpers.Helper
import com.fave.cinemabooking.utils.mapper.DomainMapper
import com.fave.cinemabooking.utils.network.responses.ApiResults
import com.fave.cinemabooking.utils.repository.Repository
import retrofit2.HttpException
import java.io.IOException

/**
 * This class functions to gather data from the API calls based on the
 * loading key
 */
class PagingDataSource constructor(private val repository: Repository,
                                   private val domainMapper: DomainMapper):
    PagingSource<Int, MovieListDomainModel>() {

    val apiKey = Constants.API_KEY.key

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListDomainModel> {
        try {
            val curLoadingKey = params.key ?: 1
            var totalPage = 0

            val result = repository.getMovieList(
                apiKey = apiKey,
                releaseDate = "2016-01-01",
                sortBy = "release_date.desc",
                page = curLoadingKey.toString())

            var responseData:MutableList<MovieListDomainModel> = mutableListOf()

            //the dataTransformed helps to map Network Response into Api Result - Success/Failure
            when(val transformedResult = Helper.dataTransformer(result = result)){
                is ApiResults.Success -> {
                    val res = transformedResult.data as NetworkMovieListModel
                    totalPage = res.total_pages
                    //the domain mapper maps the data into domain model (data required for display)
                    val items = domainMapper.mapNetworkMovieListToDomainModel(result = res)
                    responseData = items
                }
                is ApiResults.Failure -> {
                    val err = transformedResult.err
                    return LoadResult.Error(Throwable(err))
                }
            }

            return LoadResult.Page(
                data = responseData,
                prevKey = null, //only paging forward
                nextKey = if(totalPage == curLoadingKey) null else curLoadingKey.plus(1)
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}
