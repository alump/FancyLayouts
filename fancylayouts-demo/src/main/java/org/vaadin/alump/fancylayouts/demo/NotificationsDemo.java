package org.vaadin.alump.fancylayouts.demo;

import org.vaadin.alump.fancylayouts.FancyNotification;
import org.vaadin.alump.fancylayouts.FancyNotifications;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState.Position;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class NotificationsDemo extends VerticalLayout {

    private FancyNotifications notifications;
    private boolean clickNotifications = false;
    private final TextField timeout;
    private final ComboBox positionCB;

    public final static int MAX_SLEEP_TIME = 10;
    private int sleepCounter = 0;

    public NotificationsDemo() {

        setMargin(true);
        setSpacing(true);

        Label info = new Label(
                "FancyNotifications is example use case of FancyCssLayout. "
                        + "It's also useful if you want to have desktop style (OSX) "
                        + "nofications to your web application.");
        addComponent(info);

        HorizontalLayout optionLayout = new HorizontalLayout();
        optionLayout.setSpacing(true);
        addComponent(optionLayout);

        timeout = new TextField("Auto close (ms)");
        timeout.setDescription("This value only applies to new notifications made.");
        timeout.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                try {
                    int value = Integer.valueOf((String) event.getProperty()
                            .getValue());
                    notifications.setCloseTimeout(value);
                } catch (NumberFormatException e) {
                }
                timeout.setValue(String.valueOf(notifications.getCloseTimeout()));
            }
        });
        optionLayout.addComponent(timeout);

        CheckBox clickCloseCB = new CheckBox("Click to close");
        clickCloseCB.setImmediate(true);
        clickCloseCB.setDescription("Close notifications when clicked");
        optionLayout.addComponent(clickCloseCB);
        clickCloseCB.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                notifications.setClickClose((Boolean) event.getProperty()
                        .getValue());
            }
        });

        CheckBox clickNotificationCB = new CheckBox("Notify clicks");
        clickNotificationCB.setImmediate(true);
        clickNotificationCB
                .setDescription("Make new notification when notification made by these buttons are clicked.");
        optionLayout.addComponent(clickNotificationCB);
        clickNotificationCB
                .addValueChangeListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        clickNotifications = (Boolean) event.getProperty()
                                .getValue();
                    }
                });

        CheckBox defaultIconCB = new CheckBox("Use default icon");
        defaultIconCB.setImmediate(true);
        defaultIconCB
                .setDescription("Use default icon in notifications without defined icon");
        optionLayout.addComponent(defaultIconCB);
        defaultIconCB
                .addValueChangeListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        if ((Boolean) event.getProperty().getValue()) {
                            notifications.setDefaultIcon(new ThemeResource(
                                    "images/vaadin.png"));
                        } else {
                            notifications.setDefaultIcon(null);
                        }
                    }
                });

        positionCB = new ComboBox("Position");
        positionCB.setImmediate(true);
        for (Position position : Position.values()) {
            positionCB.addItem(position);
        }
        positionCB.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                notifications.setPosition((Position) event.getProperty()
                        .getValue());
            }

        });
        optionLayout.addComponent(positionCB);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setCaption("Add notifications");
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        final Button hello = new Button("Single line");
        hello.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications.showNotification(hello, "Hello World!");
            }
        });
        buttonLayout.addComponent(hello);

        final Button lorem = new Button("Two lines");
        lorem.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications.showNotification(lorem, "Lorem ipsum",
                        "foo bar lorem ipsum bar foo");
            }
        });
        buttonLayout.addComponent(lorem);

        final Button vaadin = new Button("Icon");
        vaadin.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications.showNotification(vaadin, "Vaadin",
                        "http://www.vaadin.com", new ThemeResource(
                                "images/reindeer.png"));
            }
        });
        buttonLayout.addComponent(vaadin);

        final Button styled = new Button("Styled");
        styled.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications.showNotification(styled, "I'm special!",
                        "...I have fancy colors and fonts!", null,
                        "demo-special-notification");
            }
        });
        buttonLayout.addComponent(styled);

        final Button longText = new Button("Long text");
        longText.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications
                        .showNotification(
                                longText,
                                "Very long title is very long. If it were any longer it would not be a title anymore.",
                                "Also description is really long, nobody knows why, but it's still long! You see? Well, trust me. This is long!");
            }
        });
        buttonLayout.addComponent(longText);

        final Button messageExample = new Button("Msg example");
        messageExample.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications.showNotification(messageExample, "Sami Viitanen",
                        "Thanks for using FancyLayouts! Hopefully you will "
                                + "find this add on useful.",
                        new ThemeResource("images/avatar.png"));
            }
        });
        buttonLayout.addComponent(messageExample);

        final Button htmlExample = new Button("HTML example");
        htmlExample.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                FancyNotification notif = new FancyNotification(
                        null,
                        "Hello <span style=\"text-decoration: underline;\">World</span>",
                        "Lorem <span style=\"font-size: 80%;\">ipsum.</span>");
                notif.getTitleLabel().setContentMode(ContentMode.HTML);
                notif.getDescriptionLabel().setContentMode(ContentMode.HTML);
                notifications.showNotification(notif);
            }
        });
        buttonLayout.addComponent(htmlExample);

        buttonLayout = new HorizontalLayout();
        buttonLayout.setCaption("Close notifications:");
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        final Button closeSingle = new Button("Single line");
        closeSingle.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                notifications.closeNotification(hello);
            }
        });
        buttonLayout.addComponent(closeSingle);

        // Push code, not working yet, so left out
        buttonLayout = new HorizontalLayout();
        buttonLayout.setCaption("Push tests:");
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        Button pushMe = new Button("Push notification (random delay)");
        buttonLayout.addComponent(pushMe);
        pushMe.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Thread myThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        final int sleepNow = 1 + (int) Math.round(Math.random()
                                * MAX_SLEEP_TIME);
                        final int sleepId = ++sleepCounter;
                        try {
                            Thread.sleep(sleepNow * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getUI().access(new Runnable() {

                            @Override
                            public void run() {
                                notifications.showNotification(null, "Sleep #"
                                        + sleepId, "That was nice " + sleepNow
                                        + " seconds of sleep!");

                            }

                        });

                    }

                });
                myThread.start();

            }

        });
    }

    public void init(FancyNotifications notifications) {
        this.notifications = notifications;

        notifications
                .addListener(new FancyNotifications.NotificationsListener() {

                    @Override
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
        positionCB.setValue(notifications.getPosition());
        positionCB.setNullSelectionAllowed(false);
    }
}
