package com.example.ratina.sellahapp.Chat

import com.example.ratina.sellahapp.R
import com.example.ratina.sellahapp.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.my_message.view.*
import kotlinx.android.synthetic.main.other_message.view.*



class ChatFromItem(val text: String,  val user: User): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.txtMyMessage.text = text
  }

  override fun getLayout(): Int {
    return R.layout.my_message
  }
}

class ChatToItem(val text: String, val user: User): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.txtOtherMessage.text = text

  }

  override fun getLayout(): Int {
    return R.layout.other_message
  }

}