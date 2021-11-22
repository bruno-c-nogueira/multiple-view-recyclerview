package net.simplifiedcoding.multiviewlist.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import net.simplifiedcoding.multiviewlist.R
import net.simplifiedcoding.multiviewlist.databinding.ItemDirectorBinding
import net.simplifiedcoding.multiviewlist.databinding.ItemMovieBinding
import net.simplifiedcoding.multiviewlist.databinding.ItemTitleBinding

sealed class HomeRecyclerViewHolder(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: HomeRecyclerViewItem, position: Int) -> Unit)? = null

    class TitleViewHolder(private val binding: ItemTitleBinding) : HomeRecyclerViewHolder(binding) {

        fun bind(obj: HomeRecyclerViewItem.Title) {
            binding.textViewTitle.text = obj.title

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, obj, adapterPosition)
            }

        }

    }

    class DirectorViewHolder(private val binding: ItemDirectorBinding) :
        HomeRecyclerViewHolder(binding) {
        fun bind(obj: HomeRecyclerViewItem.Director) {
            binding.imageViewDirector.loadImage(obj.avatar)
            binding.textViewName.text = obj.name
            binding.textViewMovies.text =
                binding.root.context.getString(R.string.total_movies, obj.movie_count)
            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, obj, adapterPosition)
            }
        }

    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : HomeRecyclerViewHolder(binding) {
        fun bind(obj: HomeRecyclerViewItem.Movie) {
            obj.thumbnail?.let {
                binding.imageViewMovie.loadImage(obj.thumbnail)
                binding.root.setOnClickListener {
                    itemClickListener?.invoke(it, obj, adapterPosition)
                }
            } ?: run {
                binding.imageViewMovie.hide()
            }

        }
    }

}