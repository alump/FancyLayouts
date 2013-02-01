/**
 * FancyImageConnector.java (FancyLayouts)
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

import org.vaadin.alump.fancylayouts.gwt.client.GwtFancyImage;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyImageState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.RotateDirection;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyImage.class)
public class FancyImageConnector extends AbstractComponentConnector {

    private final FancyImageClientRpc rpc = new FancyImageClientRpc() {
        @Override
        public void showImage(URLReference image) {
            getWidget().showImage(image.getURL());
        }
    };

    @Override
    public void init() {
        super.init();
        registerRpc(FancyImageClientRpc.class, rpc);
    }

    @Override
    public GwtFancyImage createWidget() {
        return new GwtFancyImage();
    }

    @Override
    public GwtFancyImage getWidget() {
        return (GwtFancyImage) super.getWidget();
    }

    @Override
    public FancyImageState getState() {
        return (FancyImageState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        List<URLReference> urls = getState().images;
        getWidget().trimImages(urls.size());

        for (int i = 0; i < urls.size(); ++i) {
            getWidget().setImage(urls.get(i).getURL(), i);
        }

        getWidget().setAutoBrowseTimeout(getState().timeoutMs);
        getWidget().setAutoBrowseEnabled(getState().autoBrowse);
        getWidget().setFadeImages(getState().fadeTransition);
        getWidget().setRotateImages(
                getState().rotateTransition != RotateDirection.NONE,
                getState().rotateTransition == RotateDirection.HORIZONTAL);
    }
}
