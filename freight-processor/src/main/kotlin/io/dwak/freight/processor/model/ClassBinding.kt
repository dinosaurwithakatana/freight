package io.dwak.freight.processor.model

import io.dwak.freight.annotation.ControllerBuilder
import io.dwak.freight.annotation.Extra
import io.dwak.freight.processor.extension.hasAnnotationWithName
import io.dwak.freight.processor.extension.packageName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

class ClassBinding(element: Element) : Binding {
  override val name: String
  override val type: TypeMirror
  override val kind: ElementKind
  val screenName: String
  val scopeName: String
  val enclosedExtras = arrayListOf<FieldBinding>()
  val popChangeHandler: TypeMirror?
  val pushChangeHandler: TypeMirror?

  init {
    name = element.simpleName.toString()
    kind = element.kind
    type = element.asType()
    val instance = element.getAnnotation(ControllerBuilder::class.java)
    screenName = instance.value
    scopeName = instance.scope
    popChangeHandler = getPopChangeHandler(instance)
    pushChangeHandler = getPushChangeHandler(instance)

    element.enclosedElements
            .filter { it.hasAnnotationWithName(Extra::class.java.simpleName) }
            .map(::FieldBinding)
            .forEach { enclosedExtras.add(it) }
  }

  companion object {
    fun getPopChangeHandler(annotation: ControllerBuilder): TypeMirror? {
      try {
        annotation.popChangeHandler // this should throw
      } catch (mte: MirroredTypeException) {
        return mte.typeMirror
      }
      return null
    }

    fun getPushChangeHandler(annotation: ControllerBuilder): TypeMirror? {
      try {
        annotation.pushChangeHandler // this should throw
      } catch (mte: MirroredTypeException) {
        return mte.typeMirror
      }
      return null
    }
  }

}