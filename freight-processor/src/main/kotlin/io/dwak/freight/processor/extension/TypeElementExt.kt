package io.dwak.freight.processor.extension

import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

fun TypeElement.packageName(elementUtils : Elements) =
  elementUtils.getPackageOf(this).qualifiedName.toString()

fun TypeElement.className(packageName: String) : String {
  val packageLen = packageName.length + 1
  return this.qualifiedName.toString().substring(packageLen).replace('.', '$')
}


