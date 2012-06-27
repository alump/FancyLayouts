/**
 * VFancyImage.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFancyImage extends GwtFancyImage implements Paintable {

    protected ApplicationConnection client;
    protected String paintableId;

    protected final static String ATTR_IMAGE_PREFIX = "image-";
    protected final static String ATTR_AUTOBROWSE_TIMEOUT = "autobrowse-timeout";
    protected final static String ATTR_AUTOBROWSE = "autobrowse";
    protected final static String ATTR_IMAGE_INDEX = "image-index";

    public VFancyImage() {

    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        if (uidl.hasAttribute(ATTR_AUTOBROWSE_TIMEOUT)) {
            setAutoBrowseTimeout(uidl.getIntAttribute(ATTR_AUTOBROWSE_TIMEOUT));
        }

        if (uidl.hasAttribute(ATTR_AUTOBROWSE)) {
            setAutoBrowseEnabled(uidl.getBooleanAttribute(ATTR_AUTOBROWSE));
        }

        int numberOfImages = 0;
        for (int i = 0; true; ++i) {
            String attr = ATTR_IMAGE_PREFIX + String.valueOf(i);
            if (!uidl.hasAttribute(attr)) {
                numberOfImages = i + 1;
                break;
            }
            setImage(getSrc(uidl.getStringAttribute(attr)), i);
        }

        trimImages(numberOfImages);

        if (uidl.hasAttribute(ATTR_IMAGE_INDEX)) {
            showImage(uidl.getIntAttribute(ATTR_IMAGE_INDEX));
        }
    }

    private String getSrc(String vaadinSrc) {
        String url = client.translateVaadinUri(vaadinSrc);
        if (url == null) {
            return "";
        }

        return url;
    }

}
