package com.example.dairy_app


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

// Define the data class outside of MainActivity for better organization
data class Dairy(
    val name: String,
    val price: Double,
    val imageUrl: String
)

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://691a2d109ccba073ee95192f.mockapi.io/api/dairy/Dairy_Products"
    private lateinit var recyclerView: RecyclerView
    private lateinit var dairyAdapter: DairyAdapter // Renamed for clarity
    private var dairyList = mutableListOf<Dairy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.dairyRecycler)

        // 1. Initialize the adapter with the (currently empty) list
        dairyAdapter = DairyAdapter(dairyList) { dairyItem ->
            // Handle item click if needed
            Log.d("CART", "Added to cart: ${dairyItem.name}")
        }

        // 2. Set the adapter and layout manager for the RecyclerView
        recyclerView.adapter = dairyAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch data from the API
        fetchDairyData()
    }

    private fun fetchDairyData() {
        val client = AsyncHttpClient()
        client.get(baseUrl, object : JsonHttpResponseHandler() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("MainActivity", "Response successful: $json")
                try {
                    // In your provided code, the API returns a JSON array directly
                    val dairyArray = json.jsonArray
                    for (i in 0 until dairyArray.length()) {
                        val dairyObject = dairyArray.getJSONObject(i)
                        val name = dairyObject.getString("name")
                        val price: String = dairyObject.getString("price")
                        val imageUrl = dairyObject.getString("image_url") // Make sure your JSON key is "image_url"
                        dairyList.add(Dairy(name, price.toDouble(), imageUrl))
                    }
                    // Notify the adapter that the data has changed so it can update the UI
                    dairyAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Log.e("MainActivity", "Error parsing JSON", e)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("MainActivity", "Failed to fetch data: $statusCode, Response: $response")
            }
        })
    }
}


// Adapter for the RecyclerView that displays the list of Dairy items
class DairyAdapter(
    private val dairyList: List<Dairy>,
    private val onAddToCartClick: (Dairy) -> Unit
) : RecyclerView.Adapter<DairyAdapter.DairyViewHolder>() {

    // ViewHolder holds the views for a single item in the list
    class DairyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.itemImage)
        val name: TextView = itemView.findViewById(R.id.nameText)
        val price: TextView = itemView.findViewById(R.id.priceText) // This shows the price

        val addButton: Button = itemView.findViewById(R.id.addButton)
    }

    // Inflates the item layout (dairy_item.xml) and creates the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DairyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dairy_item, parent, false)
        return DairyViewHolder(itemView)
    }

    // Binds the data from the dairyList to the views in the ViewHolder
    override fun onBindViewHolder(holder: DairyViewHolder, position: Int) {
        val currentDairyItem = dairyList[position]

        holder.name.text = currentDairyItem.name
        holder.price.text = "$${currentDairyItem.price}"

        // Use Glide to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentDairyItem.imageUrl)
            .centerInside()
            .into(holder.image)

        holder.addButton.setOnClickListener {
            onAddToCartClick(currentDairyItem)
        }
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int = dairyList.size
}


