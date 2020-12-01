package com.fave.cinemabooking.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fave.cinemabooking.R
import com.fave.cinemabooking.databinding.ActivityMovieDetailsBinding
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
    lateinit var mBinding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)

        binding.lifecycleOwner = this
        mBinding = binding
        subscribeToObserver() //set up observer for live data

        //to get movie id from prev activity
        intent.getStringExtra(Constants.INTENT_MOVIE_ID.key)?.let {
            movieId = it
            movieDetailsViewModel.getMovieDetails(movieId)
        }

        //get synopsis from prev activity
        intent.getStringExtra(Constants.INTENT_SYNOPSIS.key)?.let {
            synopsisTv.text = it
        }

        binding.bookBtn.setOnClickListener {
            //to open URL from browser externally
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.CINEMA_URL.key)
            )
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
                    mBinding.movieDetailsDomainModel = result
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