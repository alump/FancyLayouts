package org.vaadin.alump.fancylayouts;

import java.util.Iterator;
import java.util.Map;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ActionManager;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * FancyPanel works like Vaadin Panel but it adds transition when content inside
 * it is changed.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.alump.fancylayouts.widgetset.client.ui.VFancyPanel.class)
public class FancyPanel extends AbstractComponentContainer implements
        ComponentContainer.ComponentAttachListener,
        ComponentContainer.ComponentDetachListener, Action.Notifier {

    private static final long serialVersionUID = -8147501423567501688L;
    protected ComponentContainer content;
    protected ActionManager actionManager;
    private boolean transitionsDisabled = false;
    private boolean scrollable = false;

    public FancyPanel() {
        super();
        setContent(null);
    }

    /**
     * Create new panel with given content
     * @param content Content used inside panel
     */
    public FancyPanel(ComponentContainer content) {
        super();
        setContent(content);
    }

    /**
     * Create new panel with given caption
     * @param caption Caption of panel
     */
    public FancyPanel(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Create new panel with content and caption
     * @param content Content of panel
     * @param caption Caption of panel
     */
    public FancyPanel(ComponentContainer content, String caption) {
        this(content);
        setCaption(caption);
    }

    protected ComponentContainer createDefaultContent() {
        CssLayout layout = new CssLayout();
        layout.setStyleName("fancypanel-default-layout");
        layout.setMargin(true);
        layout.setSizeFull();
        return layout;
    }

    /**
     * Get current content of panel
     * @return Current content of panel
     */
    public ComponentContainer getContent() {
        return content;
    }

    /**
     * Set content of panel
     * @param content New content to panel
     */
    public void setContent(ComponentContainer content) {

        if (content == null) {
            content = createDefaultContent();
        }

        if (content == this.content) {
            return;
        }

        if (this.content != null) {
            this.content.setParent(null);
            this.content
                    .removeListener((ComponentContainer.ComponentAttachListener) this);
            this.content
                    .removeListener((ComponentContainer.ComponentDetachListener) this);
        }

        content.setParent(this);
        this.content = content;

        content.addListener((ComponentContainer.ComponentAttachListener) this);
        content.addListener((ComponentContainer.ComponentDetachListener) this);

        requestRepaint();
    }

    @Override
    public void addComponent(Component c) {
        content.addComponent(c);
    }

    @Override
    public void removeAllComponents() {
        content.removeAllComponents();
    }

    @Override
    public void removeComponent(Component c) {
        content.removeComponent(c);
    }

    protected ActionManager getActionManager() {
        if (actionManager == null) {
            actionManager = new ActionManager(this);
        }
        return actionManager;
    }

    public void addActionHandler(Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    public void removeActionHandler(Handler actionHandler) {
        if (actionManager != null) {
            actionManager.removeActionHandler(actionHandler);
        }
    }

    public void replaceComponent(Component oldComponent, Component newComponent) {
        content.replaceComponent(oldComponent, newComponent);
    }

    public Iterator<Component> getComponentIterator() {
        return content.getComponentIterator();
    }

    public <T extends Action & com.vaadin.event.Action.Listener> void addAction(
            T action) {
        getActionManager().addAction(action);
    }

    public <T extends Action & com.vaadin.event.Action.Listener> void removeAction(
            T action) {
        if (actionManager != null) {
            actionManager.removeAction(action);
        }
    }

    public void componentDetachedFromContainer(ComponentDetachEvent event) {
        if (event.getContainer() == content) {
            fireComponentDetachEvent(event.getDetachedComponent());
        }
    }

    public void componentAttachedToContainer(ComponentAttachEvent event) {
        if (event.getContainer() == content) {
            fireComponentAttachEvent(event.getAttachedComponent());
        }
    }

    /**
     * Disable transitions
     * @param disable true to disabled transitions
     */
    public void setTransitionsDisabled(boolean disable) {
        if (transitionsDisabled != disable) {
            transitionsDisabled = disable;
            requestRepaint();
        }
    }

    /**
     * Check if transitions are disabled
     * @return true if disabled
     */
    public boolean isTransitionsDisabled() {
        return transitionsDisabled;
    }

    /**
     * Set FancyPanel scrollable
     * @param scrollable true to make panel scrollable
     */
    public void setScrollable(boolean scrollable) {
        if (this.scrollable != scrollable) {
            this.scrollable = scrollable;
            requestRepaint();
        }
    }

    /**
     * Check if FancyPanel is scrollable
     * @return true if scrollable
     */
    public boolean isScrollable() {
        return scrollable;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        content.paint(target);

        target.addAttribute("transitions", !transitionsDisabled);
        target.addAttribute("scrollable", scrollable);

        if (actionManager != null) {
            actionManager.paintActions(null, target);
        }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {

        super.changeVariables(source, variables);

        // Actions
        if (actionManager != null) {
            actionManager.handleActions(variables, this);
        }
    }

    @Override
    public void attach() {
        requestRepaint();
        if (content != null) {
            content.attach();
        }
    }

    @Override
    public void detach() {
        if (content != null) {
            content.detach();
        }
    }

    @Override
    public void requestRepaintAll() {
        requestRepaint();
        if (getContent() != null) {
            getContent().requestRepaintAll();
        }
    }

}
