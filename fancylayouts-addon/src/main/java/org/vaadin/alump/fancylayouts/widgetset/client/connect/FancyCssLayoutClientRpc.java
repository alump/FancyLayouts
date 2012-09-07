package org.vaadin.alump.fancylayouts.widgetset.client.connect;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ClientRpc;

public interface FancyCssLayoutClientRpc extends ClientRpc {
	/**
	 * Ask to start fancy remove of given child. Will return with remove request
	 * to ServerRpc when fancy remove is done.
	 * @param child Child removed
	 */
	public void fancyRemove (Connector child);
}
