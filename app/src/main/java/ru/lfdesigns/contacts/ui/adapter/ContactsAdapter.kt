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
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import androidx.core.content.res.ResourcesCompat


class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ContactsAdapter: PagedListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactsDiffCallback()) {

    var searchTerm: String? = null
      set(value) {
          field = value
          notifyDataSetChanged()
      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.searchTerm = searchTerm
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
                    nameView.text = highlightSearchTerm(it.name, true)
                    numberView.text = highlightSearchTerm(it.phone.toString())
                    heightView.text = it.height.toString()
                }
            }

        private fun findTerm(term: String, inText: String, uninterrupted: Boolean): MatchResult? {
            var regexTerm = "("
            if (uninterrupted) {
                regexTerm += term
            } else {
                term.forEachIndexed { i, item ->
                    regexTerm += item
                    if (i < term.length - 1)
                        regexTerm += "[\\)\\- ]*"
                }
            }
            regexTerm += ")"
            val regex = regexTerm.toRegex(RegexOption.IGNORE_CASE)
            return regex.find(inText,0)
        }

        /**
         *
         * @param inText text to highlight the search term in
         * @param uninterrupted an optimization to use less computations if the term is guaranteed
         * to be uninterrupted by special symbols etc
         */
        private fun highlightSearchTerm(inText: String, uninterrupted: Boolean = false): SpannableString {
            val text = SpannableString(inText)
            return searchTerm?.let { term ->
                if (term.isEmpty())
                    return@let text

                val match = findTerm(term, inText, uninterrupted) ?: return@let text

                val span = ForegroundColorSpan(
                    ResourcesCompat.getColor(
                        nameView.resources,
                        R.color.colorPrimary,
                        null
                    )
                )

                text.apply {
                    setSpan(span,
                        match.range.first,
                        match.range.last+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }?: text
        }

        var searchTerm: String? = null
    }

}