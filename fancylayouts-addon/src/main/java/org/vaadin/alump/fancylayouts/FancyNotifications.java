/**
 * FancyNotifications.java (FancyLayouts)
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
import java.util.Iterator;
import java.util.List;

import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState.Position;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Easy way to add notification bubbles to your web application.
 */
@SuppressWarnings("serial")
public class FancyNotifications extends FancyCssLayout {

    protected List<NotificationsListener> listeners = new ArrayList<NotificationsListener>();
    protected boolean closeWhenClicked = false;
    protected Resource defaultIcon = null;

    /**
     * Interface for notification listeners
     */
    public interface NotificationsListener {
        /**
         * Called when notification is clicked (if it has non null id)
         * 
         * @param id
         *            ID of notification
         */
        public void notificationClicked(Object id);
    }

    /**
     * Construct new FancyNotifications
     */
    public FancyNotifications() {
        super.addLayoutClickListener(layoutClickListener);
        getState().horMarginTransition = false;
    }

    protected LayoutEvents.LayoutClickListener layoutClickListener = new LayoutEvents.LayoutClickListener() {

        @Override
        public void layoutClick(LayoutClickEvent event) {

            if (listeners.isEmpty()) {
                return;
            }

            Component component = event.getChildComponent();
            while (component != null && component != FancyNotifications.this) {
                if (component instanceof FancyNotification) {
                    break;
                }
                component = component.getParent();
            }

            if (component != null && component != FancyNotifications.this) {
                AbstractComponent aComponent = (AbstractComponent) component;
                Object id = aComponent.getData();
                if (id != null) {
                    for (NotificationsListener listener : listeners) {
                        listener.notificationClicked(id);
                    }
                }
                if (closeWhenClicked) {
                    fancyRemoveComponent(component);
                }
            }

        }

    };

    @Override
    protected FancyNotificationsState getState() {
        return (FancyNotificationsState) super.getState();
    }

    protected Component getNotification(Object id) {
        Iterator<Component> iter = this.iterator();
        while (iter.hasNext()) {
            Component child = iter.next();
            if (child instanceof AbstractComponent) {
                Object data = ((AbstractComponent) child).getData();
                if (data != null && data.equals(id)) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Show notification with text
     * 
     * @param title
     *            Title text
     */
    public void showNotification(Object id, String description) {
        showNotification(id, null, description, null, null);
    }

    /**
     * Show notification with title and description texts
     * 
     * @param id
     *            ID of notification, given back in events
     * @param title
     *            Title text
     * @param description
     *            Description text
     */
    public void showNotification(Object id, String title, String description) {
        showNotification(id, title, description, null, null);
    }

    /**
     * Show notification with title and description texts and an icon
     * 
     * @param id
     *            ID of notification, given back in events
     * @param title
     *            Title text
     * @param description
     *            Description text
     * @param icon
     *            Icon used in notification
     */
    public void showNotification(Object id, String title, String description,
            Resource icon) {
        showNotification(id, title, description, icon, null);
    }

    /**
     * Show notification with given options
     * 
     * @param id
     *            ID of notification, given back in events
     * @param title
     *            Title text
     * @param description
     *            Description text (null if not needed)
     * @param icon
     *            Icon (null if not needed)
     * @param styleName
     *            Style name for this notification (null if not needed)
     */
    public void showNotification(Object id, String title, String description,
            Resource icon, String styleName) {

        if (title == null && description == null) {
            throw new IllegalArgumentException(
                    "You have to define title or description for notification");
        }

        if (icon == null) {
            icon = defaultIcon;
        }

        showNotification(new FancyNotification(id, title, description, icon,
                styleName));
    }

    /**
     * Allows to construct your own notification as you want. Default options
     * (e.g. icon) will not be applied to notification given to this function.
     * 
     * @param notification
     *            Notification presented.
     */
    public void showNotification(FancyNotification notification) {
        if (getState().position == Position.TOP_RIGHT
                || getState().position == Position.TOP_LEFT) {
            addComponent(notification);
        } else {
            addComponent(notification, 0);
        }
    }

    /**
     * Close notification with ID
     * 
     * @param id
     *            ID of notification
     */
    public void closeNotification(Object id) {
        Component notif = getNotification(id);
        if (notif != null) {
            fancyRemoveComponent(notif);
        }
    }

    /**
     * Get automatic closing timeout in milliseconds.
     * 
     * @return Closing time in milliseconds.
     */
    public int getCloseTimeout() {
        return getState().closeTimeoutMs;
    }

    /**
     * Set automatic closing time in milliseconds.
     * 
     * @param millisecs
     *            Closing time in milliseconds. 0 (no automatic closing is only
     *            accepted if close by clicking is enabled).
     */
    public void setCloseTimeout(int millisecs) {
        if (millisecs < 0) {
            throw new IllegalArgumentException("Negative time not accepted");
        }

        getState().closeTimeoutMs = millisecs;
    }

    /**
     * Add notification listener
     * 
     * @param listener
     *            New listener
     */
    public void addListener(NotificationsListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove notification listener
     * 
     * @param listener
     *            Removed listener
     */
    public void removeListeners(NotificationsListener listener) {
        listeners.remove(listener);
    }

    /**
     * Define if notifications should be closed when clicked/tapped. If you
     * disable clickclose and close timeout is set to 0, then close timeout is
     * set back to default value.
     * 
     * @param closeWhenClicked
     *            true to close notifications when clicked
     */
    public void setClickClose(boolean closeWhenClicked) {
        if (this.closeWhenClicked != closeWhenClicked) {
            this.closeWhenClicked = closeWhenClicked;

            if (closeWhenClicked == false && getCloseTimeout() == 0) {
                setCloseTimeout(3000);
            }

        }
    }

    /**
     * Will notifications be closed when clicked
     */
    public boolean isClickClose() {
        return closeWhenClicked;
    }

    /**
     * Set default icon used in notifications
     * 
     * @param icon
     *            Default icon resource or null if no default icon
     */
    public void setDefaultIcon(Resource icon) {
        defaultIcon = icon;
    }

    /**
     * Get default icon used in notifications
     * 
     * @return Default icon resource or null if not defined
     */
    public Resource getDefaultIcon() {
        return defaultIcon;
    }

    @Override
    public boolean setTransitionEnabled(FancyTransition trans, boolean enabled) {
        // TODO: ugly way
        boolean ret = super.setTransitionEnabled(trans, enabled);
        getState().horMarginTransition = false;
        return ret;
    }

    /**
     * Get position where notification cards are stacked.
     * 
     * @return Position of notifications
     */
    public Position getPosition() {
        return getState().position;
    }

    /**
     * Set position where notification cards are stacked.
     * 
     * @param position
     *            Position of notifications
     */
    public void setPosition(Position position) {
        if (getState().position != position) {
            boolean revert = isTopPosition(getState().position) != isTopPosition(position);
            getState().position = position;
            if (revert) {
                revertChildOrder();
            }
        }
    }

    /**
     * Check if given position is "top position". Used to identify when children
     * has to be reordered.
     * 
     * @param position
     *            Position checked
     * @return true if "top position"
     */
    private boolean isTopPosition(Position position) {
        return (position == Position.TOP_LEFT || position == Position.TOP_RIGHT);
    }
}
