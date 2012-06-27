/**
 * VFancyPanel.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts.widgetset.client.ui;

import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderInformation;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFancyPanel extends GwtFancyPanel implements Paintable, Container {

    protected String paintableId;
    protected ApplicationConnection client;

    private Paintable content = null;

    protected final static String ATTR_TRANSITIONS = "transitions";
    protected final static String ATTR_SCROLLABLE = "scrollable";
    protected final static String ATTR_SCROLL_TOP = "scroll-top";
    protected final static String ATTR_SCROLL_LEFT = "scroll-left";

    private final RenderInformation renderInformation = new RenderInformation();

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, false)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        if (uidl.hasAttribute(ATTR_TRANSITIONS)) {
            disableTransitions(!uidl.getBooleanAttribute(ATTR_TRANSITIONS));
        }

        // Render content
        final UIDL layoutUidl = uidl.getChildUIDL(0);
        final Paintable newContent = client.getPaintable(layoutUidl);
        if (newContent != content) {
            if (content != null) {
                client.unregisterPaintable(content);
            }
            setContent((Widget) newContent);
            content = newContent;
        }
        content.updateFromUIDL(layoutUidl, client);

        if (uidl.hasAttribute(ATTR_SCROLLABLE)) {
            if (isScrollable() != uidl.getBooleanAttribute(ATTR_SCROLLABLE)) {
                setScrollable(uidl.getBooleanAttribute(ATTR_SCROLLABLE));
                client.requestLayoutPhase();
            }
        }

        if (uidl.hasAttribute(ATTR_SCROLL_TOP)) {
            setScrollTop(uidl.getIntAttribute(ATTR_SCROLL_TOP));
        }

        if (uidl.hasAttribute(ATTR_SCROLL_LEFT)) {
            setScrollLeft(uidl.getIntAttribute(ATTR_SCROLL_LEFT));
        }

    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        if (!hasChildComponent(oldComponent)) {
            return;
        }

        setContent(newComponent);
        content = (Paintable) newComponent;
    }

    public boolean hasChildComponent(Widget component) {
        if (component != null && content == component) {
            return true;
        } else {
            return false;
        }
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // TODO Auto-generated method stub

    }

    public boolean requestLayout(Set<Paintable> children) {
        client.handleComponentRelativeSize((Widget) content);
        if (height != null && height != "" && width != null && width != "") {
            return true;
        }

        return !renderInformation.updateSize(getElement());
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        int w = 0;
        int h = 0;

        if (width != null && !width.equals("")) {
            if (isScrollable()) {
                if (child.getElement().getOffsetWidth() < getContentElement()
                        .getOffsetWidth()) {
                    w = getContentElement().getOffsetWidth();
                } else {
                    w = child.getElement().getOffsetWidth();
                }
            } else {
                w = getContentElement().getOffsetWidth();
            }
            if (w < 0) {
                w = 0;
            }
        }

        if (height != null && !height.equals("")) {
            if (isScrollable()) {
                h = child.getElement().getOffsetHeight();
            } else {
                h = getContentElement().getOffsetHeight();
            }
            if (h < 0) {
                h = 0;
            }
        }

        return new RenderSpace(w, h, true);
    }

}
