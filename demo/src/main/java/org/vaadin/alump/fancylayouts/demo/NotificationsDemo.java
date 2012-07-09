package org.vaadin.alump.fancylayouts.demo;

import org.vaadin.alump.fancylayouts.FancyNotifications;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class NotificationsDemo extends VerticalLayout {
	
	private FancyNotifications notifications;
	private boolean clickNotifications = false;
	private final TextField timeout;

    public NotificationsDemo() {

        setMargin(true);
        setSpacing(true);

        Label info = new Label(
                "FancyNotifications is implemented above FancyCssLayout. It offers you to way to have fancy notifications in your web app. This component is still under development!");
        addComponent(info);
        
        HorizontalLayout optionLayout = new HorizontalLayout();
        optionLayout.setSpacing(true);
        addComponent (optionLayout);

        timeout = new TextField("Close timeout in millisecs");
        timeout.setDescription("This value only applies to new notifications made.");
        timeout.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                try {
                    int value = Integer.valueOf((String) event.getProperty()
                            .getValue());
                    notifications.setCloseTimeout(value);
                } catch (NumberFormatException e) {
                }
                timeout.setValue(String.valueOf(notifications
                        .getCloseTimeout()));
            }
        });
        optionLayout.addComponent(timeout);
        
        CheckBox clickCloseCB = new CheckBox ("Click close notifications");
        clickCloseCB.setImmediate(true);
        clickCloseCB.setDescription("Close notifications when clicked");
        optionLayout.addComponent(clickCloseCB);
        clickCloseCB.addListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				notifications.setClickClose((Boolean) event.getProperty().getValue());
			}
		});
        
        CheckBox clickNotificationCB = new CheckBox ("Notify clicks");
        clickNotificationCB.setImmediate(true);
        clickNotificationCB.setDescription("Make new notification when notification made by these buttons are clicked.");
        optionLayout.addComponent(clickNotificationCB);
        clickNotificationCB.addListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				clickNotifications = (Boolean) event.getProperty().getValue();
			}
		});
        
        CheckBox defaultIconCB = new CheckBox ("Use default icon");
        defaultIconCB.setImmediate(true);
        defaultIconCB.setDescription("Use default icon in notifications without defined icon");
        optionLayout.addComponent(defaultIconCB);
        defaultIconCB.addListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				if ((Boolean) event.getProperty().getValue()) {
					notifications.setDefaultIcon(new ThemeResource("images/vaadin.png"));
				} else {
					notifications.setDefaultIcon(null);
				}
			}
		});

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        final Button hello = new Button("Single line");
        hello.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(hello, "Hello World!");
            }
        });
        buttonLayout.addComponent(hello);

        final Button lorem = new Button("Two lines");
        lorem.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(lorem, "Lorem ipsum",
                        "foo bar lorem ipsum bar foo");
            }
        });
        buttonLayout.addComponent(lorem);

        final Button vaadin = new Button("Icon");
        vaadin.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(vaadin, "Vaadin",
                        "http://www.vaadin.com",
                        new ThemeResource("images/reindeer.png"));
            }
        });
        buttonLayout.addComponent(vaadin);

        final Button styled = new Button("Styled");
        styled.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(styled, "I'm special!",
                        "...I have fancy colors!", null,
                        "demo-special-notification");
            }
        });
        buttonLayout.addComponent(styled);
        
        final Button longText = new Button("Long text");
        longText.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(styled, "Very long title is very long, you know!",
                        "Also description is really long, nobody knows why, but it's still long!");
            }
        });
        buttonLayout.addComponent(longText);

    }
    
    public void init (FancyNotifications notifications) {
		this.notifications = notifications;
		
        notifications.addListener(
                new FancyNotifications.NotificationsListener() {

                    public void notificationClicked(Object id) {
                    	
                    	if (!clickNotifications) {
                    		return;
                    	}
                    	
                    	String msg;
                    	if (id != null && id instanceof Button) {
                    		Button button = (Button) id;
                    		msg = "Notification " + button.getCaption()
                                    + " clicked";
                    	} else {
                    		return;
                    	}

                    	NotificationsDemo.this.notifications.showNotification(null, msg);
                    }
                });
        
        timeout.setValue(String.valueOf(notifications.getCloseTimeout()));
        
    }
}
