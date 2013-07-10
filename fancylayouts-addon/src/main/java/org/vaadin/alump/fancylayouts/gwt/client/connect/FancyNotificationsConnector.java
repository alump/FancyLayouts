/**
 * FancyNotificationsConnector.java (FancyLayouts)
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

import org.vaadin.alump.fancylayouts.gwt.client.GwtFancyTimedCssLayout;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState.Position;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyNotifications.class)
public class FancyNotificationsConnector extends FancyCssLayoutConnector {

    public final static String STYLE_NAME = "fancy-notifs";
    private String positionStyleName = null;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public FancyNotificationsState getState() {
        return (FancyNotificationsState) super.getState();
    }

    @Override
    public GwtFancyTimedCssLayout createWidget() {
        GwtFancyTimedCssLayout widget = new GwtFancyTimedCssLayout() {
            @Override
            public void setHorizontalMarginTransitionEnabled(boolean enabled) {
                super.setHorizontalMarginTransitionEnabled(false);
            }
        };
        widget.setStylePrimaryName(STYLE_NAME);
        attachFancyRemover(widget);
        return widget;
    }

    @Override
    public GwtFancyTimedCssLayout getWidget() {
        return (GwtFancyTimedCssLayout) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        getWidget().setAutomaticRemoveTimeout(getState().closeTimeoutMs);

        super.onStateChanged(stateChangeEvent);

        if (positionStyleName != null) {
            getWidget().removeStyleName(positionStyleName);
        }
        positionStyleName = STYLE_NAME + "-"
                + generatePositionStyleSuffix(getState().position);
        getWidget().addStyleName(positionStyleName);
    }

    private String generatePositionStyleSuffix(Position position) {
        switch (position) {
        case TOP_RIGHT:
            return "topright";
        case BOTTOM_RIGHT:
            return "bottomright";
        case BOTTOM_LEFT:
            return "bottomleft";
        default:
            return "topleft";
        }
    }

}
