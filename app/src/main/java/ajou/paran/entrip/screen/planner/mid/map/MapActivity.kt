package ajou.paran.entrip.screen.planner.mid.map

import ajou.paran.entrip.databinding.ActivityMapBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMapBinding

    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var place_name : String
    private lateinit var road_address_name : String
    private lateinit var longitude_intent : String
    private lateinit var latitude_intent : String

    /**
     *  intent data들
     */
    private var isUpdate : Boolean = false
    private var update_id : Long = -1L
    private var todo : String? = null
    private var location : String? = null
    private var rgb : Int = -1
    private lateinit var date : String
    private var planner_id : Long = -1L
    private lateinit var selectedPlanner : PlannerEntity
    private var time : Int = -1
    private var last_select_pallete : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getIntentData()
        init()
    }

    fun init(){
        val isSearch = intent.getBooleanExtra("isSearch", false)
        if(isSearch){
            place_name = intent.getStringExtra("place_name").toString()
            road_address_name = intent.getStringExtra("road_address_name").toString()
            longitude_intent = intent.getStringExtra("longitude").toString()
            latitude_intent = intent.getStringExtra("latitude").toString()

            val uLatitude = latitude_intent.toDouble()
            val uLongitude =longitude_intent.toDouble()
            val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
            binding.kakaoMap.setMapCenterPoint(uNowPosition, true)

            val marker = MapPOIItem()
            marker.itemName = place_name
            marker.mapPoint = uNowPosition
            marker.markerType = MapPOIItem.MarkerType.BluePin
            binding.kakaoMap.addPOIItem(marker)

            binding.viewInformation.visibility = View.VISIBLE
            binding.viewInformation.isClickable = true

            binding.tvPlaceName.text = place_name
            binding.tvRoadAddress.text = road_address_name
        }else{
            if(permissionCheck()){
                initCoordinate()
            }else{
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }
        }
    }

    fun onClick(v : View?){
        v?.let{
            when(it.id){
                binding.etSearchKeyword.id -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.apply{
                        this.putExtra("isUpdate", isUpdate)
                        this.putExtra("Id", update_id)
                        this.putExtra("Todo",todo)
                        this.putExtra("Rgb",rgb)
                        this.putExtra("Time",time)
                        this.putExtra("Location",location)
                        this.putExtra("date", date)
                        this.putExtra("plannerId", planner_id)
                        this.putExtra("PlannerEntity", selectedPlanner)
                        this.putExtra("last_select_pallete",last_select_pallete)
                    }
                    startActivity(intent)
                    finish()
                }

                binding.viewInformation.id -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("위치로 저장하시겠습니까?")
                        .setNegativeButton("아니오",
                            DialogInterface.OnClickListener{ dialog, id ->
                                Log.d("TEST", "취소")
                            })
                        .setPositiveButton("네",
                            DialogInterface.OnClickListener{ dialog, id ->
                                val intent = Intent(this, InputActivity::class.java)
                                intent.apply {
                                    this.putExtra("isUpdate", isUpdate)
                                    this.putExtra("Id", update_id)
                                    this.putExtra("Todo",todo)
                                    this.putExtra("Rgb",rgb)
                                    this.putExtra("Time",time)
                                    this.putExtra("Location",road_address_name)
                                    this.putExtra("date", date)
                                    this.putExtra("plannerId", planner_id)
                                    this.putExtra("PlannerEntity", selectedPlanner)
                                }
                                startActivity(intent)
                                finish()
                            })
                    builder.show()
                }

                binding.imgBackInput.id -> {
                    val intent = Intent(this, InputActivity::class.java)
                    intent.apply {
                        this.putExtra("isUpdate", isUpdate)
                        this.putExtra("Id", update_id)
                        this.putExtra("Todo",todo)
                        this.putExtra("Rgb",rgb)
                        this.putExtra("Time",time)
                        this.putExtra("Location",location)
                        this.putExtra("date", date)
                        this.putExtra("plannerId", planner_id)
                        this.putExtra("PlannerEntity", selectedPlanner)
                        this.putExtra("last_select_pallete",last_select_pallete)
                    }
                    startActivity(intent)
                    finish()
                }

                else -> {}
            }
        }
    }

    private fun permissionCheck() : Boolean{
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return (permissionCheck == PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("MissingPermission")
    private fun initCoordinate(){
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val userNowLocation: Location? =
                lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val uLatitude = userNowLocation?.latitude
            val uLongitude = userNowLocation?.longitude
            val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
            binding.kakaoMap.setMapCenterPoint(uNowPosition, true)
        }catch(e: NullPointerException){
            Log.e("LOCATION_ERROR", e.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.finishAffinity(this)
            }else{
                ActivityCompat.finishAffinity(this)
            }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            System.exit(0)
        }
    }

    /**
     *    InputActivity로 돌아갈때 그대로 넘겨줘야 하는 데이터
     *    -> 처음에 넘겨준 데이터를 모두 읽어옴
     **/
    fun getIntentData(){
        isUpdate = intent.getBooleanExtra("isUpdate", true)
        update_id = intent.getLongExtra("Id", -1)
        todo = intent.getStringExtra("Todo")
        location = intent.getStringExtra("Location")
        rgb = intent.getIntExtra("Rgb", Color.parseColor("#cfc1c1"))
        date = intent.getStringExtra("date").toString()
        planner_id = intent.getLongExtra("plannerId", -1)
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        time = intent.getIntExtra("Time", -1)
        last_select_pallete = intent.getIntExtra("last_select_pallete",0)
    }
}