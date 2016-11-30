package io.dwak.freight.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ControllerBuilder {
    /**
     * @return The name of the screen. This will be used to generate a "goTo" method
     */
    String value() default "";

    /**
     * @return The "scope" of the screen. If no scope is given, "Main" will be used to generate a
     * `MainNavigator`
     */
    String scope() default "Main";

    /**
     * @return The change handler to use to push this Controller onto the stack.
     */
    Class pushChangeHandler() default Void.class;

    /**
     * @return The change handler to use to pop this Controller onto the stack.
     */
    Class popChangeHandler() default Void.class;
}
