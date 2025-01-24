package com.example.chatit

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class EmailAdapter(
    context: Context,
    private val emailList: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, emailList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val emailTextView = view.findViewById<TextView>(android.R.id.text1)

        // Extract the part before '@' from the email and set it as the text
        val email = emailList[position]
        val userName = email.substringBefore('@') // Get the part before '@'
        emailTextView.text = userName // Set the user name to the TextView

        return view
    }
}
