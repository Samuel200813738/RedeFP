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

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etEmail =
            findViewById<EditText>(R.id.etEmail)

        val etSenha =
            findViewById<EditText>(R.id.etSenha)

        val etConfirmarSenha =
            findViewById<EditText>(R.id.etConfirmarSenha)

        val btnCadastrar =
            findViewById<Button>(R.id.btnCadastrar)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        // VOLTAR
        btnVoltar.setOnClickListener {

            finish()

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        // CADASTRAR
        btnCadastrar.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            val senha =
                etSenha.text.toString().trim()

            val confirmarSenha =
                etConfirmarSenha.text.toString().trim()

            // CAMPOS VAZIOS
            if (
                email.isEmpty() ||
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

            // SENHAS DIFERENTES
            if (senha != confirmarSenha) {

                Toast.makeText(
                    this,
                    "As senhas não coincidem",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // FIREBASE AUTH
            auth.createUserWithEmailAndPassword(
                email,
                senha
            ).addOnCompleteListener {

                if (it.isSuccessful) {

                    val uid =
                        auth.currentUser!!.uid

                    // PERFIL INCOMPLETO
                    val user = hashMapOf(
                        "uid" to uid,
                        "email" to email,
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

                            // VOLTA LOGIN
                            startActivity(
                                Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                            )

                            // ANIMAÇÃO
                            overridePendingTransition(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                            )

                            finish()
                        }

                } else {

                    Toast.makeText(
                        this,
                        "Erro ao cadastrar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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