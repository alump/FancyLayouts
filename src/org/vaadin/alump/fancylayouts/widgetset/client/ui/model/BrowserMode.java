package org.vaadin.alump.fancylayouts.widgetset.client.ui.model;

public enum BrowserMode {
    DEFAULT, MODERN_WEBKIT, MODERN_GECKO, MODERN_OPERA;

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

    public static BrowserMode resolve() {
        return resolve(getUserAgent());
    }

    private static native String getUserAgent()
    /*-{
    return navigator.userAgent.toLowerCase();
    }-*/;
};