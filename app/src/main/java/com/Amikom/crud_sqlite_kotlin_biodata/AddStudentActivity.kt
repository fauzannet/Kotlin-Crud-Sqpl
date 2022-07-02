package com.Amikom.crud_sqlite_kotlin_biodata

import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_student.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.io.*
import java.util.*


class AddStudentActivity : AppCompatActivity() {

    var GalleryRequestCode :Int = 100
    private var imageUri:Uri? = null
    private val IMAGE_DIRECTORY = "ProfileImages"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)


        asa_tv_choose_study.onClick {
            val jurusan = listOf("S1 INFORMATIKA","S1 SISTEM INFORMASI","S1 TEKNOLOGI INFORMASI", "S1 TEKNIK KOMPUTER","D3 MANAJEMEN INFORMATIKA","D3 TEKNIK INFORMATIKA")
            selector("Pilih jurusan",jurusan) { dialog, i ->
                asa_tv_major.setText(jurusan[i])
            }
        }

        button_image.onClick {
            OpenGallery()
        }

        asa_btn_save.onClick {
            if (!validation()){
                return@onClick
            }
            insertDatabase()
            finish()
        }

    }

    private fun OpenGallery() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            var GalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(GalleryIntent,GalleryRequestCode)
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),GalleryRequestCode)
        }
    }

    private fun insertDatabase() {
        database.use {
            insert(
                StudentContract.TABLE_STUDENT,
                StudentContract.NAME to asa_edt_name.text.toString(),
                StudentContract.SEMESTER to asa_edt_age.text.toString().toInt(),
                StudentContract.ADDRESS to asa_edt_address.text.toString(),
                StudentContract.PHOTO to imageUri.toString(),
                StudentContract.MAJORITY to asa_tv_major.text.toString()
            )

            toast("Berhasil menambah data mahasiswa baru")
        }
    }

    private fun validation(): Boolean{
        when {
            asa_edt_name.text.toString().isBlank() -> {
                asa_edt_name.requestFocus()
                asa_edt_name.error = "Tidak boleh kosong"
                return false
            }
            asa_edt_age.text.toString().isBlank() -> {
                asa_edt_age.requestFocus()
                asa_edt_age.error = "Tidak boleh kosong"
                return false
            }
            asa_edt_address.text.toString().isBlank() -> {
                asa_edt_address.requestFocus()
                asa_edt_address.error = "Tidak boleh kosong"
                return false
            }
            asa_tv_major.text.toString().isBlank() -> {
                asa_tv_major.requestFocus()
                asa_tv_major.error = "Tidak boleh kosong"
                return false
            }
            else -> return true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            GalleryRequestCode->
            {
                if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    OpenGallery()
                }
                else
                {
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//            GalleryRequestCode -> {
//                if (resultCode == RESULT_OK) {
//                    imageUri = data!!.data
//                    var Image = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
//                    asa_iv_photo.setImageBitmap(Image)
//                }
//            }
            GalleryRequestCode -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        val contentUrl = data.data
                        try {
                            val selectedImageBitmap =
                                MediaStore.Images.Media.getBitmap(this.contentResolver, contentUrl)
                            asa_iv_photo.setImageBitmap(selectedImageBitmap)

                            imageUri = saveImageToInternalStorage(selectedImageBitmap)
                            Log.i("img", "$imageUri")

                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(this, "image Loading error", Toast.LENGTH_LONG)
                        }
                    }
                }
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) :Uri{
        val contextWrapper = ContextWrapper(applicationContext)
        var file = contextWrapper.getDir(IMAGE_DIRECTORY,Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try {
            val stream :OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)

            stream.flush()
            stream.close()
        }catch (e : IOException){

        }
        return Uri.parse(file.absolutePath)
    }

}
