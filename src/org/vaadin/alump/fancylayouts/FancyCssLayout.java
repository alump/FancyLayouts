package org.vaadin.alump.fancylayouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * FancyCssLayout is similiar to Vaadin CssLayout. But it also has
 * fancyRemoveComponent() which will add transition to removal. Also when Items
 * are added with addComponent() those will be added with transition.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.alump.fancylayouts.widgetset.client.ui.VFancyCssLayout.class)
public class FancyCssLayout extends AbstractLayout implements
        LayoutClickNotifier, ComponentContainer.ComponentAttachListener,
        ComponentContainer.ComponentDetachListener {

    private static final long serialVersionUID = -5420351316587635883L;
    protected List<Component> components = new ArrayList<Component>();
    protected Set<Component> fancyRemoveComponents = new HashSet<Component>();
    private boolean marginTransition = true;

    public void replaceComponent(Component oldComponent, Component newComponent) {
        if (components.contains(oldComponent)) {
            int index = components.indexOf(oldComponent);
            components.set(index, newComponent);
            super.removeComponent(oldComponent);
            super.addComponent(newComponent);
            requestRepaint();
        }
    }

    public Iterator<Component> getComponentIterator() {
        return components.iterator();
    }

    public void addListener(LayoutClickListener listener) {
        // TODO Auto-generated method stub

    }

    public void removeListener(LayoutClickListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addComponent(Component c) {
        super.addComponent(c);
        components.add(c);
        requestRepaint();
    }

    /**
     * Add widget to specific index
     * @param c Component added
     * @param index Index where component is added
     */
    public void addComponent(Component c, int index) {
        super.addComponent(c);
        components.add(index, c);
        requestRepaint();
    }

    @Override
    public void removeComponent(Component c) {
        if (!components.contains(c)) {
            return;
        }
        components.remove(c);
        fancyRemoveComponents.remove(c);
        super.removeComponent(c);
        requestRepaint();
    }

    /**
     * Like removeComponent but will add transition to removal. Notice that
     * there will be delay on removal when this is used. So it's most likely
     * is not safe to relocate Component to new layout instantly.
     * @param c Component added
     */
    public void fancyRemoveComponent(Component c) {
        if (!components.contains(c)) {
            return;
        }
        if (fancyRemoveComponents.contains(c)) {
            return;
        }

        fancyRemoveComponents.add(c);
        requestRepaint();
    }

    /**
     * Get number of components
     * @return Number of components
     */
    public int getComponentCount() {
        return components.size();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        HashMap<Paintable, String> componentCss = null;
        // Adds all items in all the locations
        for (Component c : components) {
            // Paint child component UIDL
            c.paint(target);
            String componentCssString = getCss(c);
            if (componentCssString != null) {
                if (componentCss == null) {
                    componentCss = new HashMap<Paintable, String>();
                }
                componentCss.put(c, componentCssString);
            }
        }
        if (componentCss != null) {
            target.addAttribute("css", componentCss);
        }

        target.addAttribute("margin-transition", marginTransition);

        if (!fancyRemoveComponents.isEmpty()) {
            int i = 0;
            Iterator<Component> iter = fancyRemoveComponents.iterator();
            while (iter.hasNext()) {
                target.addAttribute("fancy-remove-" + i, iter.next());
                ++i;
            }
            fancyRemoveComponents.clear();
        }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {

        if (variables.containsKey("remove")) {

            Component removable = (Component) variables.get("remove");
            removeComponent(removable);
        }
    }

    protected String getCss(Component c) {
        return null;
    }

    public void componentDetachedFromContainer(ComponentDetachEvent event) {
        Component component = event.getDetachedComponent();
        if (components.contains(component)) {
            fireComponentDetachEvent(component);
        }
    }

    public void componentAttachedToContainer(ComponentAttachEvent event) {
        Component component = event.getAttachedComponent();
        if (components.contains(component)) {
            fireComponentAttachEvent(component);
        }
    }

    /**
     * Do margin transition magic when item are hidden. This can be disabled
     * as it might cause issues in some UIs. Also this requires more performance
     * from browser.
     * @param enabled false to disable margin transition
     */
    public void setMarginTransitionEnabled(boolean enabled) {
        if (marginTransition != enabled) {
            marginTransition = enabled;
            requestRepaint();
        }
    }

    /**
     * Check if margin transitions are enabled
     * @return true if enabled
     */
    public boolean isMarginTransitionEnabled() {
        return marginTransition;
    }

}
