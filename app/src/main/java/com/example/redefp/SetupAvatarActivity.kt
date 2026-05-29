package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetupAvatarActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var avatarSelecionado = "avatar_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_setup_avatar)

        esconderSistema()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val avatar1 =
            findViewById<ImageView>(R.id.avatar1)

        val avatar2 =
            findViewById<ImageView>(R.id.avatar2)

        val avatar3 =
            findViewById<ImageView>(R.id.avatar3)

        val btnContinuar =
            findViewById<Button>(R.id.btnContinuar)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        // NOVA SETA
        btnVoltar.setImageResource(
            R.drawable.ic_seta_voltar
        )

        btnVoltar.setBackgroundColor(
            Color.TRANSPARENT
        )

        // SELEÇÃO
        avatar1.setOnClickListener {

            avatarSelecionado = "avatar_1"

            avatar1.alpha = 1f
            avatar2.alpha = 0.5f
            avatar3.alpha = 0.5f
        }

        avatar2.setOnClickListener {

            avatarSelecionado = "avatar_2"

            avatar1.alpha = 0.5f
            avatar2.alpha = 1f
            avatar3.alpha = 0.5f
        }

        avatar3.setOnClickListener {

            avatarSelecionado = "avatar_3"

            avatar1.alpha = 0.5f
            avatar2.alpha = 0.5f
            avatar3.alpha = 1f
        }

        // CONTINUAR
        btnContinuar.setOnClickListener {

            val uid = auth.currentUser!!.uid

            db.collection("users")
                .document(uid)
                .update(
                    "avatarId",
                    avatarSelecionado
                )

            startActivity(
                Intent(
                    this,
                    SetupNameActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        // VOLTAR
        btnVoltar.setOnClickListener {

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