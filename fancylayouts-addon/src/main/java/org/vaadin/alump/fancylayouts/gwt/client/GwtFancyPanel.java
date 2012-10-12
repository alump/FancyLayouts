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
import org.vaadin.alump.fancylayouts.gwt.client.model.FadeOutListener;
import org.vaadin.alump.fancylayouts.gwt.client.model.FancyRemover;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtFancyPanel extends SimplePanel {

    private Widget contentWidget = null;
    private Set<Widget> contentWidgets = new HashSet<Widget>();

    public static final String CLASS_NAME = "fancy-panel";

    private boolean transitionsEnabled = true;

    private static BrowserMode browserMode;

    protected String height = "";
    protected String width = "";

    protected ScrollPanel scrollPanel;
    protected ComplexPanel contentPanel;

    private Boolean scrollEnabled;
    private String overflowXBeforeHide;
    private String overflowYBeforeHide;
    
    protected FadeOutListener fadeOutListener;

    public GwtFancyPanel() {

        super();

        Element root = Document.get().createDivElement();
        root.addClassName(CLASS_NAME);
        root.getStyle().setOverflow(Overflow.HIDDEN);
        setElement(root);

        scrollPanel = new ScrollPanel();
        scrollPanel.addStyleName(CLASS_NAME + "-scroll");
        scrollPanel.getElement().getStyle().setWidth(100, Unit.PCT);
        scrollPanel.getElement().getStyle().setHeight(100, Unit.PCT);
        add(scrollPanel);

        contentPanel = new FlowPanel();
        contentPanel.addStyleName(CLASS_NAME + "-content");
        scrollPanel.add(contentPanel);

        if (browserMode == null) {
            browserMode = BrowserMode.resolve();
        }
        transitionsEnabled = browserMode.hasTransitionEndEvent();

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

        if (getContentElement() == element) {

            float value = new Float(getContentElement().getStyle().getOpacity());
            if (value < 0.01f) {
                onFadeOutEnded();
            }
        }
    }

    private void onFadeOutEnded() {

    	for (Widget old : contentWidgets) {
    		if (old == contentWidget) {
    			continue;
    		}
	        if (fadeOutListener != null) {
	        	old.setVisible(false);
	        	fadeOutListener.fadeOut(old);
	        } else {
	        	remove(old);
	        }
    	}
    	
    	// Start opacity for new content
        getContentElement().getStyle().setOpacity(1.0);
        contentWidget.getElement().getStyle().setOpacity(1.0);
        setFadeScrollHide(false);
    }
    
    @Override
    public void add (Widget widget) {
    	if (contentWidgets.contains(widget)) {
    		return;
    	}
    	contentWidgets.add(widget);
    	contentPanel.add(widget);
    	widget.setVisible(false);
    }
    
    @Override
    public boolean remove (Widget widget) {
    	if (!contentPanel.remove(widget)) {
    		return false;
    	}
    	
    	if (contentWidgets.contains(widget)) {
    		contentWidgets.remove(widget);
    	}
    	
    	if (contentWidget == widget) {
    		contentWidget = null;
    	}
    	
    	return true;
    }
    
    protected boolean isFadeOutUsed() {
    	return isVisible() && transitionsEnabled;
    }
    
    private void setContentWithTransition(Widget content) {    	

        contentWidget = content;
        setFadeScrollHide(true);

        if (!getContentElement().getStyle().getOpacity().equals("0")
                && addTransitionEndListener(getContentElement())) {
            getContentElement().getStyle().setOpacity(0.0);
            contentWidget.getElement().getStyle().setOpacity(0.0);
        } else {
    		contentWidget.setVisible(true);
        }
    }
    
    protected void setContentWithoutTransition(Widget content) {
		if (fadeOutListener != null) {
    		contentWidget.setVisible(false);
			fadeOutListener.fadeOut(contentWidget);
		} else {
			remove(contentWidget);
		}
		contentWidget = content;
		contentWidget.setVisible(true);
    }

    public void setContent(Widget content) {
        if (content == contentWidget) {
            return;
        }
        
        if (!contentWidgets.contains(content)) {
        	add(content);
        }
        	
        if (contentWidget != null && isFadeOutUsed()) {
        	setContentWithTransition(content);
        } else {
        	setContentWithoutTransition(content);
        }  
    }

    public void disableTransitions(boolean disable) {
        if (transitionsEnabled == disable) {
            transitionsEnabled = !disable
                    && browserMode.hasTransitionEndEvent();

            if (transitionsEnabled == false && contentWidget != null) {
                getContentElement().getStyle().setOpacity(1.0);
                contentWidget.getElement().getStyle().setOpacity(1.0);
            }
        }
    }
    
    

    protected Element getContentElement() {
        return contentPanel.getElement();
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

        if (on) {
            if (overflowYBeforeHide == null) {
                overflowXBeforeHide = scrollPanel.getElement().getStyle()
                        .getOverflowX();
                overflowYBeforeHide = scrollPanel.getElement().getStyle()
                        .getOverflowY();
                scrollPanel.getElement().getStyle()
                        .setOverflowX(Overflow.HIDDEN);
                scrollPanel.getElement().getStyle()
                        .setOverflowY(Overflow.HIDDEN);
            }
        } else {
            if (overflowYBeforeHide != null) {
                scrollPanel.getElement().getStyle()
                        .setProperty("overflow-x", overflowXBeforeHide);
                scrollPanel.getElement().getStyle()
                        .setProperty("overflow-y", overflowYBeforeHide);
                overflowXBeforeHide = null;
                overflowYBeforeHide = null;
            }
        }

    }

    public void setScrollable(boolean scroll) {

        if (scrollEnabled != null && scrollEnabled == scroll) {
            return;
        }

        setFadeScrollHide(false);

        Style scrollStyle = scrollPanel.getElement().getStyle();

        if (scroll) {
            scrollStyle.setOverflow(Overflow.AUTO);
        } else {
            scrollStyle.setOverflow(Overflow.HIDDEN);
        }

        scrollEnabled = scroll;
    }

    public void setScrollTop(int value) {
        if (scrollPanel != null) {
            scrollPanel.setVerticalScrollPosition(value);
        }
    }

    public int getScrollTop() {
        if (scrollEnabled != null) {
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
        if (scrollEnabled != null) {
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
        if (this.height.endsWith(height)) {
            return;
        }
    	
        this.height = height;
        super.setHeight(height);
    }

}
