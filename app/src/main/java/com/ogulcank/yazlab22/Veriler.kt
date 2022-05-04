package com.ogulcank.yazlab22

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_veriler.*
import kotlinx.android.synthetic.main.activity_yonetim.*
import kotlinx.android.synthetic.main.recycler_row.*

class Veriler : AppCompatActivity() {

    private lateinit var database: FirebaseFirestore
    var duraklistesi=ArrayList<Station>()
    var duraklar=ArrayList<String>()
    val frequencyMap: MutableMap<String, Int> = HashMap()
    val lat= ArrayList<Double>()
    val lng=ArrayList<Double>()
    val Clat= ArrayList<Double>()
    val Clng=ArrayList<Double>()
    val drk= ArrayList<String>()
    val dlatlng: MutableMap<String, Int> = HashMap()
    var cost = Array(13) {Array(13) {(0).toDouble()} }

    var min=99999.0
    var mincost=0.0
    var a=0
    var b=0
    var u=0
    var v=0
    var n=0
    var ne=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veriler)

        //umuttepe
        lat.add(40.8244795)
        lng.add(29.9176296)

        //umuttepe


        lat.add(40.6790803)
        lat.add(40.8226049)
        lat.add(40.7623056)
        lat.add(40.7694607)
        lat.add(40.7850303)
        lat.add(40.7979269)
        lat.add(40.7057998)
        lat.add(41.0735862)
        lat.add(40.6852912)
        lat.add(40.735119)
        lat.add(40.7605767)
        lat.add(40.767004)
        lng.add(29.9084092)
        lng.add(29.3718977)
        lng.add(29.3846575)
        lng.add(29.8069667)
        lng.add(29.5390219)
        lng.add(29.4294765)
        lng.add(29.842377)
        lng.add(30.133313)
        lng.add(29.5744034)
        lng.add(30.0268664)
        lng.add( 29.724832)
        lng.add(29.9170591)
        dlatlng.put("Umuttepe",0)
        dlatlng.put("Başiskele",0)
        dlatlng.put("Çayırova",1)
        dlatlng.put("Darıca",2)
        dlatlng.put("Derince",3)
        dlatlng.put("Dilovası",4)
        dlatlng.put("Gebze",5)
        dlatlng.put("Gölcük",6)
        dlatlng.put("Kandıra",7)
        dlatlng.put("Karamürsel",8)
        dlatlng.put("Kartepe",9)
        dlatlng.put("Körfez",10)
        dlatlng.put("İzmit",11)


        var mesafeler:kotlin.Array<Float>
        val results = FloatArray(1)
      //  Location.distanceBetween(lat[3],lng[3],lat[2],lng[2],results)
         //  println(results[0])
                drk.addAll(resources.getStringArray(R.array.ilceler))
        for (d in drk){
            println(d)
        }

        database= FirebaseFirestore.getInstance()
        VerileriAl()

    }
    fun VerileriAl(){
        val dlistesi=Station("email","Umuttepe",1)
        duraklistesi.add(dlistesi)
        duraklar.add("Umuttepe")
        database.collection("Duraklar").addSnapshotListener{snapshot,exception ->
            if(exception!=null){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
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

        //  keyList : ArrayList<String>
            var  keyList = ArrayList(frequencyMap.keys)
            val valueList = ArrayList(frequencyMap.values)
            //Adapter
            val laymanager=LinearLayoutManager(this)
            recyclerView.layoutManager=laymanager
            val adapter=RecyclerAdapter(keyList,valueList)
            recyclerView.adapter=adapter
                println(frequencyMap)
            var i=0
            //umuttepe
            var d=0
            var tmp=keyList.indexOf("Umuttepe")
            keyList[tmp]=keyList[0]
            keyList[0]="Umuttepe"
            for(k in keyList){
                Clat.add(lat[dlatlng.get(k)!!.toInt()])
                Clng.add(lng[dlatlng.get(k)!!.toInt()])
                println(k+""+Clat[i]+" "+Clng[i])
            i++
            }
            i=0

            var j=0
            print("{")
            while(i<Clat.size){
             j=0
                while(j<Clat.size){
                    val results = FloatArray(1)
                      Location.distanceBetween(Clat[j],Clng[j],Clat[i],Clng[i],results)
                    cost[i][j]=(results[0]).toDouble()
                   if(cost[i][j]==0.0 ){
                       cost[i][j]= 999999.0
                   }
                    print("" +cost[i][j]+",")
                j++}
                println("")
            i++
            }
        n=Clat.size
            i=0
            var visited= arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

            while(i<n){

                println(""+i+"v "+visited[i]+""+visited.size+""+n)
                i++
            }


            visited[1] = 1
            var sayac1=0
            var sayac2=0
            n-=1
            while (ne < n) {
                i = 1
                min = 999999.0

                while (i <= n) {
                    j = 1
                    while (j <= n) {
                        if (cost[i][j] < min) {
                            if (visited[i] != 0) {
                                min = cost[i][j]
                                u = i
                                a = u
                                v = j
                                b = v
                                //printf("%d %d %d %d \n",u,a,v,b);
                            }
                        }
                        j++
                    }
                    i++
                }
                if (visited[u] == 0 || visited[v] == 0) {
                  //  printf("\n Edge %d:(%d %d) cost:%f", ne++, a, b, min)
                      println("Edge "+(ne++)+": "+a +" "+b+"cost: "+min)
                    println("lat "+Clat[a]+""+" lng "+Clng[a]+" "+""+keyList[a]+"dan  "+Clat[b]+""+Clng[b]+""+keyList[b])
                    //rota ekleme firebase e

                    mincost += min
                    visited[b] = 1
                }
                // printf("%d %d",b,a)
                cost[b][a] = 999999.0
                cost[a][b] = cost[b][a]
            }




        }




    }}
