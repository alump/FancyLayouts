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
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyPanelState;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyPanel.class)
public class FancyPanelConnector extends AbstractComponentContainerConnector {
	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public GwtFancyPanel createWidget() {
		return new GwtFancyPanel();
	}
	
	@Override
	public GwtFancyPanel getWidget() {
		return (GwtFancyPanel)super.getWidget();
	}
	
	@Override
	public FancyPanelState getState() {
		return (FancyPanelState)super.getState();
	}

	@Override
	public void updateCaption(ComponentConnector connector) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		
		getWidget().setScrollable(getState().scrollable);
	}
	
    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        super.onConnectorHierarchyChange(event);
        
        for (ComponentConnector child : getChildComponents()) {
        	getWidget().setContent(child.getWidget());
        }
    }

}
