/**
 * FancyPanel.java (FancyLayouts)
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.vaadin.alump.fancylayouts.gwt.client.connect.FancyPanelServerRpc;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyPanelState;

import com.vaadin.event.ActionManager;
import com.vaadin.shared.Connector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * FancyPanel works like Vaadin Panel but it adds transition when content inside
 * it is changed.
 */
@SuppressWarnings("serial")
public class FancyPanel extends AbstractLayout implements
        ComponentContainer.ComponentAttachListener,
        ComponentContainer.ComponentDetachListener, FancyAnimator {

    protected Set<Component> components = new HashSet<Component>();
    protected ActionManager actionManager;
    protected int scrollTop = 0;
    protected int scrollLeft = 0;

    protected FancyPanelServerRpc serverRpc = new FancyPanelServerRpc() {

        @Override
        public void scrollTop(int top) {
            scrollTop = top;
        }

        @Override
        public void scrollLeft(int left) {
            scrollLeft = left;
        }

        @Override
        public void hidden(Connector child) {
            /*
             * Component component = (Component)child; if
             * (contents.contains(component)) { if (getContent() == component) {
             * setContent(null, true); } else {
             * FancyPanel.super.removeComponent(component); }
             * contents.remove(component); }
             */
        }

        @Override
        public void layoutClick(MouseEventDetails mouseDetails,
                Connector clickedConnector) {
            // TODO Auto-generated method stub

        }

    };

    /**
     * Create new panel with given content
     * 
     * @param content
     *            Content used inside panel
     */
    public FancyPanel(Component content) {
        super();
        showComponent(content);
    }

    /**
     * Create new panel with content and caption
     * 
     * @param content
     *            Content of panel
     * @param caption
     *            Caption of panel
     */
    public FancyPanel(ComponentContainer content, String caption) {
        this(content);
        setCaption(caption);
    }

    @Override
    protected FancyPanelState getState() {
        return (FancyPanelState) super.getState();
    }

    /**
     * Build default content container
     * 
     * @return Default content container
     */
    protected ComponentContainer createDefaultContent() {
        CssLayout layout = new CssLayout();
        layout.setStyleName("fancypanel-default-layout");
        layout.setSizeFull();
        return layout;
    }

    /**
     * Get currently visible component
     * 
     * @return Currently visible component
     */
    public Component getCurrentComponent() {
        return (ComponentContainer) getState().currentComponent;
    }

    /**
     * Show given component in panel. Will add to panel if not added yet.
     * 
     * @param component
     *            Component shown in panel.
     */
    public void showComponent(Component component) {

        if (component == null) {
            component = createDefaultContent();
        }

        addComponent(component);

        if (getCurrentComponent() != component) {
            getState().currentComponent = component;
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        if (getState().currentComponent == null) {
            if (this.getComponentCount() == 0) {
                showComponent(null);
            } else {
                showComponent(components.iterator().next());
            }
        }

        super.beforeClientResponse(initial);
    }

    @Override
    /**
     * Add child to current content (use setContent to add direct child to this
     * instance)
     */
    public void addComponent(Component c) {
        if (!components.contains(c)) {
            components.add(c);

            try {
                super.addComponent(c);

                if (components.size() == 1) {
                    getState().currentComponent = c;
                }

                markAsDirty();
            } catch (IllegalArgumentException e) {
                components.remove(c);
                throw e;
            }
        }
    }

    @Override
    /**
     * Removes all components
     */
    public void removeAllComponents() {
        super.removeAllComponents();
        components.clear();
        getState().currentComponent = null;
        markAsDirty();
    }

    @Override
    public void removeComponent(Component c) {
        if (components.contains(c)) {
            components.remove(c);
            super.removeComponent(c);
            if (getCurrentComponent() == c) {
                getState().currentComponent = null;
            }
            markAsDirty();
        }
    }

    @Override
    protected ActionManager getActionManager() {
        if (actionManager == null) {
            // actionManager = new ActionManager(this);
        }
        return actionManager;
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {

        boolean showNew = (getCurrentComponent() == oldComponent);

        if (showNew) {
            showComponent(newComponent);
            removeComponent(oldComponent);
        } else {
            removeComponent(oldComponent);
            addComponent(newComponent);
        }
    }

    /**
     * Set FancyPanel scrollable
     * 
     * @param scrollable
     *            true to make panel scrollable
     */
    public void setScrollable(boolean scrollable) {
        getState().scrollable = scrollable;
    }

    /**
     * Check if FancyPanel is scrollable
     * 
     * @return true if scrollable
     */
    public boolean isScrollable() {
        return getState().scrollable;
    }

    @Override
    public boolean setTransitionEnabled(FancyTransition trans, boolean enabled) {
        switch (trans) {
        case FADE:
            getState().useTransitions = enabled;
            return !getState().useTransitions;
        default:
            return false;
        }
    }

    @Override
    public boolean isTransitionEnabled(FancyTransition trans) {
        switch (trans) {
        case FADE:
            return !getState().useTransitions;
        default:
            return false;
        }
    }

    /**
     * Enabled and disable fade transition
     * 
     * @param enabled
     *            true to enable, false to disable
     */
    public void setFadeTransitionEnabled(boolean enabled) {
        setTransitionEnabled(FancyTransition.FADE, enabled);
    }

    @Override
    public int getComponentCount() {
        return components.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
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
     * If components are automatically removed when replaced
     * 
     * @param remove
     *            true to have auto remove enabled
     */
    public void setAutoRemove(boolean remove) {
        getState().autoRemove = remove;
    }

    public boolean isAutoRemove() {
        return getState().autoRemove;
    }

}
