package io.dwak.freight.processor.model

import io.dwak.freight.annotation.ControllerBuilder
import io.dwak.freight.annotation.Extra
import io.dwak.freight.processor.extension.hasAnnotationWithName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.type.TypeMirror

class ClassBinding(element : Element) : Binding {
  override val name : String
  override val type : TypeMirror
  override val kind : ElementKind
  val screenName : String
  val scopeName : String
  val enclosedExtras = arrayListOf<FieldBinding>()

  init {
    name = element.simpleName.toString()
    kind = element.kind
    type = element.asType()
    val instance = element.getAnnotation(ControllerBuilder::class.java)
    screenName = instance.value
    scopeName = instance.scope

    element.enclosedElements
            .filter { it.hasAnnotationWithName(Extra::class.java.simpleName) }
            .map(::FieldBinding)
            .forEach { enclosedExtras.add(it) }
  }
}