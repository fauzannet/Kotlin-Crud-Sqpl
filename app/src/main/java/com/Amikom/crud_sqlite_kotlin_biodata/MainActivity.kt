package com.Amikom.crud_sqlite_kotlin_biodata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    var adapter: StudentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = getListDataStudent()

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = StudentAdapter(this, list)

        main_rv_list_data.layoutManager = layoutManager
        main_rv_list_data.adapter = adapter

        main_cv_add_data.onClick {
            startActivity(intentFor<AddStudentActivity>())
        }
    }

    private fun getListDataStudent(): List<StudentContract> {
        var listData: List<StudentContract>? = null
        database.use {
            val result = select(StudentContract.TABLE_STUDENT)
            listData = result.parseList(classParser<StudentContract>())
        }
        return listData!!
    }

    override fun onResume() {
        super.onResume()
        val listRefresh = getListDataStudent()
        adapter = StudentAdapter(this, listRefresh)
        adapter?.notifyDataSetChanged()
        main_rv_list_data.adapter = adapter
    }
}
