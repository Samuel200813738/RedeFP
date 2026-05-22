package com.example.redefp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupsAdapter(
    private val grupos: List<GroupModel>
) : RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val tvNome: TextView =
            itemView.findViewById(R.id.tvNomeGrupo)

        val tvProfessor: TextView =
            itemView.findViewById(R.id.tvProfessor)

        val tvMembros: TextView =
            itemView.findViewById(R.id.tvMembros)

        val btnEntrar: Button =
            itemView.findViewById(R.id.btnEntrar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_group,
                parent,
                false
            )

        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GroupViewHolder,
        position: Int
    ) {

        val grupo = grupos[position]

        holder.tvNome.text =
            grupo.nome

        holder.tvProfessor.text =
            "Professor: ${grupo.professor}"

        holder.tvMembros.text =
            "${grupo.membros} membros"

        // ANIMAÇÃO
        holder.itemView.alpha = 0f

        holder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()

        // ENTRAR NA TURMA
        holder.btnEntrar.setOnClickListener {

            val context =
                holder.itemView.context

            val uid =
                FirebaseAuth
                    .getInstance()
                    .currentUser!!.uid

            FirebaseFirestore.getInstance()
                .collection("groups")
                .document(grupo.groupId)
                .collection("members")
                .document(uid)
                .set(
                    hashMapOf(
                        "uid" to uid
                    )
                )

            Toast.makeText(
                context,
                "Entrando na turma...",
                Toast.LENGTH_SHORT
            ).show()

            val intent =
                Intent(
                    context,
                    GroupFeedActivity::class.java
                )

            intent.putExtra(
                "groupId",
                grupo.groupId
            )

            intent.putExtra(
                "nome",
                grupo.nome
            )

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return grupos.size
    }
}