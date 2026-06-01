package com.example.redefp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EscolhaCadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_escolha_cadastro)

        val btnEstudante =
            findViewById<Button>(R.id.btnEstudante)

        val btnSistema =
            findViewById<Button>(R.id.btnSistema)

        btnEstudante.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterAlunoActivity::class.java
                )
            )
        }

        btnSistema.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterProfessorActivity::class.java
                )
            )
        }
    }
}