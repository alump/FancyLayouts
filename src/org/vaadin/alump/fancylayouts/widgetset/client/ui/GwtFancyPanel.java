package org.vaadin.alump.fancylayouts.widgetset.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtFancyPanel extends ComplexPanel {

    private Widget contentWidget = null;
    private Widget oldContentWidget = null;

    public static final String CLASS_NAME = "fancy-panel";
    public static final String CONTENT_CLASS_NAME = "fancy-panel-content";

    private final com.google.gwt.user.client.Element contentElement;

    private boolean transitionsEnabled = true;

    private static BrowserMode browserMode = BrowserMode.DEFAULT;

    private enum BrowserMode {
        DEFAULT, MODERN_WEBKIT, MODERN_GECKO, MODERN_OPERA
    };

    public GwtFancyPanel() {

        super();

        Element root = Document.get().createDivElement();
        root.addClassName(CLASS_NAME);
        setElement(root);

        contentElement = DOM.createDiv();
        contentElement.addClassName(CONTENT_CLASS_NAME);
        root.appendChild(contentElement);

        if (browserMode == BrowserMode.DEFAULT) {
            String agent = getUserAgent();
            if (agent.contains("webkit")) {
                browserMode = BrowserMode.MODERN_WEBKIT;
                transitionsEnabled = true;
            } else if (agent.contains("gecko/")) {
                browserMode = BrowserMode.MODERN_GECKO;
                transitionsEnabled = true;
            } else {
                transitionsEnabled = false;
            }
        }
    }

    private void addTransitionEndListener(Element element) {
        String eventName = null;
        if (browserMode == BrowserMode.MODERN_WEBKIT) {
            eventName = "webkitTransitionEnd";
        } else if (browserMode == BrowserMode.MODERN_GECKO) {
            eventName = "transitionend";
        } else if (browserMode == BrowserMode.MODERN_OPERA) {
            eventName = "oTransitionEnd";
        }

        if (eventName != null) {
            addTransitionEndListener(eventName, element);
        }
    }

    private native void addTransitionEndListener(String eventName,
            Element element)
    /*-{
         var that = this;
         element.addEventListener(eventName, function(event){
         that.@org.vaadin.alump.fancylayouts.widgetset.client.ui.GwtFancyPanel::onTransitionEnd(Ljava/lang/Object;)(element);
         },false);
         element.hasTransitionEndListener = true;
    }-*/;

    private void onTransitionEnd(Object object) {

        if (!(object instanceof Element)) {
            return;
        }

        Element element = (Element) object;

        if (contentElement == element) {

            float value = new Float(contentElement.getStyle().getOpacity());
            if (value == 0.0f) {
                remove(oldContentWidget);
                contentElement.getStyle().setOpacity(1.0);
                contentWidget.getElement().getStyle().setOpacity(1.0);
            }
        }
    }

    private void setContentWithTransition(Widget content) {
        oldContentWidget = contentWidget;
        contentWidget = content;

        add(content, contentElement);
        content.getElement().getStyle().setOpacity(0.01);

        addTransitionEndListener(contentElement);
        contentElement.getStyle().setOpacity(0.0);
    }

    public void setContent(Widget content) {
        if (content == contentWidget) {
            return;
        }

        if (contentWidget != null && transitionsEnabled) {
            setContentWithTransition(content);
        } else {
            if (contentWidget != null) {
                remove(contentWidget);
            }
            contentWidget = content;
            add(content, contentElement);
        }
    }

    public void disableTransitions(boolean disable) {
        if (transitionsEnabled == disable) {
            transitionsEnabled = !disable
                    && (browserMode == BrowserMode.MODERN_WEBKIT || browserMode == BrowserMode.MODERN_GECKO);
        }
    }

    protected Element getContentElement() {
        return contentElement;
    }

    private static native String getUserAgent()
    /*-{
    return navigator.userAgent.toLowerCase();
    }-*/;

}
