package io.dwak.freight.processor.model

import io.dwak.Extra
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class FieldBinding(element: Element) {
  internal val name: String
  internal val type: TypeMirror
  internal val key: String
  internal val builderMethodName: String
  internal val kind: ElementKind

  init {
    val instance = element.getAnnotation(Extra::class.java)
    kind = element.kind
    name = element.simpleName.toString()

    type = if (kind == ElementKind.FIELD) {
      element.asType()
    }
    else {
      (element as ExecutableElement).parameters[0].asType()
    }

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