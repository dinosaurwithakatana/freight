package io.dwak.freight.kotlinsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
  var router: Router by Delegates.notNull<Router>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val container = findViewById(R.id.container) as ViewGroup

    router = Conductor.attachRouter(this, container, savedInstanceState);
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(MainControllerBuilder()
                                                    .setMessage("message")
                                                    .setNumber(1)
                                                    .build()))
    }
  }
}
