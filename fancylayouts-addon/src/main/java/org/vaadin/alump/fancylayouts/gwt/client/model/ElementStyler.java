package org.vaadin.alump.fancylayouts.gwt.client.model;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.Element;

public class ElementStyler {

    private final Set<Value> values = new HashSet<Value>();

    public enum Value {
        OPACITY("opacity", "1.0", "0"), SCALE(BrowserMode.resolve()
                .getTransform(), "scale(1, 1)", "scale(0, 0)");

        private String name;
        private String in;
        private String out;

        private Value(String name, String in, String out) {
            this.name = name;
            this.in = in;
            this.out = out;
        }

        public String getName() {
            return name;
        }

        public String getIn() {
            return in;
        }

        public String getOut() {
            return out;
        }
    }

    public ElementStyler() {

    }

    public void styleElementOut(Element element) {
        for (Value value : values) {
            element.getStyle().setProperty(value.getName(), value.getOut());
        }
    }

    public void removeStylingFromElement(Element element, Value value) {
        element.getStyle().setProperty(value.getName(), null);
    }

    public void styleElementIn(Element element) {
        for (Value value : values) {
            element.getStyle().setProperty(value.getName(), value.getIn());
        }
    }

    public boolean isElementStyledOut(Element element) {
        if (values.isEmpty()) {
            return true;
        } else {
            Value value = values.iterator().next();
            return element.getStyle().getProperty(value.getName())
                    .equals(value.getOut());
        }
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
}
