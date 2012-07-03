package org.vaadin.alump.fancylayouts.widgetset.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFancyNotifications extends VFancyCssLayout {

    private int closeTimeout = 10000;

    public VFancyNotifications() {
        super();

        this.setStylePrimaryName("fancy-notifications");
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasAttribute("close-timeout")) {
            closeTimeout = uidl.getIntAttribute("close-timeout");
        }

    }

    @Override
    public void add(Widget widget, int index) {
        super.add(widget, index);

        final Widget removeWidget = widget;

        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {

            public boolean execute() {
                VFancyNotifications.this.fancyRemove(removeWidget);
                return false;
            }

        }, closeTimeout);
    }

}
