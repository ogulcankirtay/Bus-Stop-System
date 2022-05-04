package com.ogulcank.yazlab22


import android.content.Context
import android.graphics.Color
import android.location.Location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.ogulcank.yazlab22.databinding.ActivityMapsBinding












class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
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
        database= FirebaseFirestore.getInstance()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        dlatlng.put("Başiskele",1)
        dlatlng.put("Çayırova",2)
        dlatlng.put("Darıca",3)
        dlatlng.put("Derince",4)
        dlatlng.put("Dilovası",5)
        dlatlng.put("Gebze",6)
        dlatlng.put("Gölcük",7)
        dlatlng.put("Kandıra",8)
        dlatlng.put("Karamürsel",9)
        dlatlng.put("Kartepe",10)
        dlatlng.put("Körfez",11)
        dlatlng.put("İzmit",12)
        drk.addAll(resources.getStringArray(R.array.ilceler))
        for (d in drk){
            println(d)
        }
        //VerileriAl()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //veri al
        val dlistesi = Station("email", "Umuttepe", 1)
        duraklistesi.add(dlistesi)
        duraklar.add("Umuttepe")
        database.collection("Duraklar").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val doc = snapshot.documents
                        for (d in doc) {
                            val email = d.get("email") as String
                            val durak = d.get("durak") as String
                            val dlistesi = Station(email, durak, 1)
                            duraklistesi.add(dlistesi)
                            duraklar.add(durak)
                        }

                    }
                }
                frequencyMap.clear()
                //adet durak tutma
                for (d in duraklar) {
                    var count = frequencyMap[d]
                    if (count == null) count = 0
                    frequencyMap[d] = count + 1
                }
                duraklar.clear()
            }
            var keyList = ArrayList(frequencyMap.keys)
            val valueList = ArrayList(frequencyMap.values)
            //Adapter

            println(frequencyMap)
            var i = 0
            var tmp = keyList.indexOf("Umuttepe")
            keyList[tmp] = keyList[0]
            keyList[0] = "Umuttepe"
            for (k in keyList) {
                Clat.add(lat[dlatlng.get(k)!!.toInt()])
                Clng.add(lng[dlatlng.get(k)!!.toInt()])
                println(k + "" + Clat[i] + " " + Clng[i])
                val sydney = LatLng(Clat[i], Clng[i])
                mMap.addMarker(
                    MarkerOptions().position(sydney).title(k + " " + frequencyMap[k] + " yolcu")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                i++
            }
            //shortest path
            i = 0

            var j = 0
            print("{")
            while (i < Clat.size) {
                j = 0
                while (j < Clat.size) {
                    val results = FloatArray(1)
                    Location.distanceBetween(Clat[j], Clng[j], Clat[i], Clng[i], results)
                    cost[i][j] = (results[0]).toDouble()
                    if (cost[i][j] == 0.0) {
                        cost[i][j] = 999999.0
                    }
                    print("" + cost[i][j] + ",")
                    j++
                }
                println("")
                i++
            }
            n = Clat.size
            i = 0
            var visited = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

            while (i < n) {

                println("" + i + "v " + visited[i] + "" + visited.size + "" + n)
                i++
            }


            visited[1] = 1
            var sayac1 = 0
            var sayac2 = 0
            n -= 1
            var s=0
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
                    println("Edge " + (ne++) + ": " + a + " " + b + "cost: " + min)
                    println("lat " + Clat[a] + "" + " lng " + Clng[a] + " " + "" + keyList[a] + "dan  " + Clat[b] + "" + Clng[b] + "" + keyList[b])
                    //rota ekleme firebase e

                    if (s == 0) {
                        val polyline1 = googleMap.addPolyline(
                            PolylineOptions()
                                .clickable(true)
                                .add(
                                    LatLng(Clat[0], Clng[0]),
                                    LatLng(Clat[a], Clng[a])
                                )
                        )
                        s++
                    }

                    val polyline1 = googleMap.addPolyline(
                        PolylineOptions()
                            .clickable(true)
                            .add(
                                LatLng(Clat[a], Clng[a]),
                                LatLng(Clat[b], Clng[b])
                            )
                    )

                    mincost += min
                    visited[b] = 1
                }
                // printf("%d %d",b,a)
                cost[b][a] = 999999.0
                cost[a][b] = cost[b][a]
            }
            //shortest path

        }


    }


}