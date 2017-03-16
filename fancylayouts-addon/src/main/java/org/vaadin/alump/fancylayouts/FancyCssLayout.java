/**
 * FancyCssLayout.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vaadin.shared.Registration;
import org.vaadin.alump.fancylayouts.gwt.client.connect.FancyCssLayoutClientRpc;
import org.vaadin.alump.fancylayouts.gwt.client.connect.FancyCssLayoutServerRpc;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyCssLayoutState;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.shared.Connector;
import com.vaadin.shared.EventId;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * FancyCssLayout is similiar to Vaadin CssLayout. But it also has
 * fancyRemoveComponent() which will add transition to removal. Also when Items
 * are added with addComponent() those will be added with transition.
 */
@SuppressWarnings("serial")
public class FancyCssLayout extends AbstractLayout implements
        LayoutClickNotifier, ComponentContainer.ComponentAttachListener,
        ComponentContainer.ComponentDetachListener, FancyAnimator {

    protected List<Component> components = new ArrayList<Component>();
    protected Set<Component> fancyRemoveComponents = new HashSet<Component>();

    private final FancyCssLayoutServerRpc rpc = new FancyCssLayoutServerRpc() {
        @Override
        public void remove(Connector child) {
            Component removable = (Component) child;
            removeComponent(removable);
        }

        @Override
        public void layoutClick(MouseEventDetails mouseDetails,
                Connector clickedConnector) {
            fireEvent(LayoutClickEvent.createEvent(FancyCssLayout.this,
                    mouseDetails, clickedConnector));
        }
    };

    public FancyCssLayout() {
        registerRpc(rpc, FancyCssLayoutServerRpc.class);
    }

    @Override
    protected FancyCssLayoutState getState() {
        return (FancyCssLayoutState) super.getState();
    }

    /**
     * Replace given component with new. This will not use fancy remove.
     */
    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        if (components.contains(oldComponent)) {
            int index = components.indexOf(oldComponent);
            components.set(index, newComponent);
            super.removeComponent(oldComponent);
            super.addComponent(newComponent);
        }
    }

    @Override
    public void addComponent(Component c) {
        components.add(c);
        try {
            super.addComponent(c);
            markAsDirty();
        } catch (IllegalArgumentException e) {
            components.remove(c);
            throw e;
        }
    }

    /**
     * Add widget to specific index
     * 
     * @param c
     *            Component added
     * @param index
     *            Index where component is added
     */
    public void addComponent(Component c, int index) {
        components.add(index, c);
        try {
            super.addComponent(c);
            markAsDirty();
        } catch (IllegalArgumentException e) {
            components.remove(c);
            throw e;
        }
    }

    @Override
    public void removeComponent(Component c) {
        if (!components.contains(c)) {
            return;
        }
        components.remove(c);
        fancyRemoveComponents.remove(c);
        super.removeComponent(c);
        markAsDirty();
    }

    /**
     * Like removeComponent but will add transition to removal. Notice that
     * there will be delay on removal when this is used. So it's most likely is
     * not safe to relocate Component to new layout instantly.
     * 
     * @param c
     *            Component added
     */
    public void fancyRemoveComponent(Component c) {
        if (!components.contains(c)) {
            return;
        }
        if (fancyRemoveComponents.contains(c)) {
            return;
        }

        fancyRemoveComponents.add(c);
        getRpcProxy(FancyCssLayoutClientRpc.class).fancyRemove(c);
    }

    /**
     * Get number of components
     * 
     * @return Number of components
     */
    @Override
    public int getComponentCount() {
        return components.size();
    }

    @Override
    public void componentDetachedFromContainer(ComponentDetachEvent event) {
        Component component = event.getDetachedComponent();
        if (components.contains(component)) {
            fireComponentDetachEvent(component);
        }
    }

    @Override
    public void componentAttachedToContainer(ComponentAttachEvent event) {
        Component component = event.getAttachedComponent();
        if (components.contains(component)) {
            fireComponentAttachEvent(component);
        }
    }

    /**
     * Use setSlideEnabled
     */
    @Override
    public boolean setTransitionEnabled(FancyTransition trans, boolean enabled) {
        switch (trans) {
        case FADE:
            return true;
        case SLIDE:
            getState().horMarginTransition = enabled;
            getState().verMarginTransition = enabled;
            return getState().horMarginTransition;
        default:
            return false;
        }
    }

    /**
     * Check if different type of transitions are enabled.
     */
    @Override
    public boolean isTransitionEnabled(FancyTransition trans) {
        switch (trans) {
        case FADE:
            return true;
        case SLIDE:
            return getState().horMarginTransition
                    && getState().verMarginTransition;
        default:
            return false;
        }
    }

    /**
     * Enabled slide away effect when component is removed with fancyremove.
     * 
     * @param enabled
     *            true to enable, false to disable.
     */
    public void setSlideEnabled(boolean enabled) {
        setTransitionEnabled(FancyTransition.SLIDE, enabled);
    }

    @Override
    public Registration addLayoutClickListener(LayoutClickListener listener) {
        return addListener(EventId.LAYOUT_CLICK_EVENT_IDENTIFIER,
                LayoutClickEvent.class, listener,
                LayoutClickListener.clickMethod);
    }

    @Override
    public void removeLayoutClickListener(LayoutClickListener listener) {
        removeListener(EventId.LAYOUT_CLICK_EVENT_IDENTIFIER,
                LayoutClickEvent.class, listener);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    /**
     * Reorders children to reverse order.
     */
    public void revertChildOrder() {
        int middle = (int) Math.floor(components.size() * 0.5);
        int otherEnd = components.size() - 1;
        for (int i = 0; i < middle; ++i) {
            Component a = components.get(i);
            Component b = components.get(otherEnd);
            components.set(i, b);
            components.set(otherEnd, a);
            otherEnd -= 1;
        }
        markAsDirty();
    }

}
