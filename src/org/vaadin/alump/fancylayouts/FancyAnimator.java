package org.vaadin.alump.fancylayouts;

public interface FancyAnimator {

    /**
     * Enable or disable given transition type. For applications it's usually
     * better idea to use components own style enabling functions.
     * @param trans Type of transition enabled or disabled
     * @param enabled true to enable or false to disable
     * @return State of transition after call. If transition can not be enabled
     * return value is always false. If it can not be disabled return value
     * is always true.
     */
    boolean setTransitionEnabled(FancyTransition trans, boolean enabled);

    /**
     * Check if given type of transition is enabled
     * @param trans Transition type to checked
     * @return true if enabled, false if disabled or not supported
     */
    boolean isTransitionEnabled(FancyTransition trans);
}
