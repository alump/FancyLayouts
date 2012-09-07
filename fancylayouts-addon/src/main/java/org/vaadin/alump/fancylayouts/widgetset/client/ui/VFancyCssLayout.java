/**
 * VFancyCssLayout.java (FancyLayouts)
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

/*
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.EventId;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderInformation;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.LayoutClickEventHandler;

public class VFancyCssLayout extends GwtFancyCssLayout implements Paintable,
        Container {

    protected boolean rendering = false;
    protected String paintableId;
    protected ApplicationConnection client;
    protected int lastChildIndex = 0;
    private final RenderInformation renderInformation = new RenderInformation();

    public VFancyCssLayout() {
    }

    private final LayoutClickEventHandler clickEventHandler = new LayoutClickEventHandler(
            this, EventId.LAYOUT_CLICK) {

        @Override
        protected Paintable getChildComponent(Element element) {
            return findComponentWithElement(element);
        }

        @Override
        protected <H extends EventHandler> HandlerRegistration registerHandler(
                H handler, Type<H> type) {
            return addDomHandler(handler, type);
        }
    };

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        // TODO Auto-generated method stub

    }

    public boolean hasChildComponent(Widget component) {
        return hasChild(component);
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // TODO Auto-generated method stub

    }

    public boolean requestLayout(Set<Paintable> children) {
        if (height != null && height != "" && width != null && width != "") {
            return true;
        }

        return !renderInformation.updateSize(getElement());
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;

        if (client.updateComponent(this, uidl, true)) {
            rendering = false;
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        clickEventHandler.handleEventHandlerRegistration(client);

        if (uidl.hasAttribute("margin-transition")) {
            setVerticalMarginTransitionEnabled(uidl
                    .getBooleanAttribute("margin-transition"));
            setHorizontalMarginTransitionEnabled(uidl
                    .getBooleanAttribute("margin-transition"));
        }

        updateContentFromUIDL(uidl);
        rendering = false;

    }

    private void updateContentFromUIDL(UIDL uidl) {
        final Collection<Widget> oldWidgets = new HashSet<Widget>();
        for (final Iterator<Widget> iterator = childIterator(); iterator
                .hasNext();) {
            oldWidgets.add(iterator.next());
        }

        // ValueMap mapAttribute = null;
        // if (uidl.hasAttribute("css")) {
        // mapAttribute = uidl.getMapAttribute("css");
        // }

        lastChildIndex = 0;
        for (final Iterator<Object> i = uidl.getChildIterator(); i.hasNext();) {
            final UIDL r = (UIDL) i.next();
            final Paintable child = client.getPaintable(r);
            final Widget widget = (Widget) child;
            if (hasChild(widget)) {
                oldWidgets.remove(child);
            } else {
                add((Widget) child, lastChildIndex);
            }

            if (!r.getBooleanAttribute("cached")) {
                child.updateFromUIDL(r, client);
            }

            ++lastChildIndex;
        }

        // Remove old widgets
        for (Widget w : oldWidgets) {
            if (!remove(w)) {
                VConsole.error("Failed to remove child?");
            }
            if (w instanceof Paintable) {
                final Paintable p = (Paintable) w;
                client.unregisterPaintable(p);
            }
        }

        int fancyRemoveIndex = 0;
        String fancyRemoveAttr = "fancy-remove-" + fancyRemoveIndex;
        while (uidl.hasAttribute(fancyRemoveAttr)) {
            Paintable child = uidl.getPaintableAttribute(fancyRemoveAttr,
                    client);
            fancyRemove((Widget) child);
            fancyRemoveAttr = "fancy-remove-" + (++fancyRemoveIndex);
        }
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        int w = 0;
        int h = 0;

        if (width != null && !width.equals("")) {
            w = getElement().getOffsetWidth();
            if (w < 0) {
                w = 0;
            }
        }

        if (height != null && !height.equals("")) {
            h = getElement().getOffsetHeight();
            if (h < 0) {
                h = 0;
            }
        }

        return new RenderSpace(w, h, true);
    }

    @Override
    protected void performFancyRemove(Widget widget) {
        if (client == null) {
            super.performFancyRemove(widget);
        } else if (widget instanceof Paintable) {
            Paintable paintable = (Paintable) widget;
            client.updateVariable(paintableId, "remove", paintable, true);
        }
    }

    protected Paintable findComponentWithElement(Element element) {
        return Util.getPaintableForElement(client, VFancyCssLayout.this,
                element);
    }

}
*/