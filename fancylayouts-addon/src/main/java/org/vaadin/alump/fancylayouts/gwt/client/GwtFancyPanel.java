/**
 * GwtFancyPanel.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts.gwt.client;

import java.util.HashSet;
import java.util.Set;

import org.vaadin.alump.fancylayouts.gwt.client.model.BrowserMode;
import org.vaadin.alump.fancylayouts.gwt.client.model.ElementStyler;
import org.vaadin.alump.fancylayouts.gwt.client.model.ElementStyler.Value;
import org.vaadin.alump.fancylayouts.gwt.client.model.FadeOutListener;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VConsole;

public class GwtFancyPanel extends SimplePanel {

    private Widget previousWidget = null;
    private Widget currentWidget = null;
    private final Set<Widget> contentWidgets = new HashSet<Widget>();

    public static final String CLASS_NAME = "fancy-panel";

    protected ElementStyler elementStyler = new ElementStyler();

    private static BrowserMode browserMode;

    protected String height = "";
    protected String width = "";

    protected ContentPanel contentPanel;

    private Boolean scrollEnabled;
    private String overflowXBeforeHide;
    private String overflowYBeforeHide;

    protected FadeOutListener fadeOutListener;

    protected class ContentPanel extends ComplexPanel {

        public ContentPanel() {
            setElement(Document.get().createDivElement());
            setStyleName(CLASS_NAME + "-container");
        }

        @Override
        public void add(Widget widget) {
            DivElement contentWrapper = Document.get().createDivElement();
            contentWrapper.setClassName(CLASS_NAME + "-cwrapper");
            getElement().appendChild(contentWrapper);
            add(widget,
                    (com.google.gwt.user.client.Element) Element
                            .as(contentWrapper));
            hide(widget);
        }

        @Override
        public boolean remove(Widget widget) {
            Element wrapper = getWrapper(widget);
            boolean removed = super.remove(widget);

            if (removed && wrapper != null) {
                getElement().removeChild(wrapper);
            }

            return removed;
        }

        public Element getWrapper(Widget widget) {
            Element ret = null;

            if (widget.getParent() == this) {
                ret = widget.getElement().getParentElement();
            }

            return ret;
        }

        public void hide(Widget widget) {
            Element wrapper = getWrapper(widget);
            if (wrapper != null) {
                wrapper.getStyle().setDisplay(Display.NONE);
            }
        }

        public void show(Widget widget) {
            Element wrapper = getWrapper(widget);
            if (wrapper != null) {
                wrapper.getStyle().setDisplay(Display.BLOCK);
            }
        }

        public void hideAndShow(Widget hideWidget, Widget showWidget) {
            hide(hideWidget);
            show(showWidget);
        }
    }

    public GwtFancyPanel() {

        Element root = Document.get().createDivElement();
        root.addClassName(CLASS_NAME);
        root.getStyle().setOverflow(Overflow.HIDDEN);
        setElement(root);

        contentPanel = new ContentPanel();
        super.add(contentPanel);

        if (browserMode == null) {
            browserMode = BrowserMode.resolve();
        }

        setScrollable(false);
    }

    public void setFadeOutListener(FadeOutListener listener) {
        fadeOutListener = listener;
    }

    private boolean addTransitionEndListener(Element element) {

        if (element.hasAttribute("hasTransitionEndListener")
                && element.getAttribute("hasTransitionEndListener").equals("1")) {
            return true;
        }

        String eventName = browserMode.getTransitionEnd();
        if (eventName != null) {
            addTransitionEndListener(eventName, element);
            return true;
        }

        return false;
    }

    private native void addTransitionEndListener(String eventName,
            Element element)
    /*-{
         var that = this;
         element.addEventListener(eventName, function(event){
         that.@org.vaadin.alump.fancylayouts.gwt.client.GwtFancyPanel::onTransitionEnd(Ljava/lang/Object;)(element);
         },false);
         element.hasTransitionEndListener = true;
    }-*/;

    private void onTransitionEnd(Object object) {

        if (!(object instanceof Element)) {
            return;
        }

        Element element = (Element) object;

        if (getContentElement(previousWidget) == element) {

            if (this.elementStyler.isElementStyledOut(element)) {
                onFadeOutEnded();
            }
        }
    }

    private void onFadeOutEnded() {
        contentPanel.hideAndShow(previousWidget, currentWidget);
        Element wrapper = getContentElement(currentWidget);

        setWrapperTransitionPosition(wrapper, true);
        if (wrapper != null) {
            elementStyler.styleElementIn(wrapper);
        }
        setFadeScrollHide(false);
    }

    @Override
    public void add(Widget widget) {
        if (widget == null) {
            VConsole.error("null widget can not be added");
            return;
        }

        if (contentWidgets.contains(widget)) {
            return;
        }

        contentWidgets.add(widget);
        contentPanel.add(widget);
    }

    @Override
    public boolean remove(Widget widget) {
        if (!contentPanel.remove(widget)) {
            return false;
        }

        if (contentWidgets.contains(widget)) {
            contentWidgets.remove(widget);
        }

        if (currentWidget == widget) {
            currentWidget = null;
        }

        return true;
    }

    private void setContentWithTransition(Widget content) {

        previousWidget = currentWidget;
        currentWidget = content;

        if (previousWidget == null) {
            setContentWithoutTransition(currentWidget);
        } else {
            setFadeScrollHide(true);

            Element prevWrapper = getContentElement(previousWidget);

            if (prevWrapper != null
                    && !prevWrapper.getStyle().getOpacity().equals("0")
                    && addTransitionEndListener(prevWrapper)) {
                Element nextWrapper = getContentElement(currentWidget);

                elementStyler.styleElementOut(prevWrapper);
                elementStyler.styleElementOut(nextWrapper);

                setWrapperTransitionPosition(nextWrapper, false);
            } else {
                contentPanel.hideAndShow(previousWidget, currentWidget);
            }
        }
    }

    protected void setContentWithoutTransition(Widget content) {
        if (currentWidget != null) {
            contentPanel.hide(currentWidget);
        }

        currentWidget = content;
        contentPanel.show(currentWidget);
    }

    public boolean hasWidget(Widget widget) {
        return contentWidgets.contains(widget);
    }

    public void setContent(Widget content) {
        if (content == currentWidget) {
            return;
        }

        if (!contentWidgets.contains(content)) {
            add(content);
        }

        if (elementStyler.hasValues()) {
            setContentWithTransition(content);
        } else {
            setContentWithoutTransition(content);
        }
    }

    protected Element getContentElement(Widget widget) {
        return contentPanel.getWrapper(widget);
    }

    public boolean isScrollable() {
        return scrollEnabled != null && scrollEnabled;
    }

    private void setFadeScrollHide(boolean on) {
        if (scrollEnabled == null || !scrollEnabled) {
            return;
        }

        // Disable on mozilla, causes issues
        if (browserMode == BrowserMode.MODERN_GECKO) {
            return;
        }

        /*
         * if (on) { if (overflowYBeforeHide == null) { overflowXBeforeHide =
         * scrollPanel.getElement().getStyle() .getOverflowX();
         * overflowYBeforeHide = scrollPanel.getElement().getStyle()
         * .getOverflowY(); scrollPanel.getElement().getStyle()
         * .setOverflowX(Overflow.HIDDEN); scrollPanel.getElement().getStyle()
         * .setOverflowY(Overflow.HIDDEN); } } else { if (overflowYBeforeHide !=
         * null) { scrollPanel.getElement().getStyle()
         * .setProperty("overflow-x", overflowXBeforeHide);
         * scrollPanel.getElement().getStyle() .setProperty("overflow-y",
         * overflowYBeforeHide); overflowXBeforeHide = null; overflowYBeforeHide
         * = null; } }
         */

    }

    public void setScrollable(boolean scroll) {

        if (scrollEnabled != null && scrollEnabled == scroll) {
            return;
        }

        setFadeScrollHide(false);

        /*
         * Style scrollStyle = scrollPanel.getElement().getStyle();
         * 
         * if (scroll) { scrollStyle.setOverflow(Overflow.AUTO); } else {
         * scrollStyle.setOverflow(Overflow.HIDDEN); }
         */

        scrollEnabled = scroll;
    }

    public void setScrollTop(int value) {
        /*
         * if (scrollPanel != null) {
         * scrollPanel.setVerticalScrollPosition(value); }
         */
    }

    public int getScrollTop() {
        /*
         * if (scrollEnabled != null) { return
         * scrollPanel.getVerticalScrollPosition(); } else { return 0; }
         */
        return 0;
    }

    public void setScrollLeft(int value) {
        /*
         * if (scrollPanel != null) {
         * scrollPanel.setHorizontalScrollPosition(value); }
         */
    }

    public int getScrollLeft() {
        /*
         * if (scrollEnabled != null) { return
         * scrollPanel.getHorizontalScrollPosition(); } else { return 0; }
         */
        return 0;
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
        if (this.height.endsWith(height)) {
            return;
        }

        this.height = height;
        super.setHeight(height);
    }

    public void setFade(boolean enabled) {
        if (enabled) {
            contentPanel.addStyleName("fancy-fade");
        } else {
            contentPanel.removeStyleName("fancy-fade");
        }
        elementStyler.setValueEnabled(Value.OPACITY, enabled);

        if (!enabled) {
            for (Widget child : contentWidgets) {
                elementStyler.removeStylingFromElement(
                        contentPanel.getWrapper(child), Value.OPACITY);
            }
        }
    }

    public void setZoom(boolean enabled) {
        if (enabled) {
            contentPanel.addStyleName("fancy-zoom");
        } else {
            contentPanel.removeStyleName("fancy-zoom");
        }
        elementStyler.setValueEnabled(Value.SCALE, enabled);

        if (!enabled) {
            for (Widget child : contentWidgets) {
                elementStyler.removeStylingFromElement(
                        contentPanel.getWrapper(child), Value.SCALE);
            }
        }
    }

    private void setWrapperTransitionPosition(Element wrapper, boolean shown) {
        if (shown) {
            wrapper.getStyle().setProperty("width", null);
            wrapper.getStyle().setProperty("height", null);
            wrapper.getStyle().setProperty("top", null);
            wrapper.getStyle().setProperty("left", null);
            wrapper.getStyle().setPosition(Position.FIXED);
            wrapper.getStyle().setDisplay(Display.BLOCK);
        } else {
            int width = wrapper.getClientWidth();
            int height = wrapper.getClientHeight();
            wrapper.getStyle().setTop(0, Unit.PX);
            wrapper.getStyle().setLeft(0, Unit.PX);
            wrapper.getStyle().setWidth(width, Unit.PX);
            wrapper.getStyle().setHeight(height, Unit.PX);
            wrapper.getStyle().setPosition(Position.ABSOLUTE);
            wrapper.getStyle().setDisplay(Display.BLOCK);
        }
    }
}
