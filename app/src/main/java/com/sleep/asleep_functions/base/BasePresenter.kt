package com.sleep.asleep_functions.base

interface BasePresenter<T> {
    fun createView(view: T)
    fun destroyView()
}