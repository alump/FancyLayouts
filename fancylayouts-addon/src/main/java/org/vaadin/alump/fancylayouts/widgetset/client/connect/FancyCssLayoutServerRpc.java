package org.vaadin.alump.fancylayouts.widgetset.client.connect;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.shared.ui.LayoutClickRpc;

public interface FancyCssLayoutServerRpc extends LayoutClickRpc, ServerRpc {
	/**
	 * Client side asked to final removal of child
	 * @param child Child removed
	 */
	public void remove(Connector child);
}
