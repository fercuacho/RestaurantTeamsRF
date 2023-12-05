package com.example.restaurantteamsrf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantteamsrf.databinding.ItemVideoBinding
import com.example.restaurantteamsrf.model.Video

class VideosAdapter(val videos: ArrayList<Video>): RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    class ViewHolder (private val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(video: Video){
            with(binding){
                tvVideoTitle.text = video.title
                tvVideoDescription.text = video.description
                vvVideo.setVideoPath(video.url)
                vvVideo.setOnPreparedListener{ mediaplayer ->
                    pbVideo.visibility = View.GONE
                    mediaplayer.start()
                    val videoRatio: Float = mediaplayer.videoWidth / mediaplayer.videoHeight.toFloat()
                    val screenRatio: Float = vvVideo.width / vvVideo.height.toFloat()

                    val scale: Float = videoRatio / screenRatio

                    if (scale >= 1){
                        vvVideo.scaleX = scale
                    }else{
                        vvVideo.scaleY = 1f/scale
                    }

                    //Para que cambie al completarse
                    vvVideo.setOnCompletionListener { mediaplayer ->
                        mediaplayer.start()
                    }

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind((videos[position]))
    }


}