package com.sleep.asleep_functions.view.Main.Presenter

import com.sleep.asleep_functions.base.BasePresenter
import com.sleep.asleep_functions.base.BaseView

interface MainContract {
    interface View: BaseView {

    }

    interface Presenter: BasePresenter<View>{

    }
}