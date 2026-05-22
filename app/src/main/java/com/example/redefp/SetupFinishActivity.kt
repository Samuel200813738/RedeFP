package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetupFinishActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ImageView
    private lateinit var tvNome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(
            R.layout.activity_setup_finish
        )

        esconderSistema()

        imgAvatar =
            findViewById(R.id.imgAvatar)

        tvNome =
            findViewById(R.id.tvNome)

        val currentUser =
            FirebaseAuth.getInstance().currentUser

        // SEGURANÇA
        if (currentUser == null) {

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
            return
        }

        val uid = currentUser.uid

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                val nome =
                    document.getString("nome")
                        ?: "Usuário"

                tvNome.text = nome

                // AVATAR
                val avatarId =
                    document.getString("avatarId")

                if (!avatarId.isNullOrEmpty()) {

                    val avatarResource =
                        resources.getIdentifier(
                            avatarId,
                            "drawable",
                            packageName
                        )

                    if (avatarResource != 0) {

                        imgAvatar.setImageResource(
                            avatarResource
                        )
                    }
                }
            }

        // TIMER
        Handler(Looper.getMainLooper())
            .postDelayed({

                startActivity(
                    Intent(
                        this,
                        FeedActivity::class.java
                    )
                )

                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )

                finish()

            }, 3000)
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
}