package com.example.vidart

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View.inflate
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern
import kotlin.concurrent.thread
import com.example.vidart.donwloadPage


class MainActivity : AppCompatActivity() {

    private lateinit var buttonTiktok: Button
    private lateinit var buttonYT: Button
    private lateinit var buttonIg: Button
    private lateinit var buttonX: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize buttons
        buttonTiktok = findViewById(R.id.buttonTikTok)
        buttonYT = findViewById(R.id.buttonYT)
        buttonIg = findViewById(R.id.buttonIG)
        buttonX = findViewById(R.id.buttonX)

        // Set listener for Instagram downloader button
        buttonIg.setOnClickListener {

//            val instagramUrl = recievingData()  // Replace with dynamic URL if necessary
//            downloadInstagramVideo(instagramUrl)

            val i = Intent(this, donwloadPage::class.java)
            startActivity(i)

        }

        // Other buttons navigate to downloadPage activity
        setupButtonClickListener(buttonTiktok)
        setupButtonClickListener(buttonYT)
        setupButtonClickListener(buttonX)
    }

    private fun setupButtonClickListener(button: Button) {
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, Donlodpart2::class.java)
            startActivity(intent)
        }
    }

    private fun downloadInstagramVideo(instagramUrl: String) {
        // Building request for API
        val request = Request.Builder()
            .url("https://instagram-media-downloader.p.rapidapi.com/rapid/post.php?url=$instagramUrl")
            .get()
            .addHeader("x-rapidapi-key", "4e6076cfb3mshbd13b9a5ec1068dp1878d5jsn0757f72d8195")
            .addHeader("x-rapidapi-host", "instagram-media-downloader.p.rapidapi.com")
            .build()

        // Running in background thread
        thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    val jsonObject = JSONObject(jsonData ?: "{}")
                    val videoUrl = jsonObject.optString("video")

                    if (videoUrl.isNotEmpty()) {
                        saveVideoToStorage(videoUrl)
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Failed to get video URL.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to download video: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveVideoToStorage(videoUrl: String) {
        val request = Request.Builder().url(videoUrl).build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Failed to download video: ${response.code}")

                val fileName = cleanFileName(videoUrl.split("/").lastOrNull() ?: "downloaded_video.mp4")
                val videoFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$fileName.mp4")

                FileOutputStream(videoFile).use { output ->
                    response.body?.byteStream()?.copyTo(output)
                }

                runOnUiThread {
                    Toast.makeText(this, "Video saved at ${videoFile.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error saving video: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cleanFileName(fileName: String): String {
        // Remove invalid characters from the filename
        val pattern = Pattern.compile("[^a-zA-Z0-9._-]")
        return pattern.matcher(fileName).replaceAll("_")
    }
}