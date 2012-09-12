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

import java.util.List;

import org.vaadin.alump.fancylayouts.gwt.client.GwtFancyCssLayout;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyCssLayoutState;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyCssLayout.class)
public class FancyCssLayoutConnector extends AbstractComponentContainerConnector {
	
	private FancyCssLayoutClientRpc rpc = new FancyCssLayoutClientRpc() {

		@Override
		public void fancyRemove(Connector child) {
			Widget widget = ((ComponentConnector)child).getWidget();
			getWidget().fancyRemove(widget);
		}
		
	};
	
	@Override
	public void init() {
		super.init();
		registerRpc(FancyCssLayoutClientRpc.class, rpc);
	}
	
	@Override
	public GwtFancyCssLayout createWidget() {
		return new GwtFancyCssLayout();
	}
	
	@Override
	public GwtFancyCssLayout getWidget() {
		return (GwtFancyCssLayout)super.getWidget();
	}
	
	@Override
	public FancyCssLayoutState getState() {
		return (FancyCssLayoutState)super.getState();
	}

	@Override
	public void updateCaption(ComponentConnector connector) {
		// TODO Auto-generated method stub
	}
	
    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        super.onConnectorHierarchyChange(event);
        
        int index = 0;
        for (ComponentConnector child : getChildComponents()) {
            getWidget().addOrMove(child.getWidget(), index++);
        }

        // Detach old child widgets and possibly their caption
        for (ComponentConnector child : event.getOldChildren()) {
            if (child.getParent() == this) {
                // Skip current children
                continue;
            }
            getWidget().remove(child.getWidget());
        }
    }

}
