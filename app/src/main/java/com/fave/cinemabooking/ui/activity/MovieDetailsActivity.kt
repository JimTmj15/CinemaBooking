package com.fave.cinemabooking.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fave.cinemabooking.R
import com.fave.cinemabooking.models.domain_models.MovieDetailsDomainModel
import com.fave.cinemabooking.utils.constant.Constants
import com.fave.cinemabooking.utils.helpers.Helper
import com.fave.cinemabooking.utils.network.responses.ApiResults
import com.fave.cinemabooking.view_models.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_details.*


@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private var movieId = ""
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        subscribeToObserver() //set up observer for live data
        intent.getStringExtra(Constants.INTENT_MOVIE_ID.key)?.let {
            movieId = it
            movieDetailsViewModel.getMovieDetails(movieId)
        }

        intent.getStringExtra(Constants.INTENT_SYNOPSIS.key)?.let {
            synopsisTv.text = it
        }

        bookBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.CINEMA_URL.key))
            startActivity(browserIntent)
        }
    }

    private fun subscribeToObserver() {
        movieDetailsViewModel.movieDetails.observe(this@MovieDetailsActivity, {
            when (it) {
                is ApiResults.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ApiResults.Success -> {
                    progressBar.visibility = View.GONE
                    val result = it.data as MovieDetailsDomainModel
                    languageTv.text = result.language
                    genreTv.text = result.genre
                    val duration = result.duration + " minutes"
                    durationTv.text = duration
                }
                is ApiResults.Failure -> {
                    progressBar.visibility = View.GONE
                    val err = it.err
                    Helper.showToast(this@MovieDetailsActivity, err)
                }
            }
        })
    }
}