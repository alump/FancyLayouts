/**
 * GwtFancyCssLayout.java (FancyLayouts)
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vaadin.alump.fancylayouts.gwt.client.model.BrowserMode;
import org.vaadin.alump.fancylayouts.gwt.client.model.FancyRemover;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtFancyCssLayout extends SimplePanel {

    public final static String CLASS_NAME = "fancy-csslayout";
    private static BrowserMode browserMode;
    protected boolean transitionsEnabled = false;
    protected String width = "";
    protected String height = "";

    protected FlowPanel flowPanel = new FlowPanel();
    protected List<Widget> children = new ArrayList<Widget>();
    protected Map<Element, Widget> widgetMap = new HashMap<Element, Widget>();
    protected Set<Widget> removingMap = new HashSet<Widget>();

    protected boolean horizontalMarginTransitionEnabled = true;
    protected boolean verticalMarginTransitionEnabled = true;
    
    protected FancyRemover fancyRemover = null;

    public GwtFancyCssLayout() {
        addStyleName(CLASS_NAME);

        flowPanel.addStyleName(CLASS_NAME + "-content");
        super.add(flowPanel);

        // TODO: Is is temporary hack!!!!
        // TODO: Add proper version checks here (when transitionEnds support has
        // been added)
        if (browserMode == null) {
            browserMode = BrowserMode.resolve();
        }
        transitionsEnabled = !(browserMode == BrowserMode.DEFAULT);
    }
    
    public void addOrMove(Widget widget, int index) {
    	if (hasChild(widget)) {
    		if (children.indexOf(widget) == index) {
    			return;
    		}
    		remove(widget);
    	}
    	
    	add(widget, index);
    }

    public void add(Widget widget, int index) {
        if (hasChild(widget)) {
            return;
        }

        SimplePanel wrapper = new SimplePanel();
        wrapper.setStyleName(CLASS_NAME + "-item");

        if (index < 0 || index >= flowPanel.getWidgetCount()) {
            flowPanel.add(wrapper);
        } else {
            flowPanel.insert(wrapper, index);
        }
        wrapper.add(widget);

        final Element wrapperElement = wrapper.getElement();
        widgetMap.put(wrapperElement, widget);

        children.add(widget);

        if (this.isVisible()) {
            wrapperElement.getStyle().setOpacity(0.0);
            Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

                public boolean execute() {
                    wrapperElement.getStyle().setOpacity(1.0);
                    return false;
                }

            }, 50);
        }
    }

    @Override
    public void add(Widget widget) {
        add(widget, -1);
    }

    public boolean hasChild(Widget widget) {
        return children.contains(widget);
    }

    private void addTransitionEndListener(Element element) {

        if (element.hasAttribute("hasTransitionEndListener")
                && element.getAttribute("hasTransitionEndListener").equals("1")) {
            return;
        }

        String eventName = browserMode.getTransitionEnd();
        if (eventName != null) {
            addTransitionEndListener(eventName, element);
        }
    }

    private native void addTransitionEndListener(String eventName,
            Element element)
    /*-{
         var that = this;
         element.addEventListener(eventName, function(event){
         that.@org.vaadin.alump.fancylayouts.gwt.client.GwtFancyCssLayout::onTransitionEnd(Ljava/lang/Object;)(element);
         },false);
         element.hasTransitionEndListener = true;
    }-*/;

    private void onTransitionEnd(Object object) {

        if (!(object instanceof Element)) {
            return;
        }

        final Element element = (Element) object;
        Widget widget = widgetMap.get(element);
        if (widget == null) {
            return;
        }

        try {
            float value = new Float(element.getStyle().getOpacity());
            if (value < 0.01f) {
                removingMap.remove(widget);
                performFancyRemove(widget);
            }
        } catch (Exception e) {

        }
    }

    private void removeWidgetWithTransition(Widget child) {
        Element wrapperElement = child.getParent().getElement();

        if (!child.isVisible()) {
            performFancyRemove(child);
        } else if (!removingMap.contains(child)) {
            removingMap.add(child);
            addTransitionEndListener(wrapperElement);
            wrapperElement.getStyle().setOpacity(0.0);
            if (verticalMarginTransitionEnabled) {
                wrapperElement.getStyle().setMarginTop(
                        -wrapperElement.getOffsetHeight() / 2.0, Unit.PX);
                wrapperElement.getStyle().setMarginBottom(
                        -wrapperElement.getOffsetHeight() / 2.0, Unit.PX);
            }
            if (horizontalMarginTransitionEnabled) {
                wrapperElement.getStyle().setMarginLeft(
                        -wrapperElement.getOffsetWidth() / 2.0, Unit.PX);
                wrapperElement.getStyle().setMarginRight(
                        -wrapperElement.getOffsetWidth() / 2.0, Unit.PX);
            }
        }
    }
    
    public void setVerticalMarginTransitionEnabled(boolean enabled) {
    	verticalMarginTransitionEnabled = enabled;
    }
    
    public void setHorizontalMarginTransitionEnabled(boolean enabled) {
    	horizontalMarginTransitionEnabled = enabled;
    }

    public boolean fancyRemove(Widget widget) {
        if (!children.contains(widget)) {
            return false;
        }

        if (transitionsEnabled && this.isVisible()) {
            removeWidgetWithTransition(widget);
        } else {
            performFancyRemove(widget);
        }

        return true;
    }
    
    public void setFancyRemover (FancyRemover remover) {
    	fancyRemover = remover;
    }

    /**
     * To be overwritten if additional actions has to be performed. For example
     * do the deletion via server.
     * @param widget Child widget removed
     */
    protected void performFancyRemove(Widget widget) {
    	if (fancyRemover == null) {
    		remove(widget);
    	} else {
    		fancyRemover.remove(widget);
    	}
    }

    @Override
    public boolean remove(Widget widget) {

        if (children.contains(widget)) {
            Widget wrapper = widget.getParent();
            widgetMap.remove(wrapper.getElement());
            removingMap.remove(widget);
            flowPanel.remove(wrapper);
            children.remove(widget);
            return true;
        } else {
            return false;
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

    public Iterator<Widget> childIterator() {
        return children.iterator();
    }

}
