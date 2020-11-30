package com.fave.cinemabooking.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fave.cinemabooking.R
import com.fave.cinemabooking.databinding.MovieViewHolderBinding
import com.fave.cinemabooking.models.domain_models.MovieListDomainModel

class PagingAdapter(onRecyclerViewItemClickListener: (position: Int, item: Any) -> Unit) :
    PagingDataAdapter<MovieListDomainModel, PagingAdapter.ViewHolder>(DataDifferentiator) {


    private var _onRecyclerViewItemClickListener:
                (position: Int, item: Any) -> Unit = onRecyclerViewItemClickListener

    class ViewHolder(val binding: MovieViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieListDomainModel) {
            binding.movieListDomainModel = item
            val imgUrl = "https://image.tmdb.org/t/p/w500/${item.imgUrl}"
            binding.movieImg.load(imgUrl) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
            binding.executePendingBindings()
        }
    }

    object DataDifferentiator : DiffUtil.ItemCallback<MovieListDomainModel>() {
        override fun areItemsTheSame(
            oldItem: MovieListDomainModel,
            newItem: MovieListDomainModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieListDomainModel,
            newItem: MovieListDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieData = getItem(position)
        movieData?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                getItem(position)?.let { item -> _onRecyclerViewItemClickListener(position, item) }
            }
        }
//        val movieData = getItem(position)
//        movieData?.let {
//            holder.itemView.movieTitle.text = it.movieTitle
//            holder.itemView.movieRating.text = it.moviePopularity
//            val imgUrl = "https://image.tmdb.org/t/p/w500/${it.imgUrl}"
//            holder.itemView.movieImg.load(imgUrl) {
//                crossfade(true)
//                error(R.mipmap.ic_launcher)
//            }
//            holder.itemView.setOnClickListener {
//                getItem(position)?.let { item -> _onRecyclerViewItemClickListener(position, item) }
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MovieViewHolderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.movie_view_holder,
            parent,
            false
        )
        return ViewHolder(binding)
//        return ViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.movie_view_holder, parent, false))
    }
}
