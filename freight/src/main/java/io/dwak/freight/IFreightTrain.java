package io.dwak.freight;

public interface IFreightTrain<T> {
  @SuppressWarnings("unused")
  void ship(T target);
}
