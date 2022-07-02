package com.Amikom.crud_sqlite_kotlin_biodata

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_student.*
import kotlinx.android.synthetic.main.item_student.view.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.intentFor

class DetailStudentActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var student: StudentContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_student)
        
        student = intent.getParcelableExtra<StudentContract>("student")
        Log.d("STUDENT", student.toString())

        dsa_tv_name.text = student.name
        dsa_tv_age.text = "Seemster ${student.semester}"
        dsa_tv_address.text = student.address
        dsa_iv_photo.setImageURI(Uri.parse(student.photo))
        dsa_tv_major.text = student.majority

        dsa_btn_delete.setOnClickListener(this)
        dsa_btn_update.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            dsa_btn_delete -> deleteData(student.id)
            dsa_btn_update -> intentFor<UpdateStudentActivity>("student" to student)
        }
    }

    private fun deleteData(id: Long?) {
        database.use {
            delete(StudentContract.TABLE_STUDENT, "${StudentContract.ID} = {id}", "id" to id!!.toInt())
        }
        finish()
    }


}
