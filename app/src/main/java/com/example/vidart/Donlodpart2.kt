package com.example.vidart

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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

class Donlodpart2 : AppCompatActivity() {

    private val p360p: Button by lazy { findViewById(R.id.p360p_button) }
    private val p480p: Button by lazy { findViewById(R.id.p480p_button) }
    private val p720p: Button by lazy { findViewById(R.id.p720p_button) }
    private val client = OkHttpClient()

    private lateinit var videoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donlod_part2)

        // Instagram URL dari MainActivity
        videoUrl = intent.getStringExtra("instagramUrl") ?: ""

       
        val backButton = findViewById<ImageButton>(R.id.BackButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        fun recievingData(): String {
            val bundle:Bundle? = intent.extras
            val InstagramUrl = bundle?.getString("instagramUrl").toString()
            return InstagramUrl
        }

        val instagramUrl = recievingData()


        // coba atur kualitas
        p360p.setOnClickListener {
            saveVideoToStorage(instagramUrl)
        }
        p480p.setOnClickListener {
            saveVideoToStorage(instagramUrl)
        }
        p720p.setOnClickListener {
            saveVideoToStorage(instagramUrl)
        }
    }

    // Function to initiate video download based on resolution
//    private fun downloadVideoInResolution(resolution: String) {
//        if (videoUrl.isNotEmpty()) {
//            // Construct the URL for the API request based on resolution
//            val resolutionUrl = constructResolutionUrl(videoUrl, resolution)
//
//            // Start background thread to make the API call
//            thread {
//                try {
//                    val request = Request.Builder().url(resolutionUrl).build()
//                    val response = client.newCall(request).execute()
//                    if (response.isSuccessful) {
//                        val jsonData = response.body?.string()
//                        val jsonObject = JSONObject(jsonData ?: "{}")
//                        val videoDownloadUrl = jsonObject.optString("video")
//
//                        if (videoDownloadUrl.isNotEmpty()) {
//                            saveVideoToStorage(videoDownloadUrl)
//                        } else {
//                            runOnUiThread {
//                                Toast.makeText(this, "Failed to get video URL for $resolution.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    } else {
//                        runOnUiThread {
//                            Toast.makeText(this, "Failed to download video: ${response.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    runOnUiThread {
//                        Toast.makeText(this, "Error downloading video: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(this, "Invalid video URL.", Toast.LENGTH_SHORT).show()
//        }
//    }

    // Function to construct the correct URL based on resolution
//    private fun constructResolutionUrl(originalUrl: String, resolution: String): String {
//        return "https://some-video-api.com/download?url=$originalUrl&resolution=$resolution"
//    }

    // Function to save video to storage
    private fun saveVideoToStorage(videoUrl: String) {
        val request = Request.Builder().url(videoUrl).build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Failed to download video: ${response.code}")

                val fileName = cleanFileName(videoUrl.split("/").lastOrNull() ?: "downloaded_video.mp4")
                val videoFile = File(getExternalFilesDir(null), "$fileName.mp4")


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

    // Clean file name to avoid invalid characters
    private fun cleanFileName(fileName: String): String {
        val pattern = Pattern.compile("[^a-zA-Z0-9._-]")
        return pattern.matcher(fileName).replaceAll("_")
    }
}


//package com.example.vidart
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class Donlodpart2 : AppCompatActivity() {
//
//    val p360p : Button = findViewById(R.id.p360p_button)
//    val p480p : Button = findViewById(R.id.p480p_button)
//    val p720p : Button = findViewById(R.id.p720p_button)
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.donlod_part2)
//
//
//        fun recievingData(): String {
//            val bundle:Bundle? = intent.extras
//            val InstagramUrl = bundle?.getString("instagramUrl").toString()
//            return InstagramUrl
//        }
//
//
//
//        val backButton = findViewById<ImageButton>(R.id.BackButton)
//
//        backButton.setOnClickListener {
//            onBackPressed()
//        }
//    }
//}
