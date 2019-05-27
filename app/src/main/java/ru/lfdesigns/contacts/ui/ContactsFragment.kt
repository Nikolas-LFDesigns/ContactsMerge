package ru.lfdesigns.contacts.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.content_contacts.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_bar.*
import ru.lfdesigns.contacts.R

import ru.lfdesigns.contacts.arch.ContactsViewModel
import ru.lfdesigns.contacts.arch.ContactsViewModelFactory
import ru.lfdesigns.contacts.model.*
import javax.inject.Inject
import ru.lfdesigns.contacts.ui.adapter.ContactsAdapter
import ru.lfdesigns.contacts.ui.coordinators.ContactsFlowCoordinator
import ru.lfdesigns.contacts.ui.coordinators.ContactsNavigator

class ContactsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ContactsViewModelFactory

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ContactsViewModel::class.java)
    }

    @Inject
    lateinit var coordinator: ContactsFlowCoordinator
    @Inject
    lateinit var navigator: ContactsNavigator

    private lateinit var itemsAdapter: ContactsAdapter

    private val subscribers = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.let {
            it.contacts.observe(viewLifecycleOwner, Observer<PagedList<Contact>> { contacts ->
                clearLoadingStatus()
                itemsAdapter.submitList(contacts)
            })
            it.loadingStatus.observe(viewLifecycleOwner, Observer<LoadingStatus> { status ->
                handleLoadingStatusChanged(status)
            })
            it.visualSearchTerm.observe(viewLifecycleOwner, Observer<String> { query ->
                itemsAdapter.searchTerm = query
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
            viewModel.refreshContacts()
        }

        listView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val termHighlighter = SearchTermHighlighter(ContextCompat.getColor(context, R.color.colorPrimary))
            itemsAdapter = ContactsAdapter(termHighlighter)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
            adapter = itemsAdapter
        }
        itemsAdapter.clickListener = {
            viewModel.showContactDetails(it.localId)
        }

        setUpSearchBar()
    }

    private fun setUpSearchBar() {
        subscribers.add(search_query.textChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                if (query.isNotEmpty()) {
                    button_clear.visibility = View.VISIBLE
                } else {
                    button_clear.visibility = View.GONE
                }

                viewModel.setSearchTerm(query)
            }
        )
        button_clear.setOnClickListener { search_query.setText("") }
    }

    private fun EditText.textChanged(): Flowable<String> {
        return Flowable.create({
            doAfterTextChanged { editable ->
                if (it.isCancelled) return@doAfterTextChanged;

                val query = editable.toString()
                it.onNext(query)
            }
        }, BackpressureStrategy.MISSING)
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
                            viewModel.clearError()
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
        coordinator.attachNavigator(navigator)
    }

    override fun onDetach() {
        coordinator.detachNavigator()
        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscribers.clear()
    }

}
