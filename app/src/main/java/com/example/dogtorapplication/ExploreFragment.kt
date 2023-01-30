package com.example.dogtorapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.list_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExploreFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_explore, container, false)

        val viewPager: ViewPager2 = view.findViewById(R.id.houseViewPager)
        val mapView: MapView = view.findViewById(R.id.mapView)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val currentLocationButton: LocationButtonView =
            view.findViewById(R.id.currentLocationButton)
        val bottomSheetTitleTextView: TextView = view.findViewById(R.id.bottomSheetTitleTextView)


        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)

        viewPager.adapter = viewPagerAdapter
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.activity)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedHospitalModel = viewPagerAdapter.currentList[position]
                val cameraUpdate =
                    CameraUpdate.scrollTo(
                        LatLng(
                            selectedHospitalModel.lat,
                            selectedHospitalModel.lng
                        )
                    )
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }
        })

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return view
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource // 위치 반환

    private val marker1 = Marker()
    private val marker2 = Marker()
    private val marker3 = Marker()

    private val infoWindow1 = InfoWindow()
    private val infoWindow2 = InfoWindow()

    private val viewPagerAdapter = HospitalViewPagerAdapter(itemClicked = {
        val intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "[서울여대 부근 동물병원] ${it.title} ${it.location} 사진보기 : ${it.imgUrl}"
                )
                type = "text/plain"
            }
        startActivity(Intent.createChooser(intent, null))
    })

    private val recyclerAdapter = HospitalListAdapter()
    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 확대, 축소 정도 조절
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497885, 127.027512))
        naverMap.moveCamera(cameraUpdate)

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false
        currentLocationButton.map = naverMap

        locationSource = FusedLocationSource(this@ExploreFragment, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        getHospitalListFromAPI()
    }

    private fun getHospitalListFromAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HospitalService::class.java).also {
            it.getHospitalList()
                .enqueue(object : Callback<HospitalDto> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<HospitalDto>,
                        response: Response<HospitalDto>
                    ) {
                        if (response.isSuccessful.not()) {
                            return
                        }
                        response.body()?.let { dto ->
                            updateMarker(dto.items)
                            viewPagerAdapter.submitList(dto.items)
                            recyclerAdapter.submitList(dto.items)

                            bottomSheetTitleTextView.text = "${dto.items.size}개의 동물병원이 검색되었습니다."
                        }
                    }

                    // 실패 처리리
                    override fun onFailure(call: Call<HospitalDto>, t: Throwable) {
                    }
                })
        }
    }

    // 지도에 마커 표시하기
    private fun updateMarker(hospital: List<HospitalModel>) {
        hospital.forEach { hospital ->
            val marker = Marker()
            marker.position = LatLng(hospital.lat, hospital.lng)
            marker.onClickListener = this

            marker.map = naverMap
            marker.tag = hospital.id
            marker.icon = OverlayImage.fromResource(R.drawable.map_arrow)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                // 권한 설정 거부시 위치 추적 사용하지 않음
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mapView.onDestroy()
    }

    // 메모리가 별로 없을 때 호출됨
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    // 지도 위 마커 클릭 시 해당하는 item의 viewPager로 이동, overlay: 마커
    override fun onClick(overly: Overlay): Boolean {
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overly.tag
        }

        selectedModel?.let {
            val viewPager = houseViewPager
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }
        return true
    }

}