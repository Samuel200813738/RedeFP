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
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetupNameActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(
            R.layout.activity_setup_name
        )

        esconderSistema()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val etNome =
            findViewById<EditText>(R.id.etNome)

        val btnContinuar =
            findViewById<Button>(R.id.btnContinuar)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        btnVoltar.setImageResource(
            R.drawable.ic_seta_voltar
        )

        btnVoltar.setBackgroundColor(
            Color.TRANSPARENT
        )

        btnContinuar.setOnClickListener {

            val nome =
                etNome.text.toString().trim()

            if (nome.isEmpty()) {

                Toast.makeText(
                    this,
                    "Digite seu nome",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val uid =
                auth.currentUser!!.uid

            db.collection("users")
                .document(uid)
                .update(
                    "nome",
                    nome
                )
                .addOnSuccessListener {

                    db.collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { document ->

                            val tipo =
                                document.getString("tipo")
                                    ?: "aluno"

                            val intent = Intent(
                                this,
                                SetupSerieActivity::class.java
                            )

                            intent.putExtra(
                                "tipo",
                                tipo
                            )

                            startActivity(intent)

                            overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            )

                            finish()
                        }
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Erro ao salvar nome",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        btnVoltar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SetupAvatarActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )

            finish()
        }
    }

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

}
