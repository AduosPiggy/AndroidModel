package com.example.androidmodel.base.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class MultiContentLayout(val value: Int = -1)