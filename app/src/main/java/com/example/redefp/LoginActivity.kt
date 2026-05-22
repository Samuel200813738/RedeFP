package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        esconderSistema()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etEmail =
            findViewById<EditText>(R.id.etEmail)

        val etSenha =
            findViewById<EditText>(R.id.etSenha)

        val btnLogin =
            findViewById<Button>(R.id.btnLogin)

        val tvCadastrar =
            findViewById<TextView>(R.id.tvCadastrar)

        // LOGIN
        btnLogin.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            val senha =
                etSenha.text.toString().trim()

            if (
                email.isEmpty() ||
                senha.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                email,
                senha
            )
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val uid =
                            auth.currentUser!!.uid

                        db.collection("users")
                            .document(uid)
                            .get()
                            .addOnSuccessListener { document ->

                                val perfilCompleto =
                                    document.getBoolean(
                                        "perfilCompleto"
                                    ) ?: false

                                // PRIMEIRO LOGIN
                                if (!perfilCompleto) {

                                    startActivity(
                                        Intent(
                                            this,
                                            SetupAvatarActivity::class.java
                                        )
                                    )

                                } else {

                                    startActivity(
                                        Intent(
                                            this,
                                            FeedActivity::class.java
                                        )
                                    )
                                }

                                // ANIMAÇÃO
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )

                                finish()
                            }

                    } else {

                        Toast.makeText(
                            this,
                            "Erro no login",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // CADASTRO
        tvCadastrar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )

            // ANIMAÇÃO
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
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

    // VOLTAR COM ANIMAÇÃO
    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}