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
import java.util.List;
import java.util.Map;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

/**
 * Easy way to add notification bubbles to your web application.
 */
@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(org.vaadin.alump.fancylayouts.widgetset.client.ui.VFancyNotifications.class)
public class FancyNotifications extends FancyCssLayout {

    private int closeTimeoutMs = 10000;
    protected List<NotificationsListener> listeners = new ArrayList<NotificationsListener>();

    public interface NotificationsListener {
        public void notificationClicked(Object id);
    }

    public FancyNotifications() {
        super.addListener(layoutClickListener);
    }

    protected LayoutEvents.LayoutClickListener layoutClickListener = new LayoutEvents.LayoutClickListener() {

        public void layoutClick(LayoutClickEvent event) {

            if (listeners.isEmpty()) {
                return;
            }

            Component component = event.getChildComponent();
            while (component != null && component != FancyNotifications.this) {
                if (component instanceof NotificationLayout) {
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
            }

        }

    };

    /**
     * Show notification with title text
     * @param title Title text
     */
    public void showNotification(Object id, String title) {
        showNotification(id, title, null, null, null);
    }

    /**
     * Show notification with title and description texts
     * @param id ID of notification, given back in events
     * @param title Title text
     * @param description Description text
     */
    public void showNotification(Object id, String title, String description) {
        showNotification(id, title, description, null, null);
    }

    /**
     * Show notification with title and description texts and an icon
     * @param id ID of notification, given back in events
     * @param title Title text
     * @param description Description text
     * @param icon Icon used in notification
     */
    public void showNotification(Object id, String title, String description,
            Resource icon) {
        showNotification(id, title, description, icon, null);
    }

    private class NotificationLayout extends CssLayout {
        public NotificationLayout(Object id, String title, String description,
                Resource icon, String styleName) {

            setStyleName("fancy-notification");

            if (id != null) {
                setData(id);
            }

            if (styleName != null) {
                addStyleName(styleName);
            }

            Label titleLabel = new Label(title);
            titleLabel.setStyleName("fancy-notification-title");
            addComponent(titleLabel);

            if (description != null) {
                Label descLabel = new Label(description);
                descLabel.setStyleName("fancy-notification-desc");
                addComponent(descLabel);
            }

            if (icon != null) {
                Embedded iconImage = new Embedded();
                iconImage.setType(Embedded.TYPE_IMAGE);
                iconImage.setStyleName("fancy-notification-icon");
                iconImage.setSource(icon);
                addComponent(iconImage);
            }

        }
    }

    /**
     * Show notification with given options
     * @param id ID of notification, given back in events
     * @param title Title text
     * @param description Description text (null if not needed)
     * @param icon Icon (null if not needed)
     * @param styleName Style name for this notification (null if not needed)
     */
    public void showNotification(Object id, String title, String description,
            Resource icon, String styleName) {

        if (title == null) {
            return;
        }

        NotificationLayout notification = new NotificationLayout(id, title,
                description, icon, styleName);

        addComponent(notification);
    }

    public int getCloseTimeout() {
        return closeTimeoutMs;
    }

    public void setCloseTimeout(int millisecs) {
        if (millisecs > 0 && closeTimeoutMs != millisecs) {
            closeTimeoutMs = millisecs;
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute("close-timeout", closeTimeoutMs);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {

        // mark notification removed
        if (variables.containsKey("remove")) {
        }

        // actual layout removal is done by base class
        super.changeVariables(source, variables);
    }

    public void addListener(NotificationsListener listener) {
        listeners.add(listener);
    }

    public void removeListeners(NotificationsListener listener) {
        listeners.remove(listener);
    }
}
