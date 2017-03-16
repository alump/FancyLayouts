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

import com.vaadin.shared.Registration;
import org.vaadin.alump.fancylayouts.gwt.client.connect.FancyPanelServerRpc;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyPanelState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.RotateDirection;

import com.vaadin.event.ActionManager;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.shared.Connector;
import com.vaadin.shared.EventId;
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
        ComponentContainer.ComponentDetachListener, LayoutClickNotifier {

    protected Set<Component> components = new HashSet<Component>();
    protected ActionManager actionManager;
    protected int scrollTop = 0;
    protected int scrollLeft = 0;
    protected ComponentContainer placeHolderComponent = null;

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
        }

        @Override
        public void layoutClick(MouseEventDetails mouseDetails,
                Connector clickedConnector) {
            fireEvent(LayoutClickEvent.createEvent(FancyPanel.this,
                    mouseDetails, clickedConnector));
        }

    };

    /**
     * Create empty panel
     */
    public FancyPanel() {
        this(null);
    }

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
        if (placeHolderComponent == null) {
            CssLayout layout = new CssLayout();
            layout.setStyleName("fancypanel-default-layout");
            layout.setSizeFull();
            placeHolderComponent = layout;
        }
        return placeHolderComponent;
    }

    /**
     * Get currently visible component
     * 
     * @return Currently visible component
     */
    public Component getCurrentComponent() {
        return (Component) getState().currentComponent;
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
        if (components.contains(c)) {
            return;
        }

        components.add(c);

        try {
            super.addComponent(c);
            markAsDirty();

            if (c != placeHolderComponent && placeHolderComponent != null) {
                removeComponent(placeHolderComponent);
                placeHolderComponent = null;
            }

        } catch (IllegalArgumentException e) {
            components.remove(c);
            throw e;
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
        if (!components.remove(c)) {
            System.err.println("Can not remove unknown component");
            return;
        } else {
            super.removeComponent(c);
            if (getState().currentComponent != c) {
                resolveNewCurrent();
            }
        }
    }

    protected void resolveNewCurrent() {
        if (components.isEmpty()) {
            getState().currentComponent = null;
        } else {
            showComponent(components.iterator().next());
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

    /**
     * Enabled or disable horizontal transition
     * 
     * @param enabled
     *            true to enable, false to disable
     */
    public void setRotateTransition(boolean enabled) {
        setRotateTransition(enabled, true);
    }

    /**
     * Enable disable rotate transition. When this transition is enabled
     * conflicting transition will be automatically disabled.
     * 
     * @param enabled
     *            true to enable, false to disable.
     * @param horizontal
     *            true to rotate horizontal, false to rotate vertical. If not
     *            enabled then this value is ignored.
     */
    public void setRotateTransition(boolean enabled, boolean horizontal) {
        if (enabled) {
            setZoomTransition(false);
            getState().rotateTransition = (horizontal ? RotateDirection.HORIZONTAL
                    : RotateDirection.VERTICAL);
        } else {
            getState().rotateTransition = RotateDirection.NONE;
        }
    }

    /**
     * Check if rotate transition is enabled
     * 
     * @return true if enabled
     */
    public boolean isRotateTransition() {
        return getState().rotateTransition != RotateDirection.NONE;
    }

    /**
     * Enable disable fade transition. When this transition is enabled
     * conflicting transition will be automatically disabled.
     * 
     * @param enabled
     *            true to enable, false to disable.
     */
    public void setFadeTransition(boolean enabled) {
        getState().fadeTransition = enabled;
    }

    /**
     * Check if fade transition is enabled
     * 
     * @return
     */
    public boolean isFadeTransition() {
        return getState().fadeTransition;
    }

    /**
     * Enable disable zoom transition. When this transition is enabled
     * conflicting transition will be automatically disabled.
     * 
     * @param enabled
     *            true to enable, false to disable.
     */
    public void setZoomTransition(boolean enabled) {
        if (enabled) {
            setRotateTransition(false);
        }

        getState().zoomTransition = enabled;
    }

    public boolean isZoomTransition() {
        return getState().zoomTransition;
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

}
