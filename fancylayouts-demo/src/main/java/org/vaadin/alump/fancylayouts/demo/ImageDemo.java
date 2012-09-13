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

import org.vaadin.alump.fancylayouts.FancyImage;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo using FancyPanel
 */
@SuppressWarnings("serial")
public class ImageDemo extends VerticalLayout {

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
        final Resource resources[] = new Resource[] {
                new ExternalResource("http://misc.siika.fi/fancy-demo1.jpg"),
                new ExternalResource("http://misc.siika.fi/fancy-demo2.jpg"),
                new ExternalResource("http://misc.siika.fi/fancy-demo3.jpg") };
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

        final Button pic1 = new Button("Pic 1");
        buttonLayout.addComponent(pic1);
        pic1.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                // Ask to show image 0
                image.showResource(resources[0]);

            }
        });

        final Button pic2 = new Button("Pic 2");
        buttonLayout.addComponent(pic2);
        pic2.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                // Ask to show image 1
                image.showResource(resources[1]);

            }
        });

        final Button pic3 = new Button("Pic 3");
        buttonLayout.addComponent(pic3);
        pic3.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                // Ask to show image 2
                image.showResource(resources[2]);

            }
        });

        CheckBox auto = new CheckBox("Slide show");
        auto.setImmediate(true);
        buttonLayout.addComponent(auto);

        TextField timeout = new TextField();
        timeout.setValue(String.valueOf(image.getSlideShowTimeout()));
        timeout.setDescription("millisecs");
        buttonLayout.addComponent(timeout);
        timeout.addTextChangeListener(new TextChangeListener() {

            public void textChange(TextChangeEvent event) {
                try {
                    int value = Integer.parseInt(event.getText());
                    // Change slide show value
                    image.setSlideShowTimeout(value);
                } catch (NumberFormatException e) {
                }
            }

        });

        auto.addValueChangeListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                Boolean value = (Boolean) event.getProperty().getValue();
                // Enable/disable slideshow mode
                image.setSlideShowEnabled(value);
                pic1.setEnabled(!value);
                pic2.setEnabled(!value);
                pic3.setEnabled(!value);
            }
        });

    }
}
