package com.example.dogtorapplication

data class HospitalModel(
    val id: Int,
    val title: String,
    val location: String,
    val tell: String,
    val lat: Double,
    val lng: Double,
    val imgUrl: String
)
