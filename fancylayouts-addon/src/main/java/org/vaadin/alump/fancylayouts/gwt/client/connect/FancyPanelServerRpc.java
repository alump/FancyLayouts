package org.vaadin.alump.fancylayouts.gwt.client.connect;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.shared.ui.LayoutClickRpc;

public interface FancyPanelServerRpc extends LayoutClickRpc, ServerRpc {
	public void scrollTop(int top);
	public void scrollLeft(int left);
	public void hidden(Connector child);
}
