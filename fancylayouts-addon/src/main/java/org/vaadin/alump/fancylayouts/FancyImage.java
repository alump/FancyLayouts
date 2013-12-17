/**
 * FancyImage.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts;

import org.vaadin.alump.fancylayouts.gwt.client.connect.FancyImageClientRpc;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyImageState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.RotateDirection;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;

/**
 * FancyImage can be used to present multiple images in one place. For example
 * present pictures of product. It adds transtions when presented image is
 * changed. It also adds slideshow mode.
 */
@SuppressWarnings("serial")
public class FancyImage extends AbstractComponent {

    /**
     * Counter used to generate always unique resource ids
     */
    private int nextResourceIndex = 1;

    @Override
    protected FancyImageState getState() {
        return (FancyImageState) super.getState();
    }

    /**
     * Add new image resource
     * 
     * @param resource
     *            New image resource
     */
    public void addResource(Resource resource) {
        addResourceWithName(resource);
    }

    private String addResourceWithName(Resource resource) {
        String name = "image-" + nextResourceIndex;
        nextResourceIndex += 1;
        setResource(name, resource);

        getState().imageResIds.add(name);
        return name;
    }

    private String resourceToKey(Resource resource) {
        for (String key : getState().imageResIds) {
            Resource res = getResource(key);
            if (res.equals(resource)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Show given image (and add it if not added)
     * 
     * @param resource
     *            Image shown
     */
    public void showResource(Resource resource) {

        String key = resourceToKey(resource);
        if (key == null) {
            key = addResourceWithName(resource);
        }

        getRpcProxy(FancyImageClientRpc.class).showImage(key);
    }

    /**
     * Remove given image
     * 
     * @param resource
     *            Image removed
     */
    public void removeResource(Resource resource) {

        String key = resourceToKey(resource);
        if (key != null) {
            getState().imageResIds.remove(key);
            setResource(key, null);
        }
    }

    /**
     * Enable slide show mode
     * 
     * @param enabled
     *            true to enable slide show, false to disable
     */
    public void setSlideShowEnabled(boolean enabled) {
        getState().autoBrowse = enabled;
    }

    /**
     * Set how long each image is shown in slide show mode
     * 
     * @param millis
     *            Time in millisecs (larger than 0)
     */
    public void setSlideShowTimeout(int millis) {
        getState().timeoutMs = millis;
    }

    /**
     * Get how long each image is shown in slide show mode
     * 
     * @return Time in millisecs
     */
    public int getSlideShowTimeout() {
        return getState().timeoutMs;
    }

    /**
     * Enable or disable fade transitions when shown image is changed. Fade
     * might be enabled/disabled when you change other transition values.
     * 
     * @param enabled
     *            true to enable, false to disable
     */
    public void setFadeTransition(boolean enabled) {
        getState().fadeTransition = enabled;
    }

    /**
     * Check if fade transitions are enabled
     * 
     * @return true if enabled
     */
    public boolean isFadeTransition() {
        return getState().fadeTransition;
    }

    /**
     * Enabled or disable horizontal rotation transitions when shown image is
     * changed
     * 
     * @param enabled
     *            true to enable, false to disable
     */
    public void setRotateTransition(boolean enabled) {
        setRotateTransition(enabled, true);
    }

    /**
     * Enable or disable rotation transition when shown image is changed
     * 
     * @param enabled
     *            true to enable, false to disable
     * @param horizontal
     *            true to rotate horizontally, false to rotate vertically
     */
    public void setRotateTransition(boolean enabled, boolean horizontal) {
        if (enabled) {
            getState().rotateTransition = (horizontal ? RotateDirection.HORIZONTAL
                    : RotateDirection.VERTICAL);
        } else {
            getState().rotateTransition = RotateDirection.NONE;
        }
    }

    /**
     * Check if rotation transitions are enabled
     * 
     * @return true if enabled
     */
    public boolean isRotateTransition() {
        return getState().rotateTransition != RotateDirection.NONE;
    }

}
