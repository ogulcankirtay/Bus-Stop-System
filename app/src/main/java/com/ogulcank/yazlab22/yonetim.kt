package com.ogulcank.yazlab22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_kullanici.*
import kotlinx.android.synthetic.main.activity_yonetim.*
import java.util.*
import kotlin.collections.ArrayList

class yonetim : AppCompatActivity() {

    val frequencyMap: MutableMap<String, Int> = HashMap()
    private lateinit var database:FirebaseFirestore
    var duraklistesi=ArrayList<Station>()
    var duraklar=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yonetim)
        database= FirebaseFirestore.getInstance()
       var ilceler=resources.getStringArray(R.array.ilceler)
       if(spinner!=null){
            val adapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,ilceler)
            spinner.adapter=adapter
            }
        spinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                textViewSehir.text=ilceler[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    VerileriAl()
    }
    fun harita(view:View){
        val intent=Intent(this,MapsActivity::class.java)
        startActivity(intent)

    }
    fun Cikis(view:View){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun ekle(view:View){


        val durakHashmap= hashMapOf<String,Any>()
        durakHashmap.put("email","admin@gmail.com")
        durakHashmap.put("durak",textViewSehir.text.toString())
        val adet=Integer.parseInt(editTextSayi.text.toString())
        var i=0
        while(i<adet){
        database.collection("Duraklar").add(durakHashmap).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this,"kayıt başarılı",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
        i++
        }
    }
    fun VerileriAl() {

        database.collection("Duraklar").addSnapshotListener{snapshot,exception ->
            if(exception!=null){
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!=null){
                    if(!snapshot.isEmpty){
                    val doc=snapshot.documents
                        for(d in doc ){
                            val email=d.get("email") as String
                            val durak=d.get("durak") as String
                            val dlistesi=Station(email,durak,1)
                            duraklistesi.add(dlistesi)
                            duraklar.add(durak)
                        }

                    }
                }
                frequencyMap.clear()
                //adet durak tutma
                for(d in duraklar ){
                    var count = frequencyMap[d]
                    if (count == null) count = 0
                    frequencyMap[d] = count + 1
            }
                duraklar.clear()
            }
            println(frequencyMap)
        }




    }
    fun Goruntule(view:View){
        var intent=Intent(this,Veriler::class.java)
        startActivity(intent)
    }
}