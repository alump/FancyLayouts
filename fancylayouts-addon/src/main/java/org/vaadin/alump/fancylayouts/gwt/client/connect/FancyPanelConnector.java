/**
 * FancyPanelConnector.java (FancyLayouts)
 * 
 * Copyright 2012 Vaadin Ltd, Sami Viitanen <alump@vaadin.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.alump.fancylayouts.gwt.client.connect;

import org.vaadin.alump.fancylayouts.gwt.client.GwtFancyPanel;
import org.vaadin.alump.fancylayouts.gwt.client.model.FadeOutListener;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyPanelState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.RotateDirection;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.Util;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.client.ui.LayoutClickEventHandler;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.LayoutClickRpc;

import java.util.logging.Logger;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyPanel.class)
public class FancyPanelConnector extends AbstractLayoutConnector {

    private final LayoutClickEventHandler clickEventHandler = new LayoutClickEventHandler(
            this) {

        @Override
        protected LayoutClickRpc getLayoutClickRPC() {
            return panelServerRpc;
        }

        @Override
        protected ComponentConnector getChildComponent(
                com.google.gwt.user.client.Element element) {
            return Util.getConnectorForElement(getConnection(), getWidget(),
                    element);
        };
    };

    private final FancyPanelClientRpc clientRpc = new FancyPanelClientRpc() {

        @Override
        public void scrollTop(int top) {
            getWidget().setScrollTop(top);
        }

        @Override
        public void scrollLeft(int left) {
            getWidget().setScrollLeft(left);
        }

    };

    protected final FancyPanelServerRpc panelServerRpc = RpcProxy.create(
            FancyPanelServerRpc.class, this);

    protected final FadeOutListener fancyRemover = new FadeOutListener() {
        @Override
        public void fadeOut(Widget widget) {
            logger.fine("Hidden information sent");
            panelServerRpc
                    .hidden(findConnectorWithElement(widget.getElement()));
        }
    };

    private final static Logger logger = Logger.getLogger(FancyPanelConnector.class.getName());


    @Override
    public void init() {
        super.init();
        registerRpc(FancyPanelClientRpc.class, clientRpc);
    }

    @Override
    public GwtFancyPanel getWidget() {
        return (GwtFancyPanel) super.getWidget();
    }

    @Override
    public FancyPanelState getState() {
        return (FancyPanelState) super.getState();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        clickEventHandler.handleEventHandlerRegistration();

        getWidget().setScrollable(getState().scrollable);
        getWidget().setFade(getState().fadeTransition);
        getWidget().setZoom(getState().zoomTransition);
        getWidget().setRotate(
                getState().rotateTransition != RotateDirection.NONE,
                getState().rotateTransition == RotateDirection.HORIZONTAL);
        getWidget().setFadeOutListener(fancyRemover);

        ComponentConnector currentConnector = (ComponentConnector) getState().currentComponent;
        if (currentConnector != null
                && getWidget().hasWidget(currentConnector.getWidget())) {
            getWidget().setContent(currentConnector.getWidget());
        }

    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {

        // Remove old children
        for (ComponentConnector child : event.getOldChildren()) {
            if (child.getParent() != this) {
                logger.fine("Remove old child widget");
                Widget widget = child.getWidget();
                if (widget != null && widget.isAttached()) {
                    getWidget().remove(widget);
                }
            }
        }

        for (ComponentConnector child : getChildComponents()) {
            try {
                getWidget().add(child.getWidget());
            } catch (Exception e) {
                logger.severe("Failed to add! " + e.getMessage());
            }
        }

        ComponentConnector currentConnector = (ComponentConnector) getState().currentComponent;
        if (currentConnector != null) {
            getWidget().setContent(currentConnector.getWidget());
        }

    }

    protected ComponentConnector findConnectorWithElement(Element element) {
        return Util.getConnectorForElement(getConnection(),
                (Widget) getWidget(),
                (com.google.gwt.user.client.Element) element);
    }

}
