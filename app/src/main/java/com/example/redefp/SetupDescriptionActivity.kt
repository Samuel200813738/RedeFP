package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
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

class SetupDescriptionActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(
            R.layout.activity_setup_description
        )

        esconderSistema()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val etSerie =
            findViewById<EditText>(R.id.etSerie)

        val etDescricao =
            findViewById<EditText>(R.id.etDescricao)

        val btnContinuar =
            findViewById<Button>(R.id.btnContinuar)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        // CONTINUAR
        btnContinuar.setOnClickListener {

            val serie =
                etSerie.text.toString().trim()

            val descricao =
                etDescricao.text.toString().trim()

            if (
                serie.isEmpty() ||
                descricao.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val uid =
                auth.currentUser!!.uid

            db.collection("users")
                .document(uid)
                .update(
                    mapOf(
                        "serie" to serie,
                        "descricao" to descricao,
                        "perfilCompleto" to true
                    )
                )
                .addOnSuccessListener {

                    // IR PARA TELA FINAL
                    startActivity(
                        Intent(
                            this,
                            SetupFinishActivity::class.java
                        )
                    )

                    overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )

                    finish()
                }
        }

        // VOLTAR
        btnVoltar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SetupNameActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )

            finish()
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
}