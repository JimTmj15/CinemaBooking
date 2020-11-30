package com.fave.cinemabooking.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fave.cinemabooking.R
import com.fave.cinemabooking.models.domain_models.MovieListDomainModel
import com.fave.cinemabooking.utils.adapters.PagingAdapter
import com.fave.cinemabooking.utils.constant.Constants
import com.fave.cinemabooking.utils.helpers.Helper
import com.fave.cinemabooking.view_models.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {

    private val movieListViewModel: MovieListViewModel by viewModels()
    lateinit var pagingAdapter: PagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        initialise()
        getMovieList()

        pagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            )
            // Show ProgressBar
//            loader.show(this@MovieListActivity)
            else {
                // Hide ProgressBar
//                loader.dismiss()
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    it.error.message?.let { errMsg ->
                        Helper.showToast(this@MovieListActivity, errMsg)
                    }
                }
            }
        }

    }

    private fun gotoMovieDetails(movieId: String, synopsis: String) {
        val intent = Intent(this@MovieListActivity, MovieDetailsActivity::class.java)
        intent.putExtra(Constants.INTENT_MOVIE_ID.key, movieId)
        intent.putExtra(Constants.INTENT_SYNOPSIS.key, synopsis)
        startActivity(intent)
    }

    private fun initialise() {
        pagingAdapter = PagingAdapter { _, item ->
            val result = item as MovieListDomainModel
            gotoMovieDetails(result.id, result.synopsis)
        }

        moviesRv.apply {
            layoutManager = LinearLayoutManager(this@MovieListActivity)
            adapter = pagingAdapter
        }
    }

    private fun getMovieList() {
        lifecycleScope.launch {
            movieListViewModel.movieListData().collect {
                pagingAdapter.submitData(it)
            }
        }
    }

}