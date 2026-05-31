package com.example.redefp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerPosts: RecyclerView

    private lateinit var navHome: LinearLayout
    private lateinit var navBuscar: LinearLayout
    private lateinit var navEu: LinearLayout
    private lateinit var btnNotificacao: ImageButton

    // VARIÁVEIS DAS ABAS
    private lateinit var tabParaVoce: TextView
    private lateinit var tabTurmas: TextView
    private lateinit var tabTudo: TextView
    private lateinit var listaDeAbas: List<TextView>

    // ÍCONES NAVBAR
    private lateinit var iconHome: ImageView
    private lateinit var iconBuscar: ImageView
    private lateinit var iconEu: ImageView

    // TEXTOS NAVBAR
    private lateinit var textHome: TextView
    private lateinit var textBuscar: TextView
    private lateinit var textEu: TextView

    // NOVOS
    private lateinit var etPost: EditText
    private lateinit var btnPostar: ImageButton

    private lateinit var btnImagem: ImageButton

    private var imagemSelecionada: Uri? = null

    private val PICK_IMAGE = 1001

    private val posts = ArrayList<PostModel>()

    private lateinit var adapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELA INTEIRA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        setContentView(R.layout.activity_feed)

        esconderSistema()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerPosts =
            findViewById(R.id.recyclerPosts)

        navHome =
            findViewById(R.id.navHome)

        navBuscar =
            findViewById(R.id.navBuscar)

        navEu =
            findViewById(R.id.navEu)

        btnNotificacao =
            findViewById(R.id.btnNotificacao)

        // INICIALIZAR ABAS
        tabParaVoce = findViewById(R.id.tabParaVoce)
        tabTurmas = findViewById(R.id.tabTurmas)
        tabTudo = findViewById(R.id.tabTudo)
        listaDeAbas = listOf(tabParaVoce, tabTurmas, tabTudo)

        // ÍCONES
        iconHome =
            findViewById(R.id.iconHome)

        iconBuscar =
            findViewById(R.id.iconBuscar)

        iconEu =
            findViewById(R.id.iconEu)

        // TEXTOS
        textHome =
            findViewById(R.id.textHome)

        textBuscar =
            findViewById(R.id.textBuscar)

        textEu =
            findViewById(R.id.textEu)

        // NOVOS
        etPost =
            findViewById(R.id.etPost)

        btnPostar =
            findViewById(R.id.btnPostar)

        btnImagem =
            findViewById(R.id.btnImagem)

        btnImagem.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(
                intent,
                PICK_IMAGE
            )
        }

        adapter = FeedAdapter(posts)

        recyclerPosts.layoutManager =
            LinearLayoutManager(this)

        recyclerPosts.adapter =
            adapter

        carregarPosts()

        // LÓGICA DE CLIQUE NAS ABAS SUPERIORES
        listaDeAbas.forEach { aba ->
            aba.setOnClickListener {
                atualizarEstiloAbas(it as TextView)
            }
        }

        // POSTAR
        btnPostar.setOnClickListener {

            val texto =
                etPost.text.toString().trim()

            val uid =
                auth.currentUser?.uid ?: return@setOnClickListener

            if (texto.isEmpty() && imagemSelecionada == null) {

                Toast.makeText(
                    this,
                    "Digite algo ou selecione uma imagem",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (imagemSelecionada != null) {

                uploadImagemCloudinary(imagemSelecionada!!) { imageUrl ->

                    val post = hashMapOf(

                        "uid" to uid,
                        "nome" to "samuel",
                        "texto" to texto,
                        "imagemUrl" to imageUrl,
                        "horario" to "Agora",
                        "avatarId" to "ic_user"
                    )

                    db.collection("posts")
                        .add(post)

                    etPost.text.clear()

                    imagemSelecionada = null

                    Toast.makeText(
                        this,
                        "Postado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                val post = hashMapOf(

                    "uid" to uid,
                    "nome" to "samuel",
                    "texto" to texto,
                    "imagemUrl" to "",
                    "horario" to "Agora",
                    "avatarId" to "ic_user"
                )

                db.collection("posts")
                    .add(post)

                etPost.text.clear()

                Toast.makeText(
                    this,
                    "Postado!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // NOTIFICAÇÕES TOPO
        btnNotificacao.setOnClickListener {

            Toast.makeText(
                this,
                "Notificações em breve",
                Toast.LENGTH_SHORT
            ).show()
        }

        fun resetIcons() {

            iconHome.setColorFilter(Color.WHITE)
            iconBuscar.setColorFilter(Color.WHITE)
            iconEu.setColorFilter(Color.WHITE)

            textHome.setTextColor(Color.WHITE)
            textBuscar.setTextColor(Color.WHITE)
            textEu.setTextColor(Color.WHITE)
        }

        // HOME
        navHome.setOnClickListener {

            resetIcons()

            iconHome.setColorFilter(
                Color.parseColor("#FFD600")
            )

            textHome.setTextColor(
                Color.parseColor("#FFD600")
            )

            recyclerPosts.smoothScrollToPosition(0)
        }

        // BUSCAR
        navBuscar.setOnClickListener {

            resetIcons()

            iconBuscar.setColorFilter(
                Color.parseColor("#FFD600")
            )

            textBuscar.setTextColor(
                Color.parseColor("#FFD600")
            )

            startActivity(
                Intent(
                    this,
                    UsersActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        // PERFIL
        navEu.setOnClickListener {

            resetIcons()

            iconEu.setColorFilter(
                Color.parseColor("#FFD600")
            )

            textEu.setTextColor(
                Color.parseColor("#FFD600")
            )

            val intent = Intent(
                this,
                UserProfileActivity::class.java
            )

            intent.putExtra(
                "uid",
                auth.currentUser?.uid
            )

            startActivity(intent)

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    // FUNÇÃO PARA ATUALIZAR O VISUAL DAS ABAS (PARA VOCÊ, TURMAS, TUDO)
    private fun atualizarEstiloAbas(abaSelecionada: TextView) {
        listaDeAbas.forEach { aba ->
            if (aba == abaSelecionada) {
                // Estilo Ativo: Amarelo, Negrito e Fundo Selecionado
                aba.setTextColor(Color.parseColor("#FFD600"))
                aba.setTypeface(null, Typeface.BOLD)
                aba.setBackgroundResource(R.drawable.tab_selected_bg)
            } else {
                // Estilo Inativo: Branco, Normal e Sem Fundo
                aba.setTextColor(Color.WHITE)
                aba.setTypeface(null, Typeface.NORMAL)
                aba.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    private fun carregarPosts() {

        db.collection("posts")
            .addSnapshotListener { value, error ->

                if (error != null) {
                    return@addSnapshotListener
                }

                posts.clear()

                value?.documents?.forEach {

                    val post =
                        it.toObject(PostModel::class.java)

                    if (post != null) {

                        posts.add(post)
                    }
                }

                posts.reverse()

                adapter.notifyDataSetChanged()
            }
    }

    // ESCONDER BARRAS
    private fun esconderSistema() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            window.insetsController?.hide(
                WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars()
            )

            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        } else {

            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            esconderSistema()
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == PICK_IMAGE &&
            resultCode == RESULT_OK &&
            data != null
        ) {

            imagemSelecionada = data.data

            Toast.makeText(
                this,
                "Imagem selecionada!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun uploadImagemCloudinary(
        uri: Uri,
        callback: (String) -> Unit
    ) {

        MediaManager.get()
            .upload(uri)
            .callback(object : UploadCallback {

                override fun onStart(requestId: String?) {}

                override fun onProgress(
                    requestId: String?,
                    bytes: Long,
                    totalBytes: Long
                ) {
                }

                override fun onSuccess(
                    requestId: String?,
                    resultData: MutableMap<Any?, Any?>?
                ) {

                    val url =
                        resultData?.get("secure_url")
                            .toString()

                    callback(url)
                }

                override fun onError(
                    requestId: String?,
                    error: com.cloudinary.android.callback.ErrorInfo?
                ) {

                    Toast.makeText(
                        this@FeedActivity,
                        "Erro ao enviar imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onReschedule(
                    requestId: String?,
                    error: com.cloudinary.android.callback.ErrorInfo?
                ) {
                }
            })
            .dispatch()
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