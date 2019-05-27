package ru.lfdesigns.contacts.depend

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class ClassKey(val value: KClass<out Fragment>)