package io.dwak.freight.kotlinsample

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import io.dwak.freight.annotation.ControllerBuilder
import io.dwak.freight.annotation.Extra

@ControllerBuilder
class MainController(bundle : Bundle) : Controller(bundle) {
  lateinit var message : String @Extra set
  var number : Int? = null @Nullable @Extra set

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    return inflater.inflate(R.layout.controller_main, container, false)
  }

}

