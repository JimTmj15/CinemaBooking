package com.fave.cinemabooking.models.domain_models

data class MovieListDomainModel(
    val id: String,
    val imgUrl: String,
    val movieTitle: String,
    val moviePopularity: String,
    val synopsis: String
)