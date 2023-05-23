package com.example.googlemapapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.example.googlemapapp.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var googleMap: GoogleMap
    var loc = LatLng(37.554752, 126.970631)
    var arrLoc : ArrayList<LatLng> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
        initSpinner()
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayList<String>())
        adapter.add("Hybrid")
        adapter.add("Normal")
        adapter.add("Satellite")
        adapter.add("Terrain")
        binding.apply {
            spinner.adapter = adapter
            spinner.setSelection(1)
            spinner.onItemSelectedListener = object : OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position){
                        0 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        }
                        1 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        }
                        2 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        }
                        3 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        }

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    private fun initMap() {

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{
            // 구글 맵이 연결되면 호출되는 부분
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16f))
            googleMap.setMinZoomPreference(10f)
            googleMap.setMaxZoomPreference(18f)

            // Make Marker
            val option = MarkerOptions()
            option.position(loc)
            option.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
            option.title("역")
            option.snippet("서울역")
            googleMap.addMarker(option)?.showInfoWindow()
            
            googleMap.setOnMapClickListener {

                // Save current touch position
                arrLoc.add(it)

                googleMap.clear()

                // Make Marker
                val option2 = MarkerOptions()
                option2.position(it)
                option2.icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
                googleMap.addMarker(option2)

                /*
                // Draw a line connecting the points you touched
                val option3 = PolylineOptions().color(Color.GREEN).addAll(arrLoc)
                googleMap.addPolyline(option3)
                 */

                // Plot polygons connecting touched areas
                val option4 = PolygonOptions()
                    .fillColor(Color.argb(100, 255,255,0))
                    .strokeColor(Color.BLUE)
                    .addAll(arrLoc)
                googleMap.addPolygon(option4)

                // Set Marker
                for(location in arrLoc){
                    val op = MarkerOptions()
                    op.position(location)
                    op.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                    googleMap.addMarker(op)
                }

            }
        }




    }

}