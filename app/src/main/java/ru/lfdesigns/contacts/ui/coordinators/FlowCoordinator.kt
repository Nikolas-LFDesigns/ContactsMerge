package ru.lfdesigns.contacts.ui.coordinators

import androidx.annotation.CallSuper

/**
 * Coordinates view navigation
 *
 * For concrete implementation to work it needs an attached navigator @see [NavigatorWrapper]
 * so it needs to be attached/detached on a corresponding View lifecycle events
 */
abstract class FlowCoordinator<T: NavigatorWrapper>  {

    protected var navigator: T? = null

    @CallSuper
    open fun attachNavigator(navigator: T) {
        this.navigator = navigator
        navigator.attach()
    }

    @CallSuper
    open fun detachNavigator() {
        navigator?.detach()
        this.navigator = null
    }

}