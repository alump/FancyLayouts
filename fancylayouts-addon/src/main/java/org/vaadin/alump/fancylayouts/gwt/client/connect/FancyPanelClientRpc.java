package org.vaadin.alump.fancylayouts.gwt.client.connect;

import com.vaadin.shared.communication.ClientRpc;

public interface FancyPanelClientRpc extends ClientRpc {
	public void scrollTop (int top);
	public void scrollLeft (int left);
}
