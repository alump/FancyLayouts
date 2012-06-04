package org.vaadin.alump.fancylayouts;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractComponent;

/**
 * FancyImage can be used to present multiple images in one place. For example
 * present pictures of product. It adds transtions when presented image is
 * changed. It also adds slideshow mode.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.alump.fancylayouts.widgetset.client.ui.VFancyImage.class)
public class FancyImage extends AbstractComponent {

    private static final long serialVersionUID = -3112985073589754117L;
    private final List<Resource> images = new ArrayList<Resource>();
    private boolean autoBrowseEnabled = false;
    private int autoBrowseTimeout = 3000;
    private int showImageIndex = 0;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {

        for (int i = 0; i < images.size(); ++i) {
            String attr = "image-" + String.valueOf(i);
            target.addAttribute(attr, images.get(i));
        }

        target.addAttribute("autobrowse", autoBrowseEnabled);

        if (autoBrowseEnabled) {
            target.addAttribute("autobrowse-timeout", autoBrowseTimeout);
        } else {
            target.addAttribute("image-index", showImageIndex);
        }
    }

    /**
     * Add new image resource
     * @param resource New image resource
     */
    public void addResource(Resource resource) {
        images.add(resource);
        requestRepaint();
    }

    /**
     * Show given image (and add it if not added)
     * @param resource Image shown
     */
    public void showResource(Resource resource) {
        int index = images.indexOf(resource);
        if (index < 0) {
            addResource(resource);
            index = images.size() - 1;
        }

        if (showImageIndex != index) {
            showImageIndex = index;
            requestRepaint();
        }
    }

    /**
     * Remove given image
     * @param resource Image removed
     */
    public void removeResource(Resource resource) {
        if (images.remove(resource)) {
            requestRepaint();
        }
    }

    /**
     * Enable slide show mode
     * @param enabled true to enable slide show, false to disable
     */
    public void setSlideShowEnabled(boolean enabled) {
        if (autoBrowseEnabled != enabled) {
            autoBrowseEnabled = enabled;
            requestRepaint();
        }
    }

    /**
     * Set how long each image is shown in slide show mode
     * @param millis Time in millisecs (larger than 0)
     */
    public void setSlideShowTimeout(int millis) {
        if (autoBrowseTimeout != millis && millis > 0) {
            autoBrowseTimeout = millis;
            requestRepaint();
        }
    }

    /**
     * Get how long each image is shown in slide show mode
     * @return Time in millisecs
     */
    public int getSlideShowTimeout() {
        return autoBrowseTimeout;
    }

}
