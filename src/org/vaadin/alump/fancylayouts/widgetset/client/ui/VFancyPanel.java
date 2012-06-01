package org.vaadin.alump.fancylayouts.widgetset.client.ui;

import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderInformation;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFancyPanel extends GwtFancyPanel implements Paintable, Container {

    protected String paintableId;
    protected ApplicationConnection client;

    private Paintable content = null;

    private String height = "";
    private String width = "";

    protected final static String ATTR_TRANSITIONS = "transitions";

    private final RenderInformation renderInformation = new RenderInformation();

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, false)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        if (uidl.hasAttribute(ATTR_TRANSITIONS)) {
            disableTransitions(!uidl.getBooleanAttribute(ATTR_TRANSITIONS));
        }

        // Render content
        final UIDL layoutUidl = uidl.getChildUIDL(0);
        final Paintable newContent = client.getPaintable(layoutUidl);
        if (newContent != content) {
            if (content != null) {
                client.unregisterPaintable(content);
            }
            setContent((Widget) newContent);
            content = newContent;
        }
        content.updateFromUIDL(layoutUidl, client);

    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        if (!hasChildComponent(oldComponent)) {
            return;
        }

        setContent(newComponent);
        content = (Paintable) newComponent;
    }

    public boolean hasChildComponent(Widget component) {
        if (component != null && content == component) {
            return true;
        } else {
            return false;
        }
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // TODO Auto-generated method stub

    }

    public boolean requestLayout(Set<Paintable> children) {
        client.handleComponentRelativeSize((Widget) content);
        if (height != null && height != "" && width != null && width != "") {
            return true;
        }
        return !renderInformation.updateSize(getElement());
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        int w = 0;
        int h = 0;

        if (width != null && !width.equals("")) {
            w = getOffsetWidth();
            if (w < 0) {
                w = 0;
            }
        }

        if (height != null && !height.equals("")) {
            h = getContentElement().getOffsetHeight();
            if (h < 0) {
                h = 0;
            }
        }

        return new RenderSpace(w, h, true);
    }

    @Override
    public void setWidth(String width) {
        if (this.width.endsWith(width)) {
            return;
        }

        this.width = width;
        super.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        this.height = height;
        super.setHeight(height);
    }

}
