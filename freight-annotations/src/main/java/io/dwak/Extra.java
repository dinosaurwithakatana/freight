package io.dwak;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Extra {
  /**
   * This value will act as the key to access the property from the bundle
   * as well as the name for the method in the builder
   * @return the key/name to use
   */
  String value() default "";
}
