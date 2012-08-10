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
        		"FancyNotifications is example use case of FancyCssLayout. " +
        		"It's also useful if you want to have desktop style (OSX) " +
        		"nofications to your web application.");
        addComponent(info);
        
        HorizontalLayout optionLayout = new HorizontalLayout();
        optionLayout.setSpacing(true);
        addComponent (optionLayout);

        timeout = new TextField("Close timeout in millisecs");
        timeout.setDebugId("id-timeout-textfield");
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
        clickCloseCB.setDebugId("id-click-close-checkbox");
        clickCloseCB.setImmediate(true);
        clickCloseCB.setDescription("Close notifications when clicked");
        optionLayout.addComponent(clickCloseCB);
        clickCloseCB.addListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				notifications.setClickClose((Boolean) event.getProperty().getValue());
			}
		});
        
        CheckBox clickNotificationCB = new CheckBox ("Notify clicks");
        clickNotificationCB.setDebugId("id-notify-clicks-checkbox");
        clickNotificationCB.setImmediate(true);
        clickNotificationCB.setDescription("Make new notification when notification made by these buttons are clicked.");
        optionLayout.addComponent(clickNotificationCB);
        clickNotificationCB.addListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				clickNotifications = (Boolean) event.getProperty().getValue();
			}
		});
        
        CheckBox defaultIconCB = new CheckBox ("Use default icon");
        defaultIconCB.setDebugId("id-default-icon-checkbox");
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
        buttonLayout.setCaption("Add notifications");
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        final Button hello = new Button("Single line");
        hello.setDebugId("id-single-line-button");
        hello.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(hello, "Hello World!");
            }
        });
        buttonLayout.addComponent(hello);

        final Button lorem = new Button("Two lines");
        lorem.setDebugId("id-two-lines-button");
        lorem.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(lorem, "Lorem ipsum",
                        "foo bar lorem ipsum bar foo");
            }
        });
        buttonLayout.addComponent(lorem);

        final Button vaadin = new Button("Icon");
        vaadin.setDebugId("id-icon-button");
        vaadin.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(vaadin, "Vaadin",
                        "http://www.vaadin.com",
                        new ThemeResource("images/reindeer.png"));
            }
        });
        buttonLayout.addComponent(vaadin);

        final Button styled = new Button("Styled");
        styled.setDebugId("id-styled-button");
        styled.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(styled, "I'm special!",
                        "...I have fancy colors and fonts!", null,
                        "demo-special-notification");
            }
        });
        buttonLayout.addComponent(styled);
        
        final Button longText = new Button("Long text");
        longText.setDebugId("id-long-text-button");
        longText.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(longText,
            			"Very long title is very long. If it were any longer it would not be a title anymore.",
                        "Also description is really long, nobody knows why, but it's still long! You see? Well, trust me. This is long!");
            }
        });
        buttonLayout.addComponent(longText);
        
        final Button messageExample = new Button("Msg example");
        messageExample.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.showNotification(messageExample,
            			"Sami Viitanen",
            			"Thanks for using FancyLayouts! Hopefully you will " +
            			"find this add on useful.",
            			new ThemeResource("images/avatar.png"));
            }
        });
        buttonLayout.addComponent(messageExample);
        
        buttonLayout = new HorizontalLayout();
        buttonLayout.setCaption("Close notifications:");
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);
        
        final Button closeSingle = new Button("Single line");
        closeSingle.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
            	notifications.closeNotification(hello);
            }
        });
        buttonLayout.addComponent(closeSingle);

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
                    		msg = button.getCaption() + " clicked";
                    	} else {
                    		return;
                    	}

                    	NotificationsDemo.this.notifications.showNotification(
                    			null, "click!", msg);
                    }
                });
        
        timeout.setValue(String.valueOf(notifications.getCloseTimeout()));
        
    }
}