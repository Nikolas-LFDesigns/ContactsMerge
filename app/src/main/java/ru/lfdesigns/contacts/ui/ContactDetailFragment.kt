package ru.lfdesigns.contacts.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.contact.name
import kotlinx.android.synthetic.main.content_contact_detail.*
import kotlinx.android.synthetic.main.fragment_contact_detail.*

import ru.lfdesigns.contacts.R
import ru.lfdesigns.contacts.arch.ContactDetailViewModel
import ru.lfdesigns.contacts.arch.ContactDetailViewModelFactory
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.ui.coordinators.ContactDetailFlowCoordinator
import ru.lfdesigns.contacts.ui.coordinators.ContactDetailNavigator
import java.text.DateFormat
import javax.inject.Inject

private const val ARG_CONTACT_ID = "id"

class ContactDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ContactDetailViewModelFactory

    private var contactId: Int = 0

    private val viewModel: ContactDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ContactDetailViewModel::class.java)
    }

    @Inject
    lateinit var coordinator: ContactDetailFlowCoordinator
    @Inject
    lateinit var navigator: ContactDetailNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getInt(ARG_CONTACT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            viewModel.returnToContacts()
        }

        phone.setOnClickListener {
            viewModel.dialNumber(phone.text.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.let {
            it.contactDetail.observe(viewLifecycleOwner, Observer<Contact> { contact ->
                if (contact.isValid)
                    fillInFields(contact)
            })
            it.loadContactById(contactId)
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        coordinator.attachNavigator(navigator)
    }

    override fun onDetach() {
        coordinator.detachNavigator()
        super.onDetach()
    }

    @SuppressLint("SetTextI18n")
    private fun fillInFields(contact: Contact) {
        name.text = contact.name
        phone.text = contact.phone.toString()
        temperament.text = contact.temperament.toString()
        val formatter = DateFormat.getDateInstance(DateFormat.SHORT)
        educationPeriod.text = "${formatter.format(contact.educationPeriod.start)} - ${formatter.format(contact.educationPeriod.end)}"
        biography.text = contact.biography
    }
}
