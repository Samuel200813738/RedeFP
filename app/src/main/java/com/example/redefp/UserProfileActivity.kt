package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var tvNome: TextView
    private lateinit var tvTurma: TextView
    private lateinit var tvDescricao: TextView
    private lateinit var listPosts: ListView

    private val posts = ArrayList<String>()

    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_user_profile)

        esconderSistema()

        db = FirebaseFirestore.getInstance()

        tvNome =
            findViewById(R.id.tvNome)

        tvTurma =
            findViewById(R.id.tvTurma)

        tvDescricao =
            findViewById(R.id.tvDescricao)

        listPosts =
            findViewById(R.id.listPosts)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        // NOVA SETA
        btnVoltar.setImageResource(
            R.drawable.ic_seta_voltar
        )

        btnVoltar.setBackgroundColor(
            Color.TRANSPARENT
        )

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            posts
        )

        listPosts.adapter = adapter

        // ANIMAÇÕES
        val animDireita =
            AnimationUtils.loadAnimation(
                this,
                R.anim.slide_in_right
            )

        val animFade =
            AnimationUtils.loadAnimation(
                this,
                R.anim.fade_in
            )

        tvNome.startAnimation(animDireita)
        tvTurma.startAnimation(animFade)
        tvDescricao.startAnimation(animFade)
        listPosts.startAnimation(animDireita)

        val uid =
            intent.getStringExtra("uid")

        if (uid != null) {

            carregarPerfil(uid)

            carregarPosts(uid)
        }

        // VOLTAR
        btnVoltar.setOnClickListener {

            finish()

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        // ABRIR POST
        listPosts.setOnItemClickListener { _, _, _, _ ->

            val intent = Intent(
                this,
                FeedActivity::class.java
            )

            startActivity(intent)

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private fun carregarPerfil(uid: String) {

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                val nome =
                    document.getString("nome")

                val serie =
                    document.getString("serie")

                val descricao =
                    document.getString("descricao")

                val tipo =
                    document.getString("tipo")

                val disciplina =
                    document.getString("disciplina")

                tvNome.text =
                    nome ?: "Usuário"

                if (tipo == "professor") {

                    tvTurma.visibility =
                        android.view.View.VISIBLE

                    tvTurma.text =
                        disciplina ?: "Disciplina não informada"

                } else {

                    tvTurma.visibility =
                        android.view.View.VISIBLE

                    tvTurma.text =
                        serie ?: "Série não informada"
                }

                tvDescricao.text =
                    descricao ?: "Sem descrição"
            }


    }


    private fun carregarPosts(uid: String) {

        db.collection("posts")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->

                posts.clear()

                for (document in result) {

                    val texto =
                        document.getString("texto")

                    val horario =
                        document.getString("horario")

                    posts.add(
                        "🕒 $horario\n\n$texto"
                    )
                }

                adapter.notifyDataSetChanged()
            }
    }

    // ESCONDER BARRAS
    private fun esconderSistema() {

        val controller = WindowInsetsControllerCompat(
            window,
            window.decorView
        )

        controller.hide(
            WindowInsetsCompat.Type.systemBars()
        )

        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            esconderSistema()
        }
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
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