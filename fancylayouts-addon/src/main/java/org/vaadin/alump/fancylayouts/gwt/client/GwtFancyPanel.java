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
import java.util.logging.Logger;

import org.vaadin.alump.fancylayouts.gwt.client.model.BrowserMode;
import org.vaadin.alump.fancylayouts.gwt.client.model.ElementStyler;
import org.vaadin.alump.fancylayouts.gwt.client.model.ElementStyler.Value;
import org.vaadin.alump.fancylayouts.gwt.client.model.FadeOutListener;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

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

    protected Boolean scrollEnabled;

    protected FadeOutListener fadeOutListener;

    protected boolean activeTransition = false;

    private final static Logger logger = Logger.getLogger(GwtFancyPanel.class.getName());

    protected class ContentPanel extends ComplexPanel {

        public ContentPanel() {
            setElement(Document.get().createDivElement());
            setStyleName(CLASS_NAME + "-container");
            getElement().getStyle().setPosition(Position.RELATIVE);
            getElement().getStyle().setLeft(0, Unit.PX);
            getElement().getStyle().setTop(0, Unit.PX);
            getElement().getStyle().setWidth(100, Unit.PCT);
            getElement().getStyle().setHeight(100, Unit.PCT);
        }

        @Override
        public void add(Widget widget) {
            DivElement contentWrapper = Document.get().createDivElement();
            contentWrapper.setClassName(CLASS_NAME + "-cwrapper");
            contentWrapper.getStyle().setWidth(100, Unit.PCT);
            if (!GwtFancyPanel.this.isScrollable()) {
                contentWrapper.getStyle().setHeight(100, Unit.PCT);
            }
            getElement().appendChild(contentWrapper);
            add(widget,
                    (com.google.gwt.user.client.Element) Element
                            .as(contentWrapper));
            hide(widget);
        }

        @Override
        public boolean remove(Widget widget) {
            hide(widget);

            Element wrapper = getWrapper(widget);
            boolean removed = super.remove(widget);

            if (removed) {
                if (wrapper != null) {
                    getElement().removeChild(wrapper);
                }
            } else {
                logger.warning("Failed to remove child!");
            }

            return removed;
        }

        public Element getWrapper(Widget widget) {
            Element ret = null;

            if (widget != null && widget.getParent() == this
                    && widget.getElement() != null) {

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

        getElement().addClassName(CLASS_NAME);
        getElement().getStyle().setOverflow(Overflow.HIDDEN);

        contentPanel = new ContentPanel();
        super.add(contentPanel);

        if (browserMode == null) {
            browserMode = BrowserMode.resolve();
        }

        setScrollable(false);
        setFade(true);
    }

    public void setFadeOutListener(FadeOutListener listener) {
        fadeOutListener = listener;
    }

    private boolean addTransitionEndListener(Element element) {

        if (hasTransitionEndListener(element)) {
            return true;
        }

        String eventName = browserMode.getTransitionEnd();
        if (eventName != null) {
            addTransitionEndListener(eventName, element);
            return true;
        }

        return false;
    }

    private native boolean hasTransitionEndListener(Element element)
    /*-{
        return element.hasTransitionEndListener == true;
    }-*/;

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

        if (!activeTransition) {
            logger.warning("onTransitionEnd after transitions cancelled");
            return;
        }

        if (!(object instanceof Element)) {
            return;
        }

        Element element = (Element) object;

        if (previousWidget != null) {
            if (element == contentPanel.getWrapper(previousWidget)) {
                if (elementStyler.isElementStyledOut(element)) {
                    onFadeOutEnded();
                } else {
                    logger.warning("onTransitionEnd for hidden: wrong state?");
                }
            }
        } else {
            if (element == contentPanel.getWrapper(currentWidget)) {
                if (elementStyler.isElementStyledOn(element)) {
                    onFadeInEnded();
                } else {
                    logger.warning("onTransitionEnd for shown: wrong state?");
                }
            }
        }
    }

    private void onFadeOutEnded() {
        clearWrapperTransitionPosition(getContentElement(previousWidget));
        contentPanel.hideAndShow(previousWidget, currentWidget);
        previousWidget = null;

        final Widget currentWidgetWas = currentWidget;
        final Element wrapper = getContentElement(currentWidget);

        if (wrapper != null) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    if (currentWidgetWas == currentWidget) {
                        elementStyler.styleElementOn(wrapper);
                    }
                }
            });
        } else {
            logger.warning("Failed to fade in new widget");
        }
    }

    private void onFadeInEnded() {
        clearWrapperTransitionPosition(getContentElement(currentWidget));
        activeTransition = false;
    }

    @Override
    public void add(Widget widget) {
        if (widget == null) {
            logger.warning("null widget can not be added to fancy panel");
            return;
        }

        if (contentWidgets.contains(widget)) {
            return;
        }

        contentWidgets.add(widget);
        contentPanel.add(widget);
    }

    protected void skipTransition() {
        activeTransition = false;
        clearWrapperTransitionPosition(contentPanel.getWrapper(currentWidget));
        changeContentWithoutTransition(currentWidget);
    }

    @Override
    public boolean remove(Widget widget) {

        if (widget == previousWidget) {
            if (activeTransition) {
                skipTransition();
            }
            previousWidget = null;
        } else if (widget == currentWidget) {
            currentWidget = null;
        }

        if (!contentPanel.remove(widget)) {
            return false;
        }

        contentWidgets.remove(widget);

        return true;
    }

    private void changeContentWithTransition(Widget content) {

        Element prevWrapper = getContentElement(currentWidget);
        if (prevWrapper == null) {
            logger.fine("Missing wrapper of old widget, transition skipped.");
            changeContentWithoutTransition(content);
            return;
        }

        previousWidget = currentWidget;
        currentWidget = content;

        Element nextWrapper = getContentElement(currentWidget);

        addTransitionEndListener(prevWrapper);
        addTransitionEndListener(nextWrapper);

        activeTransition = true;

        setWrapperTransitionPosition(prevWrapper);
        setWrapperTransitionPosition(nextWrapper);

        elementStyler.styleElementOut(prevWrapper);
        elementStyler.styleElementIn(nextWrapper);
    }

    protected void changeContentWithoutTransition(Widget content) {
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
            logger.warning("Setting widget not found from children as current.");
            add(content);
        }

        if (browserMode.hasTransitionEndEvent() && elementStyler.hasValues()
                && previousWidget == null) {
            changeContentWithTransition(content);
        } else {
            changeContentWithoutTransition(content);
        }
    }

    protected Element getContentElement(Widget widget) {
        return contentPanel.getWrapper(widget);
    }

    public boolean isScrollable() {
        return scrollEnabled != null && scrollEnabled;
    }

    public void setScrollable(boolean scroll) {

        if (scrollEnabled != null && scrollEnabled == scroll) {
            return;
        }

        Style style = contentPanel.getElement().getStyle();

        if (scroll) {
            style.setOverflow(Overflow.AUTO);
        } else {
            style.clearOverflow();
        }

        scrollEnabled = scroll;
    }

    public void setScrollTop(int value) {
        contentPanel.getElement().setScrollTop(value);
    }

    public int getScrollTop() {
        return contentPanel.getElement().getScrollTop();
    }

    public void setScrollLeft(int value) {
        contentPanel.getElement().setScrollLeft(value);
    }

    public int getScrollLeft() {
        return contentPanel.getElement().getScrollLeft();
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

        if (enabled == elementStyler.isValueEnabled(Value.OPACITY)) {
            return;
        }

        if (enabled) {
            contentPanel.addStyleName("fancy-fade");
        } else {
            contentPanel.removeStyleName("fancy-fade");
        }
        elementStyler.setValueEnabled(Value.OPACITY, enabled);

        if (!enabled) {
            if (!elementStyler.hasValues()) {
                activeTransition = false;
            }

            for (Widget child : contentWidgets) {
                elementStyler.removeStylingFromElement(
                        contentPanel.getWrapper(child), Value.OPACITY);
            }
        }
    }

    public void setZoom(boolean enabled) {

        if (enabled == elementStyler.isValueEnabled(Value.SCALE)) {
            return;
        }

        if (enabled) {
            contentPanel.addStyleName("fancy-zoom");
        } else {
            contentPanel.removeStyleName("fancy-zoom");
        }
        elementStyler.setValueEnabled(Value.SCALE, enabled);

        if (!enabled) {
            if (!elementStyler.hasValues()) {
                activeTransition = false;
            }

            for (Widget child : contentWidgets) {
                elementStyler.removeStylingFromElement(
                        contentPanel.getWrapper(child), Value.SCALE);
            }
        }
    }

    public void setRotate(boolean enabled, boolean horizontal) {

        if (!enabled) {
            contentPanel.removeStyleName("fancy-rotate");
            elementStyler.setValueEnabled(Value.VERTICAL_ROTATE, false);
            elementStyler.setValueEnabled(Value.HORIZONTAL_ROTATE, false);

            if (!elementStyler.hasValues()) {
                activeTransition = false;
            }

            for (Widget child : contentWidgets) {
                elementStyler.removeStylingFromElement(
                        contentPanel.getWrapper(child), Value.VERTICAL_ROTATE);
            }
        } else {
            if (horizontal) {
                elementStyler.setValueEnabled(Value.VERTICAL_ROTATE, false);
                elementStyler.setValueEnabled(Value.HORIZONTAL_ROTATE, true);
            } else {
                elementStyler.setValueEnabled(Value.HORIZONTAL_ROTATE, false);
                elementStyler.setValueEnabled(Value.VERTICAL_ROTATE, true);
            }

            contentPanel.addStyleName("fancy-rotate");
        }
    }

    /**
     * Clear hardcoded position on the screen
     * 
     * @param wrapper
     */
    private void clearWrapperTransitionPosition(Element wrapper) {
        Style style = wrapper.getStyle();
        style.clearPosition();
        if (this.isScrollable()) {
            style.clearHeight();
        } else {
            style.setHeight(100, Unit.PCT);
        }
        style.clearTop();
        style.clearLeft();
        style.clearOverflow();
    }

    /**
     * Set wrapper to hard coded position on the screen
     * 
     * @param wrapper
     */
    private void setWrapperTransitionPosition(Element wrapper) {
        Style style = wrapper.getStyle();

        style.setTop(0, Unit.PX);
        style.setLeft(0, Unit.PX);
        style.setHeight(100, Unit.PCT);
        style.setPosition(Position.ABSOLUTE);
        style.setOverflow(Overflow.HIDDEN);
    }
}
