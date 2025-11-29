package com.example.dairy_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_p)

        val nameField = findViewById<EditText>(R.id.inputName)
        val passField = findViewById<EditText>(R.id.inputPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)

        loginBtn.setOnClickListener {
            val name = nameField.text.toString()
            val pass = passField.text.toString()

            if (name.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", name)
            startActivity(intent)
        }
    }
}
