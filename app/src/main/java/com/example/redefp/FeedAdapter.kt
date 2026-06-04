package com.example.redefp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FeedAdapter(
    private val listaPosts: List<PostModel>,
    private val onPostClick: (PostModel) -> Unit
) : RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View)

        : RecyclerView.ViewHolder(itemView) {

        val tvNome: TextView =
            itemView.findViewById(R.id.tvNome)

        val tvHorario: TextView =
            itemView.findViewById(R.id.tvHorario)

        val tvTexto: TextView =
            itemView.findViewById(R.id.tvTexto)

        val imgAvatar: ImageView =
            itemView.findViewById(R.id.imgAvatar)

        val imgPost: ImageView =
            itemView.findViewById(R.id.imgPost)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_post,
                parent,
                false
            )

        return PostViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {

        val post = listaPosts[position]

        holder.tvNome.text =
            post.nome

        holder.tvHorario.text =
            "• ${post.horario}"

        holder.tvTexto.text =
            post.texto

        val context =
            holder.itemView.context

        val avatarResource =
            context.resources.getIdentifier(
                post.avatarId,
                "drawable",
                context.packageName
            )

        if (avatarResource != 0) {

            holder.imgAvatar.setImageResource(
                avatarResource
            )

        } else {

            holder.imgAvatar.setImageResource(
                R.drawable.user_profile
            )
        }

        // FOTO DO POST
        if (post.imagemUrl.isNotEmpty()) {

            holder.imgPost.visibility =
                View.VISIBLE

            Glide.with(context)
                .load(post.imagemUrl)
                .into(holder.imgPost)

        } else {

            holder.imgPost.visibility =
                View.GONE

            Glide.with(context)
                .clear(holder.imgPost)

            holder.imgPost.setImageDrawable(null)
        }

        // CLIQUE NO CARD
        holder.itemView.setOnClickListener {

            onPostClick(post)
        }

        // ANIMAÇÃO
        holder.itemView.alpha = 0f

        holder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    override fun getItemCount(): Int {

        return listaPosts.size
    }
}