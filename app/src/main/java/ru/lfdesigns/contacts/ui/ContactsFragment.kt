package ru.lfdesigns.contacts.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_contacts.*
import ru.lfdesigns.contacts.R

import ru.lfdesigns.contacts.arch.ContactsViewModel
import ru.lfdesigns.contacts.arch.ContactsViewModelFactory
import ru.lfdesigns.contacts.model.*
import javax.inject.Inject
import ru.lfdesigns.contacts.arch.ContactsInteractor
import ru.lfdesigns.contacts.ui.adapter.ContactsAdapter

// TODO: search bar on toolbar
class ContactsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ContactsViewModelFactory

    private var viewModel: ContactsViewModel? = null

    @Inject
    lateinit var contactsInteractor: ContactsInteractor

    private lateinit var itemsAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contactsInteractor.navController = NavHostFragment.findNavController(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactsViewModel::class.java)
        viewModel?.let {
            it.contacts.observe(viewLifecycleOwner, Observer<PagedList<Contact>> { contacts ->
                clearLoadingStatus()
                itemsAdapter.submitList(contacts)
            })
            it.loadingStatus.observe(viewLifecycleOwner, Observer<LoadingStatus> { status ->
                handleLoadingStatusChanged(status)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer.setOnRefreshListener {
            viewModel?.refreshContacts()
        }

        listView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            itemsAdapter = ContactsAdapter()
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
            adapter = itemsAdapter
        }
        itemsAdapter.clickListener = {
            contactsInteractor.handleContactChoice(it.localId)
        }
    }

    private fun clearLoadingStatus() {
        swipeContainer.isRefreshing = false
        progressView.visibility = View.GONE
    }

    private fun handleLoadingStatusChanged(status: LoadingStatus) {
        if (status != LoadingStatus.LOADING)
            swipeContainer.isRefreshing = false
        progressView.visibility = View.GONE

        when (status) {
            LoadingStatus.LOADING -> {
                if (itemsAdapter.itemCount == 0)
                    progressView.visibility = View.VISIBLE
            }
            LoadingStatus.ERROR -> {
                view?.let {
                    Snackbar.make(it, R.string.error_generic, Snackbar.LENGTH_LONG)
                        .addCallback(object: Snackbar.Callback() {

                        override fun onDismissed(snackbar: Snackbar , event: Int) {
                            viewModel?.clearError()
                        }

                    }).show()
                }
            }
            else -> {
            }
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

}
