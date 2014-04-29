/**
 * BrowserMode.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts.gwt.client.model;

import com.google.gwt.dom.client.Element;

/**
 * BrowserMode is used to store and identify browser and browser specific
 * functionalities.
 */
public enum BrowserMode {

    /**
     * Default (not included to any of following groups)
     */
    DEFAULT,
    /**
     * Modern Webkit based browser
     */
    MODERN_WEBKIT,
    /**
     * Modern Gecko based browser
     */
    MODERN_GECKO,
    /**
     * Modern Opera (presto?) browser
     */
    MODERN_OPERA;

    private static BrowserMode browserMode;

    /**
     * Resolve browser mode from given user agent string
     * 
     * @param agent
     *            User agent string
     * @return Browser mode
     */
    public static BrowserMode resolve(String agent) {

        BrowserMode ret = BrowserMode.DEFAULT;

        if (agent.contains("webkit")) {
            ret = BrowserMode.MODERN_WEBKIT;
        } else if (agent.contains("gecko/")) {
            ret = BrowserMode.MODERN_GECKO;
        } else if (agent.contains("presto/")) {
            ret = BrowserMode.MODERN_OPERA;
        }

        return ret;
    }

    /**
     * Check if browser has transition end event handling
     * 
     * @return true if supported
     */
    public boolean hasTransitionEndEvent() {
        return getTransitionEnd() != null;
    }

    /**
     * Get transition end event name for browser
     * 
     * @return Name of event
     */
    public String getTransitionEnd() {
        switch (this) {
        case MODERN_WEBKIT:
            return "webkitTransitionEnd";
        case MODERN_GECKO:
            return "transitionend";
        case MODERN_OPERA:
            return "oTransitionEnd";
        default:
            return null;
        }
    }

    /**
     * Resolve browser type from user agent
     * 
     * @return Browser mode
     */
    public static BrowserMode resolve() {
        if (browserMode == null) {
            browserMode = resolve(getUserAgent());
        }
        return browserMode;
    }

    public String getTransform() {
        switch (this) {
        case MODERN_WEBKIT:
            return "webkitTransform";
        default:
            return "transform";
        }
    }

    public String getTranformValue(Element element) {
        return element.getStyle().getProperty(this.getTransform());
    }

    public void setTranformValue(Element element, String value) {
        element.getStyle().setProperty(this.getTransform(), value);
    }

    private static native String getUserAgent()
    /*-{
    return navigator.userAgent.toLowerCase();
    }-*/;
};