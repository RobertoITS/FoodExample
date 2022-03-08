package com.raqueveque.foodexample.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.raqueveque.foodexample.R
import com.raqueveque.foodexample.detail.SliderAdapter.SliderViewHolder
import com.squareup.picasso.Picasso

class SliderAdapter(imageList: ArrayList<ImageSlider>, imageSliderViewPager: ViewPager2) : RecyclerView.Adapter<SliderViewHolder>() {

    private val sliderItems: List<ImageSlider>
    //
    private val viewPager2: ViewPager2
    init {
        this.sliderItems = imageList
        //
        this.viewPager2 = imageSliderViewPager
    }

    class SliderViewHolder (item: View): RecyclerView.ViewHolder(item) {
        private val image: ImageView = item.findViewById(R.id.imageContainer)

        fun image(sliderItem: ImageSlider){
            Picasso.get().load(sliderItem.image).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_container, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.image(sliderItems[position])
        //Hasta aca la animacion:
        if (position == sliderItems.size - 2){
            viewPager2.post(runnable)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val runnable = Runnable {
        imageList.addAll(sliderItems)
        notifyDataSetChanged()
    }

}
