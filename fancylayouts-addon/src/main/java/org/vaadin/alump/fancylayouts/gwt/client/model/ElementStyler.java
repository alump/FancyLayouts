package org.vaadin.alump.fancylayouts.gwt.client.model;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.VConsole;

/**
 * Styler for wrapper element. In = element is hidden, but will be now shown. On
 * = element is shown. Out = element was shown and will be now hidden.
 * 
 */
public class ElementStyler {

    private final Set<Value> values = new HashSet<Value>();

    public final static String ON_CLASSNAME = "fancy--on";
    public final static String OUT_CLASSNAME = "fancy--out";
    public final static String IN_CLASSNAME = "fancy--in";

    public enum Value {
        // Fade in and out
        OPACITY("opacity", "1.0", "0"),
        // Zoom out
        SCALE(BrowserMode.resolve().getTransform(), "scale(1, 1)",
                "scale(0, 0)"),
        // Rotate as a card
        ROTATE(BrowserMode.resolve().getTransform(), "rotateX(0deg)",
                "rotateX(-90deg)", "rotateX(90deg)");

        private String name;
        private String in;
        private String on;
        private String out;

        private Value(String name, String on, String inOut) {
            this.name = name;
            this.on = on;
            this.in = inOut;
            this.out = inOut;
        }

        private Value(String name, String on, String in, String out) {
            this.name = name;
            this.on = on;
            this.in = in;
            this.out = out;
        }

        public String getName() {
            return name;
        }

        public String getIn() {
            return in;
        }

        public String getOn() {
            return on;
        }

        public String getOut() {
            return out;
        }

        public boolean isIn(String value) {
            if (value != null && in != null && in.equals(value)) {
                return true;
            } else {
                VConsole.error("IN: '" + in + "' != '" + value + "'");
                return false;
            }
        }

        public boolean isOn(String value) {
            if (value != null && on != null && on.equals(value)) {
                return true;
            } else {
                VConsole.error("ON: '" + in + "' != '" + value + "'");
                return false;
            }
        }

        public boolean isOut(String value) {
            if (value != null && in != null && in.equals(value)) {
                return true;
            } else {
                VConsole.error("OUT: '" + in + "' != '" + value + "'");
                return false;
            }
        }
    }

    public ElementStyler() {

    }

    public void removeStylingFromElement(Element element, Value value) {
        element.removeClassName(ON_CLASSNAME);
        element.removeClassName(OUT_CLASSNAME);
        element.removeClassName(IN_CLASSNAME);
        element.getStyle().setProperty(value.getName(), null);
    }

    public void styleElementOut(Element element) {
        element.removeClassName(ON_CLASSNAME);
        element.removeClassName(IN_CLASSNAME);
        element.addClassName(OUT_CLASSNAME);
        for (Value value : values) {
            element.getStyle().setProperty(value.getName(), value.getOut());
        }
    }

    public void styleElementIn(Element element) {
        element.removeClassName(ON_CLASSNAME);
        element.removeClassName(OUT_CLASSNAME);
        element.addClassName(IN_CLASSNAME);
        for (Value value : values) {
            element.getStyle().setProperty(value.getName(), value.getIn());
        }
    }

    public void styleElementOn(Element element) {
        element.removeClassName(IN_CLASSNAME);
        element.removeClassName(OUT_CLASSNAME);
        element.addClassName(ON_CLASSNAME);
        for (Value value : values) {
            element.getStyle().setProperty(value.getName(), value.getOn());
        }
    }

    public boolean isElementStyledOut(Element element) {
        String className = element.getClassName();
        return className != null && className.contains(OUT_CLASSNAME);
    }

    public boolean isElementStyledIn(Element element) {
        String className = element.getClassName();
        return className != null && className.contains(IN_CLASSNAME);
    }

    public boolean isElementStyledOn(Element element) {
        String className = element.getClassName();
        return className != null && className.contains(ON_CLASSNAME);
    }

    public boolean hasValues() {
        return !values.isEmpty();
    }

    public void setValueEnabled(Value value, boolean enabled) {
        if (enabled) {
            values.add(value);
        } else {
            values.remove(value);
        }
    }

    public boolean isValueEnabled(Value value) {
        return values.contains(value);
    }
}
