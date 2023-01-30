package com.example.dogtorapplication

data class userInformation(
    var userID : String? = null, // 파이어베이스에 의해 알아서 할당되는 고유 값
    var userEmail : String? = null // email

) {
}