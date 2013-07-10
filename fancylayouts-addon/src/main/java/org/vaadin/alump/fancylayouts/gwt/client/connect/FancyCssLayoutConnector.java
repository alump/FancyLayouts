/**
 * FancyCssLayoutConnector.java (FancyLayouts)
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

import org.vaadin.alump.fancylayouts.gwt.client.GwtFancyCssLayout;
import org.vaadin.alump.fancylayouts.gwt.client.model.FancyRemover;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyCssLayoutState;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.Util;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.client.ui.LayoutClickEventHandler;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.LayoutClickRpc;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyCssLayout.class)
public class FancyCssLayoutConnector extends AbstractLayoutConnector {

    protected final FancyCssLayoutServerRpc cssServerRpc = RpcProxy.create(
            FancyCssLayoutServerRpc.class, this);

    private final FancyCssLayoutClientRpc clientRpc = new FancyCssLayoutClientRpc() {

        @Override
        public void fancyRemove(Connector child) {
            Widget widget = ((ComponentConnector) child).getWidget();
            getWidget().fancyRemove(widget);
        }

    };

    private final LayoutClickEventHandler clickEventHandler = new LayoutClickEventHandler(
            this) {

        @Override
        protected ComponentConnector getChildComponent(
                com.google.gwt.user.client.Element element) {
            return Util.getConnectorForElement(getConnection(), getWidget(),
                    element);
        }

        @Override
        protected LayoutClickRpc getLayoutClickRPC() {
            return getRpcProxy(FancyCssLayoutServerRpc.class);
        }
    };

    @Override
    public void init() {
        super.init();
        registerRpc(FancyCssLayoutClientRpc.class, clientRpc);

        attachFancyRemover(getWidget());
    }

    protected void attachFancyRemover(GwtFancyCssLayout widget) {
        widget.setFancyRemover(new FancyRemover() {

            @Override
            public void remove(Widget widget) {
                cssServerRpc.remove(findConnectorWithElement(widget
                        .getElement()));
            }
        });
    }

    @Override
    public GwtFancyCssLayout getWidget() {
        return (GwtFancyCssLayout) super.getWidget();
    }

    @Override
    public FancyCssLayoutState getState() {
        return (FancyCssLayoutState) super.getState();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        clickEventHandler.handleEventHandlerRegistration();
        getWidget().setHorizontalMarginTransitionEnabled(
                getState().horMarginTransition);
        getWidget().setVerticalMarginTransitionEnabled(
                getState().verMarginTransition);
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        // super.onConnectorHierarchyChange(event);

        // Remove old children
        for (ComponentConnector child : event.getOldChildren()) {
            if (child.getParent() != this) {
                Widget widget = child.getWidget();
                if (widget.isAttached()) {
                    getWidget().remove(widget);
                }
            }
        }

        // Add or move children
        int index = 0;
        for (ComponentConnector child : getChildComponents()) {
            getWidget().addOrMove(child.getWidget(), index);
            ++index;
        }
    }

    protected ComponentConnector findConnectorWithElement(Element element) {
        return Util.getConnectorForElement(getConnection(),
                (Widget) getWidget(),
                (com.google.gwt.user.client.Element) element);
    }

}
