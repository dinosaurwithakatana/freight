package io.dwak.freight.internal.annotation;

/**
 * For internal use only.
 *
 * This annotation is used by the linter to verify that required methods have been called
 */
public @interface HasRequiredMethods {
  /**
   * List of required methods
   * @return String array of required methods on the builder
   */
  String[] value();
}
