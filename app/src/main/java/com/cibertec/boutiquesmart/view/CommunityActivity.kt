package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class CommunityActivity : AppCompatActivity() {

    private lateinit var jsonText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val menuBar = findViewById<BottomNavigationView>(R.id.community_menu)
        jsonText = findViewById(R.id.community_json_text)


        val username = intent.getStringExtra(USERNAME) ?: "invitado"

        menuBar.setOnItemSelectedListener {
            val bundle = Bundle().apply { putString(USERNAME, username) }
            val target = when (it.itemId) {
                R.id.menu_home -> HomeActivity::class.java
//                R.id.menu_shopping_cart -> CartActivity::class.java
                R.id.menu_community -> CommunityActivity::class.java
                R.id.menu_profile -> ProfileActivity::class.java
                else -> null
            }
            target?.let {
                startActivity(Intent(this, it).apply { putExtras(bundle) })
            }
            true
        }
//
//        getPosts()
    }
//
//    private fun getPosts() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://jsonplaceholder.typicode.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val jsonPlaceholder = retrofit.create(JsonPlaceHolderAPI::class.java)
//
//        jsonPlaceholder.getPosts().enqueue(object : Callback<List<Post>> {
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                if (!response.isSuccessful) {
//                    jsonText.text = "Código: ${response.code()}"
//                    return
//                }
//
//                val postList = response.body().orEmpty()
//                val builder = StringBuilder()
//
//                for (post in postList) {
//                    builder.appendLine("\n--------------------------------------")
//                    builder.appendLine("Post: ${post.id}")
//                    builder.appendLine(post.body)
//                }
//
//                jsonText.text = builder.toString()
//            }
//
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                jsonText.text = "Error: ${t.message}"
//            }
//        })
//    }
}