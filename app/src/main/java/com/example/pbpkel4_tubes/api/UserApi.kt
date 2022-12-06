package com.example.pbpkel4_tubes.api

class UserApi {
    companion object{
        val BASE_URL="https://www.tourtravel.itkitapro.com/"

        val GET_ALL_URL = BASE_URL + "users/"
        val GET_BY_ID_URL = BASE_URL + "userEmail/"
        val ADD_URL = BASE_URL + "users/"
        val UPDATE_URL = BASE_URL + "users/"
        val DELETE_URL = BASE_URL + "users/"

        val LOGIN_URL = BASE_URL + "login/"
    }
}