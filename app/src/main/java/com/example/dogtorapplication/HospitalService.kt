package com.example.dogtorapplication

import retrofit2.Call
import retrofit2.http.GET

interface HospitalService {
    @GET("/v3/eb7b5a7c-21ae-42a8-9bd8-4a7d6a9c277d")
    fun getHospitalList(): Call<HospitalDto>
}