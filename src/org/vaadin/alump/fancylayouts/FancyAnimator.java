/**
 * FancyAnimator.java (FancyLayouts)
 * 
 * Copyright 2012 Vaadin Ltd, Sami Viitanen <alump@vaadin.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.alump.fancylayouts;

/**
 * Animator interface. Not really yet very useful. Ignore :)
 */
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
