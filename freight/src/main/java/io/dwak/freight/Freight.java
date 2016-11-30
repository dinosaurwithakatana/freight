package io.dwak.freight;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

import io.dwak.freight.annotation.ControllerBuilder;
import io.dwak.freight.internal.IFreightTrain;

public final class Freight {
  private static final String TAG = "Freight";
  private static final String SUFFIX = "$$FreightTrain";
  public static final String ANDROID_PREFIX = "android.";
  public static final String JAVA_PREFIX = "java.";
  private static final Map<Class<?>, IFreightTrain> FREIGHT_TRAINS = new LinkedHashMap<>();

  /**
   * Used to ship values into {@link io.dwak.freight.annotation.Extra} annotated fields
   * @param target class to ship into
     */
  public static void ship(Object target) {
    final Class<?> targetClass = target.getClass();

    IFreightTrain freightTrain = findFreightTrain(targetClass);

    if(freightTrain != null){
      freightTrain.ship(target);
    }
  }

  private static IFreightTrain findFreightTrain(Class<?> cls) {
    IFreightTrain freightTrain = FREIGHT_TRAINS.get(cls);
    if (freightTrain != null) {
      return freightTrain;
    }
    String clsName = cls.getName();
    if (clsName.startsWith(ANDROID_PREFIX) || clsName.startsWith(JAVA_PREFIX)) {
      return null;
    }
    //noinspection TryWithIdenticalCatches
    try {
      Class<?> freightTrainClass = Class.forName(clsName + SUFFIX);
      //noinspection unchecked
      freightTrain = (IFreightTrain) freightTrainClass.newInstance();
    } catch (ClassNotFoundException e) {
      freightTrain = findFreightTrain(cls.getSuperclass());
    } catch (InstantiationException e) {
      Log.e(TAG, e.getMessage());
    } catch (IllegalAccessException e) {
      Log.e(TAG, e.getMessage());
    }
    FREIGHT_TRAINS.put(cls, freightTrain);
    return freightTrain;
  }
}
