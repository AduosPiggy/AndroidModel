package com.example.androidmodel.base.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FullScreen(val value: Boolean = true)