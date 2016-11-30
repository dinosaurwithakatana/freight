package io.dwak.freight;


public interface Navigator {

    /**
     * Pop to the root of the navigation stack
     * @return Whether screens were popped to return to root
     */
    boolean popToRoot();

    /**
     * Go back in the navigation stack
     * @return whether there are screens left in the stack
     */
    boolean goBack();
}
