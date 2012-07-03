package org.vaadin.alump.fancylayouts.demo;

import org.vaadin.alump.fancylayouts.FancyNotifications;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class NotificationsDemo extends VerticalLayout {
	
	private FancyNotifications notifications;

    public NotificationsDemo() {

        setMargin(true);
        setSpacing(true);

        Label info = new Label(
                "FancyNotifications is implemented above FancyCssLayout. It offers you to way to have fancy notifications in your web app. This component is still under development!");
        addComponent(info);

        final TextField timeout = new TextField("Close timeout in millisecs");
        timeout.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                try {
                    int value = Integer.valueOf((String) event.getProperty()
                            .getValue());
                    getNotifications().setCloseTimeout(value);
                } catch (NumberFormatException e) {
                }
                timeout.setValue(String.valueOf(getNotifications()
                        .getCloseTimeout()));
            }
        });
        addComponent(timeout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        final Button hello = new Button("Single line");
        hello.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getNotifications().showNotification(hello, "Hello World!");
            }
        });
        buttonLayout.addComponent(hello);

        final Button lorem = new Button("Two lines");
        lorem.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getNotifications().showNotification(lorem, "Lorem ipsum",
                        "foo bar lorem ipsum bar foo");
            }
        });
        buttonLayout.addComponent(lorem);

        final Button vaadin = new Button("Icon");
        vaadin.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getNotifications().showNotification(vaadin, "Vaadin",
                        "http://www.vaadin.com",
                        new ThemeResource("images/reindeer.png"));
            }
        });
        buttonLayout.addComponent(vaadin);

        final Button styled = new Button("Styled");
        styled.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getNotifications().showNotification(styled, "I'm special!",
                        "...I have fancy colors!", null,
                        "demo-special-notification");
            }
        });
        buttonLayout.addComponent(styled);

    }

    public FancyNotifications getNotifications() {
    	
    	if (notifications == null) {
    		FancyLayoutsApplication app = (FancyLayoutsApplication) getApplication();
    		notifications = app.getNotifications();
    		

            notifications.addListener(
                    new FancyNotifications.NotificationsListener() {

                        public void notificationClicked(Object id) {
                        	String msg;
                        	if (id != null && id instanceof Button) {
                        		Button button = (Button) id;
                        		msg = "Notification " + button.getCaption()
                                        + " clicked";
                        	} else {
                        		return;
                        	}

                            getNotifications().showNotification(null, msg);
                        }
                    });
    	}
    	
    	return notifications;
    }
}
