/**
 * ImageDemo.java (FancyLayouts)
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

package org.vaadin.alump.fancylayouts.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.alump.fancylayouts.FancyImage;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo using FancyPanel
 */
@SuppressWarnings("serial")
public class ImageDemo extends VerticalLayout {

    protected CheckBox horizontal;
    private final Set<Component> disableWhenAutoPlay = new HashSet<Component>();

    public ImageDemo() {
        setMargin(true);
        setSpacing(true);
        setWidth("100%");

        Label desc = new Label(
                "FancyImage can be used when you want to present multiple images in one place. This component allows you to select which image to show when or just enabled the automatic slide show mode.");
        addComponent(desc);

        final FancyImage image = new FancyImage();
        image.setWidth("500px");
        image.setHeight("281px");

        // Setting images used
        final List<Resource> resources = getImageResources();
        for (Resource resource : resources) {
            image.addResource(resource);
        }

        // Setting slideshow timeout to 3 seconds (3000 millisecs)
        image.setSlideShowTimeout(3000);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);
        addComponent(image);
        setComponentAlignment(image, Alignment.TOP_CENTER);

        for (int i = 0; i < resources.size(); ++i) {
            final Button button = new Button("Pic " + (i + 1));
            buttonLayout.addComponent(button);
            final Resource res = resources.get(i);
            button.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    image.showResource(res);

                }
            });
            disableWhenAutoPlay.add(button);
        }

        CheckBox auto = new CheckBox("Slide show");
        buttonLayout.addComponent(auto);

        CheckBox fade = new CheckBox("Fade");
        fade.setDescription("Fade image when changing");
        fade.setValue(image.isFadeTransition());
        buttonLayout.addComponent(fade);

        CheckBox rotate = new CheckBox("Rotate");
        rotate.setDescription("Rotate image when changing");
        rotate.setValue(image.isRotateTransition());
        buttonLayout.addComponent(rotate);

        horizontal = new CheckBox("Horizontal");
        horizontal
                .setDescription("Should rotate happen horizontally or vertically.");
        horizontal.setValue(true);
        buttonLayout.addComponent(horizontal);

        TextField timeout = new TextField();
        timeout.setCaption("Slide show millisecs");
        timeout.setValue(String.valueOf(image.getSlideShowTimeout()));
        timeout.setDescription("How many millisec the slideshow shows one image");
        buttonLayout.addComponent(timeout);
        timeout.addValueChangeListener(event -> {
            try {
                int value = Integer.parseInt(event.getValue());
                // Change slide show value
                image.setSlideShowTimeout(value);
            } catch (NumberFormatException e) {
            }
        });

        auto.addValueChangeListener(event -> {
            Boolean value = event.getValue();
            // Enable/disable slideshow mode
            image.setSlideShowEnabled(value);
            for (Component component : disableWhenAutoPlay) {
                component.setEnabled(!value);
            }
        });

        fade.addValueChangeListener(event -> {
            Boolean value = event.getValue();
            image.setFadeTransition(value.booleanValue());
        });

        rotate.addValueChangeListener(event -> {
            Boolean value = event.getValue();
            image.setRotateTransition(value.booleanValue(),
                    horizontal.getValue());
        });

        horizontal.addValueChangeListener(event -> {
            if (image.isRotateTransition()) {
                image.setRotateTransition(true, event.getValue());
            }
        });
    }

    private List<Resource> getImageResources() {
        List<Resource> list = new ArrayList<Resource>();
        list.add(new ExternalResource("http://misc.siika.fi/fancy-demo1.jpg"));
        list.add(new ExternalResource("http://misc.siika.fi/fancy-demo2.jpg"));
        list.add(new ExternalResource("http://misc.siika.fi/fancy-demo3.jpg"));

        // Image is only added if present in file system. Will not be there
        // unless manually added!
        File fileResImage = new File("/tmp/test.png");
        if (fileResImage.exists()) {
            list.add(new FileResource(fileResImage));
        }
        return list;
    }
}
