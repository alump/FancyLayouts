package org.vaadin.alump.fancylayouts.widgetset.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtFancyPanel extends SimplePanel {

    private Widget contentWidget = null;
    private Widget oldContentWidget = null;

    public static final String CLASS_NAME = "fancy-panel";
    public static final String CONTENT_CLASS_NAME = "fancy-panel-content";

    private boolean transitionsEnabled = true;

    private static BrowserMode browserMode = BrowserMode.DEFAULT;

    protected String height = "";
    protected String width = "";

    protected ScrollPanel scrollPanel;
    protected ComplexPanel contentPanel;

    private String overflowBeforeHide;

    private enum BrowserMode {
        DEFAULT, MODERN_WEBKIT, MODERN_GECKO, MODERN_OPERA
    };

    public GwtFancyPanel() {

        super();

        Element root = Document.get().createDivElement();
        root.addClassName(CLASS_NAME);
        setElement(root);

        contentPanel = new FlowPanel();
        contentPanel.addStyleName(CONTENT_CLASS_NAME);

        setScrollPanel(false);

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

        setScrollable(false);
    }

    private void setScrollPanel(boolean scrollable) {

        if (scrollable) {
            if (scrollPanel == null) {
                scrollPanel = new ScrollPanel();
                scrollPanel.addStyleName(CLASS_NAME + "-scroll");
                scrollPanel.setSize("100%", "100%");
                this.remove(contentPanel);
                scrollPanel.add(contentPanel);
                this.add(scrollPanel);
            }
        } else {
            if (scrollPanel != null) {
                this.remove(scrollPanel);
                scrollPanel = null;
                this.add(contentPanel);
            } else if (contentPanel.getParent() != this) {
                this.add(contentPanel);
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

        if (getContentElement() == element) {

            float value = new Float(getContentElement().getStyle().getOpacity());
            if (value < 0.1f) {
                contentPanel.remove(oldContentWidget);
                oldContentWidget = null;
                contentWidget.getElement().getStyle().setOpacity(1.0);
                getContentElement().getStyle().setOpacity(1.0);

                if (overflowBeforeHide != null) {

                    scrollPanel.getElement().getStyle()
                            .setProperty("overflow-y", overflowBeforeHide);
                    overflowBeforeHide = null;

                }
            }
        }
    }

    private void setContentWithTransition(Widget content) {
        oldContentWidget = contentWidget;
        contentWidget = content;

        if (scrollPanel != null) {
            overflowBeforeHide = scrollPanel.getElement().getStyle()
                    .getOverflowY();
            scrollPanel.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
        } else {
            overflowBeforeHide = null;
        }

        contentPanel.add(content);
        content.getElement().getStyle().setOpacity(0.0);

        addTransitionEndListener(getContentElement());
        getContentElement().getStyle().setOpacity(0.0);
    }

    public void setContent(Widget content) {
        if (content == contentWidget) {
            return;
        }

        if (contentWidget != null && transitionsEnabled) {
            setContentWithTransition(content);
        } else {
            if (contentWidget != null) {
                contentPanel.remove(contentWidget);
            }
            contentWidget = content;
            contentPanel.add(contentWidget);
        }
    }

    public void disableTransitions(boolean disable) {
        if (transitionsEnabled == disable) {
            transitionsEnabled = !disable
                    && (browserMode == BrowserMode.MODERN_WEBKIT || browserMode == BrowserMode.MODERN_GECKO);
        }
    }

    protected Element getContentElement() {
        return contentPanel.getElement();
    }

    public void setScrollable(boolean scroll) {

        setScrollPanel(scroll);
    }

    public void setScrollTop(int value) {
        if (scrollPanel != null) {
            scrollPanel.setVerticalScrollPosition(value);
        }
    }

    public int getScrollTop() {
        if (scrollPanel != null) {
            return scrollPanel.getVerticalScrollPosition();
        } else {
            return 0;
        }
    }

    public void setScrollLeft(int value) {
        if (scrollPanel != null) {
            scrollPanel.setHorizontalScrollPosition(value);
        }
    }

    public int getScrollLeft() {
        if (scrollPanel != null) {
            return scrollPanel.getHorizontalScrollPosition();
        } else {
            return 0;
        }
    }

    @Override
    public void setWidth(String width) {
        if (this.width.endsWith(width)) {
            return;
        }

        this.width = width;
        super.setWidth(width);

    }

    @Override
    public void setHeight(String height) {
        this.height = height;
        super.setHeight(height);
    }

    private static native String getUserAgent()
    /*-{
    return navigator.userAgent.toLowerCase();
    }-*/;

}
