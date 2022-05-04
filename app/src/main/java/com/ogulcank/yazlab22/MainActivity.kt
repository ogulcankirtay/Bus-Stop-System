package com.ogulcank.yazlab22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.net.IDN

class MainActivity : AppCompatActivity() {
private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()

        val guncelkullanici=auth.currentUser

        if(guncelkullanici!=null){
            val intent=Intent(this,kullanici::class.java)
            startActivity(intent)
            finish()
        }




}
    fun kgiris(view: View){
        auth.signInWithEmailAndPassword(editTextTextEmail.text.toString(),editTextTextPassword.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                val guncelkullanici=auth.currentUser?.email.toString()
                val intent = Intent(this,kullanici::class.java)
                startActivity(intent)
                Toast.makeText(this,"${guncelkullanici}", Toast.LENGTH_LONG).show();
                finish()
            }
        }.addOnFailureListener{
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
    fun Agiris(view: View){
       // buttonGiris.setOnClickListener{
            if(editTextTextEmail.text.isNullOrBlank () && editTextTextPassword.text.isNullOrBlank()){
                Toast.makeText(this,"eksik veri girişi", Toast.LENGTH_LONG).show();
            }else if(editTextTextEmail.text.toString()=="admin@gmail.com" && editTextTextPassword.text.toString()=="12345")
            {
                val intent = Intent(this,yonetim::class.java)
                startActivity(intent)
                Toast.makeText(this,"Admin giriş başarılı", Toast.LENGTH_LONG).show();
                finish()

            }
    }

}