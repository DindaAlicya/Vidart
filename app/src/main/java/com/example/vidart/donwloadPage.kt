
 package com.example.vidart

 import android.annotation.SuppressLint
 import android.content.Intent
 import android.os.Bundle
 import android.widget.Button
 import android.widget.EditText
 import android.widget.ImageButton
 import androidx.activity.enableEdgeToEdge
 import androidx.appcompat.app.AppCompatActivity
 import androidx.core.view.ViewCompat
 import androidx.core.view.WindowInsetsCompat
 import android.widget.Toast


 class donwloadPage : AppCompatActivity() {
     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_donwload_page)

         val textInputEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.text_input_edit_text)
         val clearImageButton = findViewById<ImageButton>(R.id.clear_image_button)
         val backButton = findViewById<ImageButton>(R.id.BackButton)
         val downloadButton = findViewById<Button>(R.id.download_button)

         //turn the url edit text into string text
         val InstagramUrl = textInputEditText.text.toString()

         clearImageButton.setOnClickListener {
             textInputEditText.text = null
         }

         //export data to DonlodPart2
         downloadButton.setOnClickListener {
             // Ambil teks dari EditText hanya saat tombol ditekan
             val InstagramUrl = textInputEditText.text.toString()
             if (InstagramUrl.isNotEmpty()) {
                 val i = Intent(this, Donlodpart2::class.java)
                 i.putExtra("instagramUrl", InstagramUrl)
                 startActivity(i)
             } else {
                 // Tampilkan pesan jika teks kosong
                 Toast.makeText(this, "URL tidak boleh kosong", Toast.LENGTH_SHORT).show()
             }
         }


     }
     }
