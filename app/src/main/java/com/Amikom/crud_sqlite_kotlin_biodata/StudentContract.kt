package com.Amikom.crud_sqlite_kotlin_biodata
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentContract(
    val id: Long?,
    val name: String,
    val semester: Int,
    val address: String,
    val photo: String,
    val majority: String
) : Parcelable {
    companion object{
        const val TABLE_STUDENT = "table_student"
        const val ID = "id"
        const val NAME = "name"
        const val SEMESTER = "age"
        const val ADDRESS = "address"
        const val PHOTO = "photo"
        const val MAJORITY = "majority"
    }
}