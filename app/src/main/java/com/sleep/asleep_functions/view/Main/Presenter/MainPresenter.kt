package com.sleep.asleep_functions.view.Main.Presenter

class MainPresenter: MainContract.Presenter {

    private var mainView: MainContract.View? = null

    override fun createView(view: MainContract.View) {
        mainView = view
    }

    override fun destroyView() {
        mainView = null
    }

}