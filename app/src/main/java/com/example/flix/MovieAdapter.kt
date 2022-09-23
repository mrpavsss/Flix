package com.example.flix

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"
private const val POPULAR = 0
private const val NORMAL = 1
class MovieAdapter(private val context: Context, private val movies: List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val inflater = LayoutInflater.from(context)
        val viewHolder: ViewHolder = when (viewType) {
            POPULAR -> {
                val v1: View = inflater.inflate(R.layout.popular_item, parent, false)
                ViewHolder(v1)
            }
            else -> {
                val v2: View = inflater.inflate(R.layout.item_movie, parent, false)
                ViewHolder(v2)
            }
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        val movie = movies[position]
        if (movie.voteAverage > 7.1) {
            return POPULAR
        }
        return NORMAL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position $position")
        val movie = movies[position]
        when (holder.itemViewType) {
            POPULAR -> {
                holder.bindPopular(movie)
            }
            else -> {
                holder.bindNormal(movie)
            }
        }
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val backdropImage = itemView.findViewById<ImageView>(R.id.backdropImage)
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)

        init {
            itemView.setOnClickListener(this)
        }

        fun bindPopular(movie: Movie) {
            Glide.with(context)
                .load(movie.backdropImageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(backdropImage)
        }

        fun bindNormal(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            val orientation = context.resources.configuration.orientation

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Glide.with(context)
                    .load(movie.posterImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(ivPoster)
            }
            else {
                Glide.with(context)
                    .load(movie.backdropImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(ivPoster)
            }
        }

        override fun onClick(p0: View?) {
            val movie = movies[adapterPosition]
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)
            if (movie.voteAverage <= 7.1) {
                val activity = context as Activity
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    tvTitle,
                    "tvTitle")
                context.startActivity(intent, options.toBundle())
            }
            else {
                context.startActivity(intent)
            }
        }
    }

}