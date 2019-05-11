package ru.lfdesigns.contacts.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lfdesigns.contacts.R
import ru.lfdesigns.contacts.model.Contact


class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ContactsAdapter: PagedListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.item = item
        holder.clickListener = clickListener
    }

    var clickListener: ((Contact) -> Unit)? = null

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val nameView: TextView = itemView.findViewById(R.id.name)
        private val numberView: TextView = itemView.findViewById(R.id.number)
        private val heightView: TextView = itemView.findViewById(R.id.height)

        init {
            itemView.setOnClickListener {
                clickListener?.invoke(item!!)
            }
        }

        var clickListener: ((Contact) -> Unit)? = null

        private var _item: Contact? = null
        var item: Contact?
            get() = _item
            set(value) {
                _item = value
                _item?.let {
                    nameView.text = it.name
                    numberView.text = it.phone
                    heightView.text = it.height.toString()
                }
            }
    }

}