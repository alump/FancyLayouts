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

	/**
	 * Default automatic close timeout in milliseconds.
	 */
	public static final int DEFAULT_CLOSE_TIMEOUT_MS = 10000;
	
    private int closeTimeoutMs = DEFAULT_CLOSE_TIMEOUT_MS;
    protected List<NotificationsListener> listeners = new ArrayList<NotificationsListener>();
    protected boolean closeWhenClicked = false;
    protected Resource defaultIcon = null;

    /**
     * Interface for notification listeners
     */
    public interface NotificationsListener {
    	/**
    	 * Called when notification is clicked (if it has non null id)
    	 * @param id ID of notification
    	 */
        public void notificationClicked(Object id);
    }

    /**
     * Construct new FancyNotifications
     */
    public FancyNotifications() {
        super.addListener(layoutClickListener);
    }

    protected LayoutEvents.LayoutClickListener layoutClickListener = new LayoutEvents.LayoutClickListener() {

        public void layoutClick(LayoutClickEvent event) {
        
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
                if (closeWhenClicked) {
                	fancyRemoveComponent (component);
                }
            }

        }

    };
    
    protected Component getNotification (Object id) {
        Iterator<Component> iter = this.getComponentIterator();
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
     * @param title Title text
     */
    public void showNotification(Object id, String description) {
        showNotification(id, null, description, null, null);
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

            setStyleName("fancy-notif");

            if (id != null) {
                setData(id);
            }

            if (styleName != null) {
                addStyleName(styleName);
            }

            if (title != null) {
	            Label titleLabel = new Label(title);
	            titleLabel.setStyleName("fancy-notif-title");
	            addComponent(titleLabel);
            } else {
               	addStyleName("fancy-notif-notitle");
            }

            if (description != null) {
                Label descLabel = new Label(description);
                descLabel.setStyleName("fancy-notif-desc");
                addComponent(descLabel);
            } else {
               	addStyleName("fancy-notif-nodesc");
            }

            if (icon != null) {
                Embedded iconImage = new Embedded();
                iconImage.setType(Embedded.TYPE_IMAGE);
                iconImage.setStyleName("fancy-notif-icon");
                iconImage.setSource(icon);
                addComponent(iconImage);
            } else {
            	addStyleName("fancy-notif-noicon");
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
    	
    	if (title == null && description == null) {
    		throw new IllegalArgumentException("You have to define title or description");
    	}
        
        if (icon == null) {
        	icon = defaultIcon;
        }

        NotificationLayout notification = new NotificationLayout(id, title,
                description, icon, styleName);

        addComponent(notification);
    }
    
    /**
     * Close notification with ID
     * @param id ID of notification
     */
    public void closeNotification(Object id) {
    	Component notif = getNotification (id);
    	if (notif != null) {
    		fancyRemoveComponent(notif);
    	}
    }

    /**
     * Get automatic closing timeout in milliseconds.
     * @return Closing time in milliseconds.
     */
    public int getCloseTimeout() {
        return closeTimeoutMs;
    }

    /**
     * Set automatic closing time in milliseconds.
     * @param millisecs Closing time in milliseconds. 0 (no automatic closing
     * is only accepted if close by clicking is enabled).
     */
    public void setCloseTimeout(int millisecs) {
    	if (millisecs < 0 || (millisecs < 1 && !closeWhenClicked)) {
    		return;
    	}
    	
        if (closeTimeoutMs != millisecs) {
            closeTimeoutMs = millisecs;
            requestRepaint();
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
        //if (variables.containsKey("remove")) {
        //}

        // actual layout removal is done by base class
        super.changeVariables(source, variables);
    }

    /**
     * Add notification listener
     * @param listener New listener
     */
    public void addListener(NotificationsListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove notification listener
     * @param listener Removed listener
     */
    public void removeListeners(NotificationsListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Define if notifications should be closed when clicked/tapped. If you
     * disable clickclose and close timeout is set to 0, then close timeout
     * is set back to default value.
     * @param closeWhenClicked true to close notifications when clicked
     */
    public void setClickClose (boolean closeWhenClicked) {
    	if (this.closeWhenClicked != closeWhenClicked) {
    		this.closeWhenClicked = closeWhenClicked;
    		
    		if (closeWhenClicked == false && getCloseTimeout() == 0) {
    			setCloseTimeout(DEFAULT_CLOSE_TIMEOUT_MS);
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
     * @param icon Default icon resource or null if no default icon
     */
    public void setDefaultIcon (Resource icon) {
    	defaultIcon = icon;
    }
    
    /**
     * Get default icon used in notifications
     * @return Default icon resource or null if not defined
     */
    public Resource getDefaultIcon() {
    	return defaultIcon;
    }
}
