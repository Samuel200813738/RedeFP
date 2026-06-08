package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsersActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var listUsers: ListView

    private val usuarios = ArrayList<String>()
    private val usersIds = ArrayList<String>()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FULLSCREEN
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_users)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        listUsers = findViewById(R.id.listUsers)



        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            usuarios
        )

        listUsers.adapter = adapter

        // ANIMAÇÃO LISTA
        val animacao =
            AnimationUtils.loadAnimation(
                this,
                R.anim.slide_in_right
            )

        listUsers.startAnimation(animacao)

        carregarUsuarios()


        // ABRIR PERFIL
        listUsers.setOnItemClickListener { _, _, position, _ ->

            val intent = Intent(
                this,
                UserProfileActivity::class.java
            )

            intent.putExtra(
                "uid",
                usersIds[position]
            )

            startActivity(intent)

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        // ENVIAR SOLICITAÇÃO
        listUsers.setOnItemLongClickListener { _, _, position, _ ->

            val meuUid = auth.currentUser!!.uid
            val amigoUid = usersIds[position]

            val solicitacao = hashMapOf(
                "de" to meuUid,
                "para" to amigoUid,
                "status" to "pendente"
            )

            db.collection("friend_requests")
                .add(solicitacao)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Solicitação enviada!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            true
        }
    }

    private fun carregarUsuarios() {

        val meuUid = auth.currentUser?.uid

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                usuarios.clear()
                usersIds.clear()

                for (document in result) {

                    val uid = document.getString("uid")

                    if (uid != meuUid) {

                        val nome = document.getString("nome")
                        val descricao = document.getString("descricao")

                        usuarios.add(
                            "👤 $nome\n📝 $descricao"
                        )

                        usersIds.add(uid!!)
                    }
                }

                adapter.notifyDataSetChanged()
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