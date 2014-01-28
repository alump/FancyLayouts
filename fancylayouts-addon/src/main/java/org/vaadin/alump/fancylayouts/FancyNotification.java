package org.vaadin.alump.fancylayouts;

import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * Simple CssLayout implementation used to present single notification inside
 * FancyNotifications.
 */
@SuppressWarnings("serial")
public class FancyNotification extends CssLayout {
    private final Label titleLabel = new Label();
    private final Label descLabel = new Label();
    private final Image iconImage = new Image();

    public FancyNotification(Object id, String title) {
        this(id, title, null, null, null);
    }

    public FancyNotification(Object id, String title, String desc) {
        this(id, title, desc, null, null);
    }

    public FancyNotification(Object id, String title, String desc, Resource icon) {
        this(id, title, desc, icon, null);
    }

    public FancyNotification(Object id, String title, String description,
            Resource icon, String styleName) {

        setStyleName("fancy-notif");

        if (id != null) {
            setData(id);
        }

        if (styleName != null) {
            addStyleName(styleName);
        }

        titleLabel.setStyleName("fancy-notif-title");
        titleLabel.setVisible(title != null);
        addComponent(titleLabel);
        if (title != null) {
            titleLabel.setValue(title);
        } else {
            addStyleName("fancy-notif-notitle");
        }

        descLabel.setStyleName("fancy-notif-desc");
        descLabel.setVisible(description != null);
        addComponent(descLabel);
        if (description != null) {
            descLabel.setValue(description);
        } else {
            addStyleName("fancy-notif-nodesc");
        }

        iconImage.setStyleName("fancy-notif-icon");
        iconImage.setVisible(icon != null);
        addComponent(iconImage);
        if (icon != null) {
            iconImage.setSource(icon);
        } else {
            addStyleName("fancy-notif-noicon");
        }

    }

    /**
     * Get label presenting the title of notification
     * 
     * @return Title label
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * Get label presenting the description of notification
     * 
     * @return Description label
     */
    public Label getDescriptionLabel() {
        return descLabel;
    }

    /**
     * Get image presenting the icon of notification
     * 
     * @return Icom image
     */
    public Image getIconImage() {
        return iconImage;
    }
}
