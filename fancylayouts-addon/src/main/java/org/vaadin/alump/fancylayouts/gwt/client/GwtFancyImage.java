/**
 * GwtFancyImage.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts.gwt.client;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.alump.fancylayouts.gwt.client.model.BrowserMode;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class GwtFancyImage extends Widget {
    private final List<ImageElement> images = new ArrayList<ImageElement>();
    private int currentIndex = 0;
    private Timer autoBrowseTimer;
    private int autoBrowseTimeoutMs = 3000;

    protected final static String ROTATE_VALUE_VISIBLE = "rotateX(0deg)";
    protected final static String ROTATE_VALUE_HIDDEN = "rotateX(180deg)";
    protected final static String ROTATE_VALUE_DISABLED = "none";

    protected static BrowserMode browserMode;

    public final static String CLASS_NAME = "fancy-image";

    private boolean rotateImages = false;

    public GwtFancyImage() {
        DivElement root = Document.get().createDivElement();
        root.getStyle().setPosition(Position.RELATIVE);
        root.addClassName(CLASS_NAME);
        this.setElement(root);
    }

    private void stopAutoBrowseTimer() {
        if (autoBrowseTimer != null) {
            getAutoBrowseTimer().cancel();
            autoBrowseTimer = null;
        }
    }

    private Timer getAutoBrowseTimer() {
        if (autoBrowseTimer == null) {
            autoBrowseTimer = new Timer() {

                @Override
                public void run() {
                    if (GwtFancyImage.this.isVisible()) {
                        showNextImage();
                    }
                }

            };
        }

        return autoBrowseTimer;
    }

    public void setAutoBrowseEnabled(boolean on) {
        if (on) {
            getAutoBrowseTimer().scheduleRepeating(autoBrowseTimeoutMs);
            getAutoBrowseTimer().run();
        } else {
            stopAutoBrowseTimer();
        }
    }

    public void setAutoBrowseTimeout(int millis) {
        if (millis > 0 && autoBrowseTimeoutMs != millis) {
            autoBrowseTimeoutMs = millis;
            getAutoBrowseTimer().scheduleRepeating(autoBrowseTimeoutMs);
        }
    }

    private ImageElement createImageElement(String url) {
        ImageElement image = Document.get().createImageElement();
        image.setSrc(url);
        // image.getStyle().setDisplay(Display.NONE);
        image.getStyle().setPosition(Position.ABSOLUTE);
        image.getStyle().setLeft(0.0, Unit.PX);
        image.getStyle().setTop(0.0, Unit.PX);
        image.getStyle().setOpacity(0.0);
        return image;
    }

    public void setImage(String url, int index) {
        if (index >= images.size()) {
            addImage(url);
        } else {
            ImageElement replaced = images.get(index);
            if (!replaced.getSrc().equals(url)) {
                replaced.setSrc(url);
            }
        }
    }

    public int addImage(String url) {
        ImageElement image = createImageElement(url);
        images.add(image);
        getElement().appendChild(image);

        if (images.size() == 1) {
            showImage(0);
        }

        applyTransitionStyleNames(image);
        return images.size();
    }

    public void trimImages(int amount) {
        while (images.size() > amount) {
            removeImage(images.get(amount).getSrc());
        }

        if (currentIndex >= images.size()) {
            showImage(0);
        }
    }

    public void removeImage(String url) {
        for (ImageElement image : images) {
            if (image.getSrc().equals(url)) {
                getElement().removeChild(image);
                images.remove(image);
                break;
            }
        }
    }

    public void showImage(String url) {
        for (ImageElement image : images) {
            if (image.getSrc().endsWith(url)) {
                showImage(images.indexOf(image));
                break;
            }
        }
    }

    public void showImage(int index) {

        if (images.isEmpty()) {
            return;
        }

        if (index >= images.size()) {
            index = 0;
        }

        if (index != currentIndex) {
            ImageElement prevImage = images.get(currentIndex);
            ImageElement currentImage = images.get(index);

            currentIndex = index;

            applyTransitionStyleNames(prevImage);
            applyTransitionStyleNames(currentImage);
        } else {
            ImageElement currentImage = images.get(currentIndex);
            applyTransitionStyleNames(currentImage);
        }

    }

    private void showNextImage() {
        int nextIndex = currentIndex + 1;
        if (nextIndex >= images.size()) {
            nextIndex = 0;
        }

        showImage(nextIndex);
    }

    public void setRotateImages(boolean rotate) {
        rotateImages = rotate;

        for (ImageElement element : images) {
            applyTransitionStyleNames(element);
        }
    }

    protected static BrowserMode getBrowserMode() {
        if (browserMode == null) {
            browserMode = BrowserMode.resolve();
        }

        return browserMode;
    }

    protected void applyTransitionStyleNames(ImageElement element) {

        boolean isCurrent = currentIndex == images.indexOf(element);

        if (rotateImages) {
            element.addClassName("rotate-image");
            if (isCurrent) {
                element.getStyle().setProperty(getBrowserMode().getTransform(),
                        ROTATE_VALUE_VISIBLE);
            } else {
                element.getStyle().setProperty(getBrowserMode().getTransform(),
                        ROTATE_VALUE_HIDDEN);
            }
        } else {
            element.removeClassName("rotate-image");
            element.getStyle().setProperty(getBrowserMode().getTransform(),
                    ROTATE_VALUE_DISABLED);
        }

        if (isCurrent) {
            element.getStyle().setOpacity(1.0);
        } else {
            element.getStyle().setOpacity(0.0);
        }

    }
}
