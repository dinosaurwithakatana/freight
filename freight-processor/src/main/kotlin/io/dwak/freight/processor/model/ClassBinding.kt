package io.dwak.freight.processor.model

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.type.TypeMirror

class ClassBinding(element: Element): Binding {
  override val name: String
  override val type: TypeMirror
  override val kind: ElementKind

  init {
    name = element.simpleName.toString()
    kind = element.kind
    type = element.asType()
  }
}