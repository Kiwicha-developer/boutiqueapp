package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ComunityActivity : AppCompatActivity(){
    private lateinit var jsonText: TextView
    private lateinit var menuBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        menuBar = findViewById(R.id.community_menu)
        jsonText = findViewById(R.id.community_json_text)

        val userId = intent.getIntExtra("id_user",-1)

        menuBar.setOnItemSelectedListener {
            val target = when(it.itemId) {
                R.id.menu_home -> HomeActivity::class.java
                R.id.menu_shopping_cart -> CartActivity::class.java
                R.id.menu_community -> ComunityActivity::class.java
                R.id.menu_profile -> ProfileActivity::class.java
                else -> null
            }
            target?.let { activity ->
                val intent = Intent(this,activity).apply {
                    putExtra("id_user", userId)
                }
                startActivity(intent)
            }
            true
        }

        getPosts()

    }

    private fun getPosts() {
        val fireDb = FirebaseFirestore.getInstance()

        fireDb.collection("posts").get()
            .addOnSuccessListener { result ->
                val builder = StringBuilder()

                for (document in result) {
                    val user = document.getString("usuario") ?: "Anónimo"
                    val title = document.getString("title") ?: "Sin título"
                    val body = document.getString("body") ?: "Sin contenido"

                    builder.appendLine("<br>--------------------------------------<br>")
                    builder.appendLine("$user<br>")
                    builder.appendLine("<b>$title </b><br>")
                    builder.appendLine("$body<br>")
                }

                jsonText.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_LEGACY)
            }
            .addOnFailureListener { exception ->
                jsonText.text = "Error: ${exception.message}"
            }
    }
}