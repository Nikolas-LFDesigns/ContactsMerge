package ru.lfdesigns.contacts.ui.coordinators

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Wraps a navigation pattern which historically belongs to View.
 * Since it deals with temporary objects with lifecycle, it needs to be initialized/deinitialized
 * with an attach/detach pattern, using corresponding [Fragment] methods [Fragment.onAttach] and [Fragment.onDetach]
 */
open class NavigatorWrapper(private val fragment: Fragment) : INavigatorWrapper, LifecycleObserver {

    protected lateinit var navigator: NavController

    override fun attach() {
        fragment.lifecycle.addObserver(this)
    }

    override fun detach() {
        this.fragment.lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        navigator = NavHostFragment.findNavController(fragment)
    }
}