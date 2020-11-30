package com.fave.cinemabooking.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fave.cinemabooking.R
import com.fave.cinemabooking.models.domain_models.MovieListDomainModel
import com.fave.cinemabooking.models.others.Data
import com.fave.cinemabooking.utils.di.qualifiers.BaseUrl
import kotlinx.android.synthetic.main.movie_view_holder.view.*

class PagingAdapter(onRecyclerViewItemClickListener: (position: Int, item: Any) -> Unit):
    PagingDataAdapter<MovieListDomainModel, PagingAdapter.ViewHolder>(DataDifferentiator) {


    private var _onRecyclerViewItemClickListener:
                (position: Int, item: Any) -> Unit = onRecyclerViewItemClickListener

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    object DataDifferentiator : DiffUtil.ItemCallback<MovieListDomainModel>() {
        override fun areItemsTheSame(oldItem: MovieListDomainModel, newItem: MovieListDomainModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieListDomainModel, newItem: MovieListDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieData = getItem(position)
        movieData?.let {
            holder.itemView.movieTitle.text = it.movieTitle
            holder.itemView.movieRating.text = it.moviePopularity
            val imgUrl = "https://image.tmdb.org/t/p/w500/${it.imgUrl}"
            holder.itemView.movieImg.load(imgUrl) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
            holder.itemView.setOnClickListener {
                getItem(position)?.let { item -> _onRecyclerViewItemClickListener(position, item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_view_holder, parent, false))
    }
}
