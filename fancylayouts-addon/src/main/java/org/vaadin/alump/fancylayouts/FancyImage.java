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

import org.vaadin.alump.fancylayouts.widgetset.client.shared.FancyImageState;

import com.vaadin.server.ResourceReference;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;

/**
 * FancyImage can be used to present multiple images in one place. For example
 * present pictures of product. It adds transtions when presented image is
 * changed. It also adds slideshow mode.
 */
@SuppressWarnings("serial")
public class FancyImage extends AbstractComponent {
    
    @Override
    protected FancyImageState createState() {
      return new FancyImageState();
    }
    
    @Override
    protected FancyImageState getState() {
      return (FancyImageState) super.getState();
    }

    /**
     * Add new image resource
     * @param resource New image resource
     */
    public void addResource(Resource resource) {
    	ResourceReference ref = ResourceReference.create(resource, this, null);
        getState().images.add(ref);
    }

    /**
     * Show given image (and add it if not added)
     * @param resource Image shown
     */
    public void showResource(Resource resource) {
    	ResourceReference ref = ResourceReference.create(resource, this, null);
        int index = getState().images.indexOf(ref);
        if (index < 0) {
            addResource(resource);
            index = getState().images.size() - 1;
        }

        getState().imageIndex = index;
    }

    /**
     * Remove given image
     * @param resource Image removed
     */
    public void removeResource(Resource resource) {
    	ResourceReference ref = ResourceReference.create(resource, this, null);
        getState().images.remove(ref);
    }

    /**
     * Enable slide show mode
     * @param enabled true to enable slide show, false to disable
     */
    public void setSlideShowEnabled(boolean enabled) {
    	getState().autoBrowse = enabled;
    }

    /**
     * Set how long each image is shown in slide show mode
     * @param millis Time in millisecs (larger than 0)
     */
    public void setSlideShowTimeout(int millis) {
    	getState().timeoutMs = millis;
    }

    /**
     * Get how long each image is shown in slide show mode
     * @return Time in millisecs
     */
    public int getSlideShowTimeout() {
        return getState().timeoutMs;
    }

}
