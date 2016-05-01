package io.dwak.freight.processor.extension

import javax.lang.model.element.Element

/**
 * Returns {@code true} if the an annotation is found on the given element with the given class
 * name (not fully qualified).
 */
fun Element.hasAnnotationWithName(simpleName: String): Boolean {
  annotationMirrors.forEach {
    val annotationElement = it.annotationType.asElement();
    val annotationName = annotationElement.simpleName.toString();
    if (simpleName.equals(annotationName)) {
      return true;
    }
  }
  return false;
}

