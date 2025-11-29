package com.example.dairy_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.jvm.java


// -------------------------
// DATA MODEL
// -------------------------
data class Dairy(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String
)

// -------------------------
// ADAPTER
// -------------------------
class DairyAdapter(
    private val dairyList: MutableList<Dairy>,
    private val onAddToCartClick: (Dairy) -> Unit
) : RecyclerView.Adapter<DairyAdapter.DairyViewHolder>() {

    class DairyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val addButton: Button = itemView.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DairyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dairy_item, parent, false)
        return DairyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DairyViewHolder, position: Int) {
        val item = dairyList[position]

        Glide.with(holder.itemView)
            .load(item.imageUrl)
            .centerCrop()
            .into(holder.itemImage)


        holder.nameText.text = item.name
        holder.priceText.text = "$${item.price}"



        holder.addButton.setOnClickListener {
            Cart.addItem(item)
            onAddToCartClick(item)
        }
      }

    override fun getItemCount(): Int = dairyList.size


}

// -------------------------
// MAIN ACTIVITY
// -------------------------
class MainActivity : AppCompatActivity() {


    private val apiUrl = "https://691a2d109ccba073ee95192f.mockapi.io/api/dairy/Dairy_Products"
    private val dairyList = mutableListOf<Dairy>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DairyAdapter

    private val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.dairyRecycler)
        adapter = DairyAdapter(dairyList) { item ->
            Log.d("CART", "Added to cart: ${item.name}")
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        val checkoutButton = findViewById<Button>(R.id.checkoutButton)

        checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }


        fetchDairyProducts()
    }

    private fun fetchDairyProducts() {
        client.get(apiUrl, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("IMG", "imageUrl: ${json.jsonObject}")

                val array: JSONArray = json.jsonArray

                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)

                    val id = obj.optString("id")
                    val name = obj.optString("name", "Unknown")

                    val price = obj.optString("price", "0.0").toDoubleOrNull() ?: 0.0
                    
                    val image = obj.optString("imageUrl", "")

                    dairyList.add(Dairy(id, name, price, image))

                }

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("API", "FAILED → $statusCode\n$response")

            }

        })
    }
}




