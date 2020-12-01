package com.fave.cinemabooking.utils.mapper

import com.fave.cinemabooking.models.domain_models.MovieDetailsDomainModel
import com.fave.cinemabooking.models.domain_models.MovieListDomainModel
import com.fave.cinemabooking.models.network.models.movie_details.NetworkMovieDetailsModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.extensions.to2DecimalPoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/*
* This class functions to map the data returned from API call to the
* corresponding UI/Business Logic using domain model design pattern.
*/
@Singleton
class DomainMapper @Inject constructor() {

    suspend fun mapNetworkMovieListToDomainModel(result: NetworkMovieListModel):
            MutableList<MovieListDomainModel> =
        coroutineScope {
            withContext(Dispatchers.IO) {
                when {
                    result.results.isEmpty() -> mutableListOf()
                    else -> {
                        val res = result.results.sortedBy {
                            it.release_date
                        }
                        res.map {
                            MovieListDomainModel(
                                id = it.id.toString(),
                                imgUrl = it.poster_path ?: "",
                                movieTitle = it.title ?: "",
                                moviePopularity = (it.popularity ?: 0.00).to2DecimalPoints()
                                    .toString(),
                                synopsis = it.overview ?: ""
                            )
                        }.toMutableList()
                    }
                }
            }
        }


    suspend fun mapNetworkMovieDetailsToDomainModel(result: NetworkMovieDetailsModel):
            MovieDetailsDomainModel =
        coroutineScope {
            withContext(Dispatchers.IO) {
                var genre = ""
                result.genres?.let {
                    it.forEach { genreData ->
                        genre += genreData.name + ","
                    }
                    genre = genre.dropLast(1) //to remove the last comma
                }

                if(genre.isEmpty()) {
                    genre = "Nil" //if genre is empty, show Nil
                }
                MovieDetailsDomainModel(
                    genre = genre,
                    language = result.original_language ?: "",
                    duration = (result.runtime ?: 0).toString()
                )
            }

        }
}