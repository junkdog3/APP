package com.example.myapplication

data class Country(
    val name: Name,
    val capital: List<String>? = null,
    val region: String = "",
    val population: Long = 0
)

data class Name(
    val common: String = ""
)