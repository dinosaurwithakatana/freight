package io.dwak.freight.internal;

/**
 * THIS IS FOR INTERNAL USE ONLY
 * @param <T>
 */
public interface IFreightTrain<T> {
  @SuppressWarnings("unused")
  void ship(T target);
}
