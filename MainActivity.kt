package com.example.dairy_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    data class Dairy(
        val name: String,
        val price: String,
        val imageUrl: String
    )

    // Adapter for the RecyclerView that displays the list of Pokemon
    class DairyAdapter(private val dairyList: MutableList<Dairy>) :
        RecyclerView.Adapter<DairyAdapter.DairyViewHolder>() {

        class DairyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.nameText)
            val typeTextView: TextView = itemView.findViewById(R.id.typeText)
            val imageView: ImageView = itemView.findViewById(R.id.itemImage)
        }


        //Loads one Pokemon layout (pokemon_item.xml) into the RecyclerView
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DairyViewHolder  {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.dairy_item, parent, false)
            return DairyViewHolder(itemView)
        }

        // Take one Pokemon from the list and fills in the layout
        //name
        //type
        //image
        //Glide downloads and displays the image from the URL
        override fun onBindViewHolder(holder: DairyViewHolder, position: Int) {
            val currentPokemon = dairyList[position]
            holder.nameTextView.text = currentPokemon.name
            holder.typeTextView.text = currentPokemon.types.joinToString(", ")
            Glide.with(holder.itemView)
                .load(currentPokemon.imageUrl)
                .centerInside()
                .into(holder.imageView)
        }

        //Tells the RecyclerView how many items to display
        override fun getItemCount(): Int = dairyList.size
    }

    class MainActivity : AppCompatActivity() {

        private val baseUrl = "https://pokeapi.co/api/v2/pokemon/"
        private lateinit var recyclerView: RecyclerView
        //UI element that displays the scrolling feed of Pokemon

        private lateinit var DairyAdapter: DairyAdapter
        // the bridge between data and the RecyclerView
        private val dairyList = mutableListOf<Dairy>()
        //Stores all Pokemon objects that have been fetched

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.adapter = adapter
            adapter = DairyAdapter(dairyList)
            recyclerView.layoutManager = LinearLayoutManager(this)


            // Load the first Pokémon
            loadSampleDairyItems()


