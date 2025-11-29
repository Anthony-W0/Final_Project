package com.example.dairy_app

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout)

        val listLayout = findViewById<LinearLayout>(R.id.cartRecycler)
        val totalText = findViewById<TextView>(R.id.tvTotal)

        var total = 0.0

        for (item in Cart.items) {
            val textView = TextView(this)
            textView.text = "${item.name} - $${item.price}"
            textView.textSize = 18f
            listLayout.addView(textView)

            total += item.price
        }

        totalText.text = "Total: $${String.format("%.2f", total)}"
    }
}
