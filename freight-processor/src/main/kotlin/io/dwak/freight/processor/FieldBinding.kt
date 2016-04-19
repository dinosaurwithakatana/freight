package io.dwak.freight.processor

import io.dwak.Extra
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

class FieldBinding(element: Element) {
  internal val name: String
  internal val type: TypeMirror
  internal var key: String
  internal var builderMethodName: String

  init {
    val instance = element.getAnnotation(Extra::class.java)
    name = element.simpleName.toString()
    type = element.asType()
    if (instance.value.isNotEmpty()) {
      key = instance.value
      builderMethodName = key
    }
    else {
      key = name.toUpperCase()
      builderMethodName = name
    }

  }

}