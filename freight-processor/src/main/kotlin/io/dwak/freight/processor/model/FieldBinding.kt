package io.dwak.freight.processor.model

import io.dwak.Extra
import io.dwak.freight.processor.extension.hasAnnotationWithName
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
  internal val isRequired: Boolean

  init {
    val extraInstance = element.getAnnotation(Extra::class.java)
    kind = element.kind
    name = element.simpleName.toString()

    type = if (kind == ElementKind.FIELD) {
      element.asType()
    }
    else {
      (element as ExecutableElement).parameters[0].asType()
    }

    if (extraInstance.value.isNotEmpty()) {
      key = extraInstance.value
      builderMethodName = key
    }
    else {
      key = name.toUpperCase()
      builderMethodName = name
    }

    isRequired = !element.hasAnnotationWithName("Nullable")
  }

  override fun toString(): String {
    @Suppress("ConvertToStringTemplate")
    return "FieldBinding(builderMethodName='$builderMethodName', " +
            "name='$name', " +
            "type=$type, " +
            "key='$key', " +
            "kind=$kind, " +
            "isRequired=$isRequired)"
  }


}