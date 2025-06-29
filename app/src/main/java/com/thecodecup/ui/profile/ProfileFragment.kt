package com.thecodecup.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thecodecup.R
import com.thecodecup.data.local.AppDatabase
import com.thecodecup.data.model.UserProfileEntity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = AppDatabase.getInstance(requireContext())
        val dao = db.userProfileDao()

        // Grab each item layout view
        val itemName = view.findViewById<View>(R.id.itemFullName)
        val itemPhone = view.findViewById<View>(R.id.itemPhone)
        val itemEmail = view.findViewById<View>(R.id.itemEmail)
        val itemAddress = view.findViewById<View>(R.id.itemAddress)

        // Set label cho từng dòng
        itemName.findViewById<TextView>(R.id.txtLabel).text = "Full Name"
        itemPhone.findViewById<TextView>(R.id.txtLabel).text = "Phone Number"
        itemEmail.findViewById<TextView>(R.id.txtLabel).text = "Email"
        itemAddress.findViewById<TextView>(R.id.txtLabel).text = "Address"

// Set icon cho từng dòng (đảm bảo các drawable này có sẵn trong res/drawable)
        itemName.findViewById<ImageView>(R.id.imgIcon).setImageResource(R.drawable.ic_user)
        itemPhone.findViewById<ImageView>(R.id.imgIcon).setImageResource(R.drawable.ic_phone)
        itemEmail.findViewById<ImageView>(R.id.imgIcon).setImageResource(R.drawable.ic_email)
        itemAddress.findViewById<ImageView>(R.id.imgIcon).setImageResource(R.drawable.ic_location)


        val nameText = itemName.findViewById<TextView>(R.id.txtValue)
        val nameEdit = itemName.findViewById<EditText>(R.id.editField)
        val nameEditBtn = itemName.findViewById<View>(R.id.btnEdit)

        val phoneText = itemPhone.findViewById<TextView>(R.id.txtValue)
        val phoneEdit = itemPhone.findViewById<EditText>(R.id.editField)
        val phoneEditBtn = itemPhone.findViewById<View>(R.id.btnEdit)

        val emailText = itemEmail.findViewById<TextView>(R.id.txtValue)
        val emailEdit = itemEmail.findViewById<EditText>(R.id.editField)
        val emailEditBtn = itemEmail.findViewById<View>(R.id.btnEdit)

        val addressText = itemAddress.findViewById<TextView>(R.id.txtValue)
        val addressEdit = itemAddress.findViewById<EditText>(R.id.editField)
        val addressEditBtn = itemAddress.findViewById<View>(R.id.btnEdit)

        val btnSave = view.findViewById<View>(R.id.btnSave)

        // Load data
        lifecycleScope.launch {
            val profile = dao.getProfile()
            profile?.let {
                nameText.text = it.name
                nameEdit.setText(it.name)

                phoneText.text = it.phone
                phoneEdit.setText(it.phone)

                emailText.text = it.email
                emailEdit.setText(it.email)

                addressText.text = it.address
                addressEdit.setText(it.address)
            }
        }

        // Toggling logic
        nameEditBtn.setOnClickListener {
            toggleEdit(nameText, nameEdit)
        }

        phoneEditBtn.setOnClickListener {
            toggleEdit(phoneText, phoneEdit)
        }

        emailEditBtn.setOnClickListener {
            toggleEdit(emailText, emailEdit)
        }

        addressEditBtn.setOnClickListener {
            toggleEdit(addressText, addressEdit)
        }

        btnSave.setOnClickListener {
            val newProfile = UserProfileEntity(
                name = nameEdit.text.toString(),
                phone = phoneEdit.text.toString(),
                email = emailEdit.text.toString(),
                address = addressEdit.text.toString()
            )

            lifecycleScope.launch {
                dao.saveProfile(newProfile)
                Toast.makeText(requireContext(), "Profile saved!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // quay lại home
            }
        }
    }

    private fun toggleEdit(textView: TextView, editText: EditText) {
        if (editText.visibility == View.GONE) {
            textView.visibility = View.GONE
            editText.visibility = View.VISIBLE
            editText.requestFocus()
        } else {
            textView.visibility = View.VISIBLE
            editText.visibility = View.GONE
        }
    }
}
