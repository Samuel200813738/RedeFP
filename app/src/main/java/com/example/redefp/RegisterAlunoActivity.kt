package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterAlunoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_register_aluno)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etRA =
            findViewById<EditText>(R.id.etRA)

        val etSenha =
            findViewById<EditText>(R.id.etSenha)

        val etConfirmarSenha =
            findViewById<EditText>(R.id.etConfirmarSenha)

        val btnCadastrar =
            findViewById<Button>(R.id.btnCadastrarAluno)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        btnVoltar.setOnClickListener {

            finish()

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        btnCadastrar.setOnClickListener {

            val ra =
                etRA.text.toString().trim()

            val senha =
                etSenha.text.toString().trim()

            val confirmarSenha =
                etConfirmarSenha.text.toString().trim()

            if (
                ra.isEmpty() ||
                senha.isEmpty() ||
                confirmarSenha.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (senha != confirmarSenha) {

                Toast.makeText(
                    this,
                    "As senhas não coincidem",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (ra.length < 5) {

                Toast.makeText(
                    this,
                    "RA inválido",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val email =
                "${ra}sp@al.educacao.sp.gov.br"

            auth.createUserWithEmailAndPassword(
                email,
                senha
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid =
                        auth.currentUser!!.uid

                    val user = hashMapOf(

                        "uid" to uid,

                        "ra" to ra,

                        "email" to email,

                        "tipo" to "aluno",

                        "nome" to "",

                        "serie" to "",

                        "descricao" to "",

                        "avatarId" to "avatar_1",

                        "perfilCompleto" to false
                    )

                    db.collection("users")
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener {

                            Toast.makeText(
                                this,
                                "Conta criada!",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(
                                Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                            )

                            overridePendingTransition(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                            )

                            finish()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                this,
                                "Erro ao salvar usuário",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                } else {

                    val erro =
                        task.exception?.localizedMessage
                            ?: "Erro ao cadastrar"

                    Toast.makeText(
                        this,
                        erro,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}