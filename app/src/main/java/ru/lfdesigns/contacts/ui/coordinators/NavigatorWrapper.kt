package ru.lfdesigns.contacts.ui.coordinators

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.lfdesigns.contacts.depend.ContactsScope

open class NavigatorWrapper : LifecycleObserver {

    protected lateinit var navigator: NavController
    private var fragment: Fragment? = null

    fun attach(fragment: Fragment) {
        this.fragment = fragment
        fragment.lifecycle.addObserver(this)
    }

    fun detach() {
        this.fragment?.lifecycle?.removeObserver(this)
        this.fragment = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onStart(){
        fragment?.let {
            navigator = NavHostFragment.findNavController(it)
        }
    }
}