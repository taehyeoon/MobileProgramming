package com.example.getcurlocbygmap

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.getcurlocbygmap.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var googleMap: GoogleMap
    var loc = LatLng(37.554752, 126.970631)

    lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION)

    // 사용자가 GPS를 허용하는지 안하는지에 대한 콜백을 받는 launcher
    val gpsSettingLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(checkGPSProvider()){
                getLastLocation()
            }else{
                setCurrentLocation(loc)
            }
        }

    val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){permissions->
            when{
                // 정확한 위치 허용, 대략적인 위치 허용 둘중에 하나만 허용되 있는 경우 현재 위치를 가져
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false)->
                    getLastLocation()

                else->{
                    setCurrentLocation(loc)
                }
            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{
            googleMap = it
            initLocation()
        }
    }

    // fusedLocationProviderClient 객체 초기화
    private fun initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }
    // 현재 위치를 가지고 옴
    private fun getLastLocation() {
        when{
            // 1. FindLoc 권한이 허용된 경우( 정확한 위치 허용 )
            checkFineLocationPermission()-> {
                // GPS가 꺼져있는 경우
                if(!checkGPSProvider()){
                    // GPS 설정 화면으로 이동
                    showGPSSetting()
                }else{

                    // /*
                    // 현재 위치를 가지고 오는 경우
                    fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                        object : CancellationToken(){
                            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                                return CancellationTokenSource().token
                            }

                            override fun isCancellationRequested(): Boolean {
                                return false
                            }
                        }).addOnSuccessListener {
                    if(it!=null){
                        loc = LatLng(it.latitude, it.longitude)
                    }
                        setCurrentLocation(loc)
                    }
                    // */

                    /*

                    // 최근 위치를 가지고 오는 경우
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        if(it!=null){
                            loc = LatLng(it.latitude, it.longitude)
                        }
                        setCurrentLocation(loc)
                    }
                     */

                }
            }





            // 2. CoarseLoc 권한이 허용된 경우 ( 대략적인 위치 정보 허용 ) // GPS 사용 x
            checkCoarseLocationPermission() ->{
                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    object : CancellationToken(){
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                            return CancellationTokenSource().token
                        }

                        override fun isCancellationRequested(): Boolean {
                            return false
                        }
                    }).addOnSuccessListener {
                    if(it!=null){
                        loc = LatLng(it.latitude, it.longitude)
                    }
                    setCurrentLocation(loc)
                }
            }





            // 3. 명시적으로 사용자가 권한을 거부한 경우
            ActivityCompat.shouldShowRequestPermissionRationale(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)->{

                showPermissionRequestDlg()
            }

            
            
            
            
            
            // 4. 아예 앱을 처음 실행했을 때 실행되는 부분
            else->{
                locationPermissionRequest.launch(permissions)
            }
        }
    }

    // 사용자가 명시적으로 권한을 거부한 경우, 설정창 활성화
    private fun showPermissionRequestDlg() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 제공")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" +
                    "위치 설정을 허용하시겠습니까?"
        )
        builder.setPositiveButton("설정", DialogInterface.OnClickListener{dialog, i ->
            locationPermissionRequest.launch(permissions)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener{dialog, i ->
            dialog.dismiss()
            setCurrentLocation(loc)
        })
        builder.create().show()
    }

    // GPS 설정창 활성화
    private fun showGPSSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 활성화")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" +
                    "위치 설정을 허용하시겠습니까?"
        )
        builder.setPositiveButton("설정", DialogInterface.OnClickListener{dialog, i ->
            val GpsSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            gpsSettingLauncher.launch(GpsSettingIntent)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener{dialog, i ->
            dialog.dismiss()
            setCurrentLocation(loc)
        })
        builder.create().show()
    }




    // 입력된 loc에 marker하나 생성
    private fun setCurrentLocation(loc: LatLng) {
        val option = MarkerOptions()
        option.position(loc)
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        googleMap.addMarker(option)
        // moveCamera 이동만 가능한 지도, animateCamera 회전도 가능한 지
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16f))
    }

    private fun checkFineLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarseLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkGPSProvider(): Boolean {
        // GPS가 활성화 되어있는지 확인하는 함
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }
}