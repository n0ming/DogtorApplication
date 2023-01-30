package com.example.dogtorapplication

data class mydogIndformation (
    var userID : String? = null, // 파이어베이스에 의해 알아서 할당되는 고유 값
    var userName : String? = null,
    var sickName : String? =null,
    var sickday : String? =null,
    var hospital : String? =null,
    var medicine : String? =null,
    var docId : String? =null,
    var date : String? =null,
    var dogName : String? = null

) {
}