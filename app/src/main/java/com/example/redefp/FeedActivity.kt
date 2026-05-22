package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerPosts: RecyclerView

    private lateinit var navHome: LinearLayout
    private lateinit var navBuscar: LinearLayout
    private lateinit var navMensagens: LinearLayout
    private lateinit var navEu: LinearLayout

    private lateinit var btnMenu: ImageButton
    private lateinit var btnNotificacao: ImageButton

    private val posts = ArrayList<PostModel>()

    private lateinit var adapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_feed)

        esconderSistema()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerPosts =
            findViewById(R.id.recyclerPosts)

        navHome =
            findViewById(R.id.navHome)

        navBuscar =
            findViewById(R.id.navBuscar)

        navMensagens =
            findViewById(R.id.navMensagens)



        navEu =
            findViewById(R.id.navEu)

        btnMenu =
            findViewById(R.id.btnMenu)

        btnNotificacao =
            findViewById(R.id.btnNotificacao)

        adapter = FeedAdapter(posts)

        recyclerPosts.layoutManager =
            LinearLayoutManager(this)

        recyclerPosts.adapter =
            adapter

        carregarPosts()

        // MENU
        btnMenu.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    GroupsActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        // NOTIFICAÇÕES TOPO
        btnNotificacao.setOnClickListener {

            Toast.makeText(
                this,
                "Notificações em breve",
                Toast.LENGTH_SHORT
            ).show()
        }

        // HOME
        navHome.setOnClickListener {

            recyclerPosts.smoothScrollToPosition(0)
        }

        // BUSCAR
        navBuscar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UsersActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        // MENSAGENS
        navMensagens.setOnClickListener {

            Toast.makeText(
                this,
                "Mensagens em breve",
                Toast.LENGTH_SHORT
            ).show()
        }

        // PERFIL
        navEu.setOnClickListener {

            val intent = Intent(
                this,
                UserProfileActivity::class.java
            )

            intent.putExtra(
                "uid",
                auth.currentUser?.uid
            )

            startActivity(intent)

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private fun carregarPosts() {

        db.collection("posts")
            .addSnapshotListener { value, error ->

                if (error != null) {
                    return@addSnapshotListener
                }

                posts.clear()

                value?.documents?.forEach {

                    val post =
                        it.toObject(PostModel::class.java)

                    if (post != null) {

                        posts.add(post)
                    }
                }

                posts.reverse()

                adapter.notifyDataSetChanged()
            }
    }

    // ESCONDER BARRAS
    private fun esconderSistema() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            window.insetsController?.hide(
                WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars()
            )

            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        } else {

            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            esconderSistema()
        }
    }

    // VOLTAR COM ANIMAÇÃO
    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}