package com.example.redefp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroupFeedActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerAvisos: RecyclerView

    private lateinit var adapter: FeedAdapter

    private val posts = ArrayList<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_group_feed
        )

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val groupId =
            intent.getStringExtra("groupId")
                ?: ""

        val nomeTurma =
            intent.getStringExtra("nome")
                ?: ""

        findViewById<TextView>(R.id.tvNomeTurma)
            .text = nomeTurma

        val etAviso =
            findViewById<EditText>(R.id.etAviso)

        val btnPublicar =
            findViewById<Button>(R.id.btnPublicar)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        recyclerAvisos =
            findViewById(R.id.recyclerAvisos)

        adapter = FeedAdapter(posts) { post ->

            Toast.makeText(
                this,
                post.texto,
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerAvisos.layoutManager =
            LinearLayoutManager(this)

        recyclerAvisos.adapter = adapter

        carregarPosts(groupId)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnPublicar.setOnClickListener {

            val texto =
                etAviso.text.toString().trim()

            if (texto.isEmpty()) {

                Toast.makeText(
                    this,
                    "Digite um aviso",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val uid =
                auth.currentUser!!.uid

            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    val nome =
                        document.getString("nome")
                            ?: ""

                    val serie =
                        document.getString("serie")
                            ?: ""

                    val avatarId =
                        document.getString("avatarId")
                            ?: "ic_user"

                    val horario =
                        SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                        ).format(Date())

                    val post = hashMapOf(

                        "nome" to nome,
                        "serie" to serie,
                        "texto" to texto,
                        "horario" to horario,
                        "avatarId" to avatarId,
                        "groupId" to groupId
                    )

                    db.collection("groups")
                        .document(groupId)
                        .collection("posts")
                        .add(post)

                    etAviso.text.clear()
                }
        }
    }

    private fun carregarPosts(groupId: String) {

        db.collection("groups")
            .document(groupId)
            .collection("posts")
            .addSnapshotListener { value, _ ->

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
}