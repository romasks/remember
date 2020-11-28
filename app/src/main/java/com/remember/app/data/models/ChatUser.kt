package com.remember.app.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatUser(
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("location")
    val location: String?, // Минск
    @SerializedName("login")
    val login: String, // Помню
    @SerializedName("name")
    val name: String, // Pomnyu
    @SerializedName("picture")
    val picture: String?, // /uploads/accounts/1.jpg
    @SerializedName("surname")
    val surname: String, // Рус
    @SerializedName("thirdname")
    val thirdname: String // Дмитриевна
)  : Parcelable