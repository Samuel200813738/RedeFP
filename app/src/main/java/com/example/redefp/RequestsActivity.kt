package com.example.redefp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequestsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var listRequests: ListView

    private val requests = ArrayList<String>()
    private val requestIds = ArrayList<String>()
    private val senderIds = ArrayList<String>()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        listRequests = findViewById(R.id.listRequests)

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            requests
        )

        listRequests.adapter = adapter

        carregarSolicitacoes()

        listRequests.setOnItemClickListener { _, _, position, _ ->

            val meuUid = auth.currentUser!!.uid
            val amigoUid = senderIds[position]
            val requestId = requestIds[position]

            val amizade1 = hashMapOf(
                "amigo" to amigoUid
            )

            val amizade2 = hashMapOf(
                "amigo" to meuUid
            )

            db.collection("friends")
                .document(meuUid)
                .collection("meus_amigos")
                .document(amigoUid)
                .set(amizade1)

            db.collection("friends")
                .document(amigoUid)
                .collection("meus_amigos")
                .document(meuUid)
                .set(amizade2)

            db.collection("friend_requests")
                .document(requestId)
                .delete()

            Toast.makeText(
                this,
                "Amizade aceita!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun carregarSolicitacoes() {

        val meuUid = auth.currentUser!!.uid

        db.collection("friend_requests")
            .whereEqualTo("para", meuUid)
            .get()
            .addOnSuccessListener { result ->

                requests.clear()
                requestIds.clear()
                senderIds.clear()

                for (document in result) {

                    val deUid = document.getString("de")

                    db.collection("users")
                        .document(deUid!!)
                        .get()
                        .addOnSuccessListener { userDoc ->

                            val nome = userDoc.getString("nome")
                            val descricao = userDoc.getString("descricao")

                            requests.add(
                                "👤 $nome\n📝 $descricao"
                            )

                            requestIds.add(document.id)
                            senderIds.add(deUid)

                            adapter.notifyDataSetChanged()
                        }
                }
            }
    }
}