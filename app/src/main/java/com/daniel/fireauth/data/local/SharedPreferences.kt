package com.daniel.fireauth.data.local

import android.content.Context

class SharedPreferences {

    fun insertUserData(context: Context, profileImage: String, username: String, email: String){
        val sharedPreferences = context.getSharedPreferences("user_data_shared_prefs", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString("profileImage", profileImage)
        edit.putString("username", username)
        edit.putString("email", email)
        edit.apply()
    }

    fun readUserData(context: Context): ArrayList<String>{
        val sharedPreferences = context.getSharedPreferences("user_data_shared_prefs", Context.MODE_PRIVATE)
        val profileImage = sharedPreferences.getString("profileImage", "").toString()
        val username = sharedPreferences.getString("username", "").toString()
        val email = sharedPreferences.getString("email", "").toString()
        return arrayListOf(profileImage, username, email)
    }

    fun deleteUserData(context: Context){
        val sharedPreferences = context.getSharedPreferences("user_data_shared_prefs", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.clear()
        edit.apply()
    }

}