package com.example.expensify

data class User(
    val uid : String = "",
    val name : String = "",
    val email : String = "",
    val phone : String = "",
    val profileImageUrl : String = "",
    val createdAt : Long = System.currentTimeMillis()
)
