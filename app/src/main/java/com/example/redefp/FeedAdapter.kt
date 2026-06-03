package com.example.redefp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FeedAdapter(
    private val listaPosts: List<PostModel>
) : RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        val tvTexto: TextView = itemView.findViewById(R.id.tvTexto)
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val imgPost: ImageView = itemView.findViewById(R.id.imgPost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = listaPosts[position]
        val context = holder.itemView.context

        // --- AJUSTE DA ALTURA DO CARD ---
        // Aumentamos para 380dp para o card não parecer "fino"
        val params = holder.itemView.layoutParams
        params.height = 120.dp(context)
        holder.itemView.layoutParams = params

        // Opcional: Define uma altura mínima para garantir que ele nunca encolha
        holder.itemView.minimumHeight = 380.dp(context)
        // --------------------------------

        holder.tvNome.text = post.nome
        holder.tvHorario.text = post.horario
        holder.tvTexto.text = post.texto

        // Avatar
        val avatarRes = context.resources.getIdentifier(post.avatarId, "drawable", context.packageName)
        holder.imgAvatar.setImageResource(if (avatarRes != 0) avatarRes else R.drawable.user_profile)

        // FOTO DO POST (Mantendo sua lógica original de imagem)
        if (post.imagemUrl.isNotEmpty()) {
            holder.imgPost.visibility = View.VISIBLE
            holder.imgPost.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            holder.imgPost.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            holder.imgPost.maxHeight = 250.dp(context) // Reduzi um pouco o max da imagem para sobrar espaço no card de 380
            holder.imgPost.adjustViewBounds = true
            holder.imgPost.scaleType = ImageView.ScaleType.FIT_CENTER

            Glide.with(context)
                .load(post.imagemUrl)
                .into(holder.imgPost)
        } else {
            holder.imgPost.visibility = View.GONE
        }

        // ANIMAÇÃO
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(300).start()
    }

    override fun getItemCount() = listaPosts.size

    private fun Int.dp(context: Context): Int =
        (this * context.resources.displayMetrics.density).toInt()
}