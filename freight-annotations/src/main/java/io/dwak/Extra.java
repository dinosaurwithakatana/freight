package io.dwak;

public @interface Extra {
  /**
   * This value will act as the key to access the property from the bundle
   * as well as the name for the method in the builder
   * @return the key/name to use
   */
  String value() default "";
}
