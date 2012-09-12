package org.vaadin.alump.fancylayouts.gwt.client.connect;

import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.URLReference;

public interface FancyImageClientRpc extends ClientRpc {
	
	public void showImage(URLReference image);

}
