package com.example.redefp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var etNome: EditText
    private lateinit var etDescricao: EditText
    private lateinit var btnSalvar: Button

    private var avatarSelecionado = "avatar_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etNome = findViewById(R.id.etNome)
        etDescricao = findViewById(R.id.etDescricao)
        btnSalvar = findViewById(R.id.btnSalvar)

        val uid = auth.currentUser!!.uid

        val avatar1 = findViewById<ImageView>(R.id.avatar1)
        val avatar2 = findViewById<ImageView>(R.id.avatar2)
        val avatar3 = findViewById<ImageView>(R.id.avatar3)


        avatar1.setOnClickListener { avatarSelecionado = "avatar_1" }
        avatar2.setOnClickListener { avatarSelecionado = "avatar_2" }
        avatar3.setOnClickListener { avatarSelecionado = "avatar_3" }


        db.collection("users").document(uid).get()
            .addOnSuccessListener {

                etNome.setText(it.getString("nome"))
                etDescricao.setText(it.getString("descricao"))
                avatarSelecionado = it.getString("avatarId") ?: "avatar_1"
            }

        btnSalvar.setOnClickListener {

            val data = hashMapOf(
                "nome" to etNome.text.toString(),
                "descricao" to etDescricao.text.toString(),
                "avatarId" to avatarSelecionado,
                "uid" to uid
            )

            db.collection("users")
                .document(uid)
                .set(data)

            Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}