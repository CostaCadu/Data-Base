package com.example.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.database.databinding.ActivityAddBinding
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddBinding
    private var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)



        user = intent.getSerializableExtra("Data") as? User

        if(user == null) binding.btnAddOrUser.text = "Add User"
        else {
            binding.btnAddOrUser.text = "Update"
            binding.tvFirstName.setText(user?.firstName.toString())
            binding.tvLastName.setText(user?.lastName.toString())
        }
        binding.btnAddOrUser.setOnClickListener { addUser() }
    }

    private fun addUser() {
        val firstName = binding.tvFirstName.text.toString()
        val lastName = binding.tvLastName.text.toString()


        lifecycleScope.launch {
            if(user == null){
                val user = User(firstName = firstName, lastName = lastName)
                AppDatabase(this@AddActivity).getUserDao().addUser(user)
                finish()
            }else {
                val u = User(firstName, lastName)
                u.id = user?.id ?: 0
                AppDatabase(this@AddActivity).getUserDao().updateUser(u)
                finish()
            }

        }
    }
}