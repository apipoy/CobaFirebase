package com.afif.cobafirebase

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPrice: TextView

    private var db = Firebase.firestore
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView_product)
        tvName = findViewById(R.id.tv_detail_product_name)
        tvDescription = findViewById(R.id.tv_productDescription)
        tvPrice = findViewById(R.id.tv_detail_price)

        // Assuming "products" is the collection name. Change it to your actual collection name.
        readData(db.collection("products"))
    }

    private fun readData(collection: CollectionReference) {
        collection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "Data: ${document.id} => ${document.data}")

                    // Extract data from the document
                    val imageUrl = document.getString("Image")
                    val name = document.getString("Name")
                    val description = document.getString("Description")
                    val price = document.getLong("Price")?.toInt() ?: 0

                    // Update UI elements
                    tvName.text = name
                    tvDescription.text = description
                    tvPrice.text = price.toString()

                    // Load image using Glide
                    Glide.with(this@MainActivity)
                        .load(imageUrl)
                        .into(imageView)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: $exception")
                // Handle failure, e.g., show a Toast or log an error message.
            }
    }
}
