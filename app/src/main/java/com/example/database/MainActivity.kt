package com.example.database


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAdapter(list: List<User>) {
        mAdapter?.setData(list)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userList = AppDatabase(this@MainActivity).getUserDao().getAllUser()

            mAdapter = UserAdapter()
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = mAdapter
                setAdapter(userList)

                mAdapter?.setOnActionEditListener {
                    val intent = Intent(this@MainActivity, AddActivity::class.java)
                    intent.putExtra("Data", it)
                    startActivity(intent)
                }

                mAdapter?.setOnDeleteListener {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("YES") { p0, p1 ->
                        lifecycleScope.launch {
                            AppDatabase(this@MainActivity).getUserDao().deleteUser(it)
                            val list = AppDatabase(this@MainActivity).getUserDao().getAllUser()
                            setAdapter(list)
                            finish(finish())
                        }
                        p0.dismiss()
                    }
                    builder.setNegativeButton("NO") { p0, p1 ->
                        p0.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun finish(addUser: Unit) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}
