package io.dwak.freight.processor.model

import javax.lang.model.element.ElementKind
import javax.lang.model.type.TypeMirror

interface Binding {
  val name: String
  val type: TypeMirror
  val kind: ElementKind
}