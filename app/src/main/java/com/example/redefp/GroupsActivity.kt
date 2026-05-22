package com.example.redefp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class GroupsActivity : AppCompatActivity() {

    private lateinit var recyclerGroups: RecyclerView

    private lateinit var adapter: GroupsAdapter

    private val lista =
        ArrayList<GroupModel>()

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(
            R.layout.activity_groups
        )

        db = FirebaseFirestore.getInstance()

        recyclerGroups =
            findViewById(R.id.recyclerGroups)

        val btnVoltar =
            findViewById<ImageButton>(R.id.btnVoltar)

        val btnCriarGrupo =
            findViewById<Button>(R.id.btnCriarGrupo)

        adapter = GroupsAdapter(lista)

        recyclerGroups.layoutManager =
            LinearLayoutManager(this)

        recyclerGroups.adapter =
            adapter

        carregarGrupos()

        // ANIMAÇÃO ENTRADA
        recyclerGroups.alpha = 0f
        recyclerGroups.translationY = 80f

        recyclerGroups.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()

        // VOLTAR
        btnVoltar.setOnClickListener {

            finish()

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        // CRIAR GRUPO
        btnCriarGrupo.setOnClickListener {

            criarGrupoTeste()

            btnCriarGrupo.animate()
                .rotationBy(360f)
                .setDuration(500)
                .start()
        }
    }

    private fun carregarGrupos() {

        db.collection("groups")
            .addSnapshotListener { value, _ ->

                lista.clear()

                value?.documents?.forEach {

                    val grupo =
                        it.toObject(GroupModel::class.java)

                    if (grupo != null) {

                        lista.add(grupo)
                    }
                }

                adapter.notifyDataSetChanged()
            }
    }

    private fun criarGrupoTeste() {

        val grupoId =
            db.collection("groups")
                .document().id

        val grupo = hashMapOf(

            "nome" to "3° Ano A",
            "professor" to "Carlos",
            "membros" to 34,
            "groupId" to grupoId
        )

        db.collection("groups")
            .document(grupoId)
            .set(grupo)
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