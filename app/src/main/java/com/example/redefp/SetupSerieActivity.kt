package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetupSerieActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_setup_serie)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val tvTitulo =
            findViewById<TextView>(R.id.tvTitulo)

        val tvSubtitulo =
            findViewById<TextView>(R.id.tvSubtitulo)

        val etCampo =
            findViewById<EditText>(R.id.etCampo)

        val btnContinuar =
            findViewById<Button>(R.id.btnContinuar)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        val tipo =
            intent.getStringExtra("tipo") ?: "aluno"

        if (tipo == "professor") {

            tvTitulo.text =
                "Qual sua disciplina ou cargo?"

            tvSubtitulo.text =
                "Informe sua disciplina ou função."

            etCampo.hint =
                "Informática"

        } else {

            tvTitulo.text =
                "Qual sua série?"

            tvSubtitulo.text =
                "Informe sua série."

            etCampo.hint =
                "3°A"
        }

        btnContinuar.setOnClickListener {

            val valor =
                etCampo.text.toString().trim()

            if (valor.isEmpty()) {

                Toast.makeText(
                    this,
                    "Preencha o campo",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val uid =
                auth.currentUser!!.uid

            val campo =
                if (tipo == "professor")
                    "disciplina"
                else
                    "serie"

            db.collection("users")
                .document(uid)
                .set(
                    mapOf(
                        campo to valor
                    ),
                    com.google.firebase.firestore.SetOptions.merge()
                )
                .addOnSuccessListener {

                    val intent =
                        Intent(
                            this,
                            SetupDescriptionActivity::class.java
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

        btnVoltar.setOnClickListener {

            finish()

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }

}
