package com.ogulcank.yazlab22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_kullanici.*

class kullanici : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()

    var ilceler=resources.getStringArray(R.array.ilceler)
        if(spinner2!=null){
            val adapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,ilceler)
            spinner2.adapter= adapter
        }
        spinner2.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                textView.text=ilceler[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
    fun cikis(view:View){
        auth.signOut()
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun harita(view:View){

        val intent= Intent(this,MapsActivity::class.java)
        startActivity(intent)

    }
    //veritabanina ekleme
    fun ekle(view:View){
        var guncelk=auth.currentUser!!.email.toString()
        val durak=textView.text.toString()
        val durakHashmap= hashMapOf<String,Any>()
            durakHashmap.put("email",guncelk!!)
            durakHashmap.put("durak",durak)
        database.collection("Duraklar").add(durakHashmap).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this,"kayıt başarılı",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}