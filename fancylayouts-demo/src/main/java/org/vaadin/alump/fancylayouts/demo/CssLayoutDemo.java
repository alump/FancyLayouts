/**
 * CssLayoutDemo.java (FancyLayouts)
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

import java.util.Iterator;

import org.vaadin.alump.fancylayouts.FancyCssLayout;
import org.vaadin.alump.fancylayouts.FancyTransition;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo using FancyCssLayout
 */
@SuppressWarnings("serial")
public class CssLayoutDemo extends VerticalLayout {

    private int layoutCounter = 0;
    private boolean addCssMiddle = false;
    private int clickCounter = 0;
    private int layoutClickCounter = 0;
    private boolean boxMode = false;

    public CssLayoutDemo() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        final FancyCssLayout cssLayout = new FancyCssLayout();

        Label todo = new Label(
                "FancyCssLayout adds transitions when you add or remove components from it.");
        addComponent(todo);

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        addComponent(hLayout);

        Button addContent = new Button("Add new content item");
        hLayout.addComponent(addContent);

        CheckBox middleCbox = new CheckBox("add middle");
        middleCbox.setImmediate(true);
        middleCbox.setValue(addCssMiddle);
        hLayout.addComponent(middleCbox);

        CheckBox marginCbox = new CheckBox("slide");
        marginCbox.setImmediate(true);
        marginCbox.setValue(cssLayout
                .isTransitionEnabled(FancyTransition.SLIDE));
        hLayout.addComponent(marginCbox);

        CheckBox styleCbox = new CheckBox("cards");
        styleCbox.setImmediate(true);
        styleCbox.setValue(boxMode);
        hLayout.addComponent(styleCbox);

        final Label counterLabel = new Label(getClickCounterCaption());
        hLayout.addComponent(counterLabel);

        cssLayout.setSizeFull();
        addComponent(cssLayout);
        setExpandRatio(cssLayout, 1.0f);

        for (int i = 0; i < 10; ++i) {
            addCssLayoutContent(cssLayout);
        }

        addContent.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                addCssLayoutContent(cssLayout);
            }
        });

        middleCbox.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                addCssMiddle = (Boolean) event.getProperty().getValue();
            }

        });

        marginCbox.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cssLayout.setSlideEnabled((Boolean) event.getProperty()
                        .getValue());
            }

        });

        styleCbox.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                Iterator<Component> iter = cssLayout.iterator();
                while (iter.hasNext()) {
                    Component component = iter.next();
                    if (value) {
                        component.addStyleName("demo-removable-two");
                    } else {
                        component.removeStyleName("demo-removable-two");
                    }
                }
                boxMode = value;
            }

        });

        cssLayout.addLayoutClickListener(new LayoutClickListener() {

            @Override
            public void layoutClick(LayoutClickEvent event) {
                ++clickCounter;
                if (event.getChildComponent() == null) {
                    ++layoutClickCounter;
                }
                counterLabel.setValue(getClickCounterCaption());
            }

        });

    }

    private String getClickCounterCaption() {
        return "layout clicked " + layoutClickCounter + "(" + clickCounter
                + ")";
    }

    private void addCssLayoutContent(final FancyCssLayout layout) {
        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.addStyleName("demo-removable-layout");

        if (boxMode) {
            hLayout.addStyleName("demo-removable-two");
        }

        hLayout.setSpacing(true);
        hLayout.setWidth("100%");
        Button remove = new Button("✖");
        remove.addStyleName("remove-button");
        remove.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                layout.fancyRemoveComponent(hLayout);
            }
        });
        hLayout.addComponent(remove);
        hLayout.setComponentAlignment(remove, Alignment.MIDDLE_CENTER);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setWidth("100%");
        hLayout.addComponent(vLayout);
        hLayout.setExpandRatio(vLayout, 1.0f);

        Label label = new Label("Entry #" + (++layoutCounter));
        label.addStyleName("demo-big-label");
        vLayout.addComponent(label);

        Label label2 = new Label("Lorem ipsum, foo bar?");
        label2.addStyleName("demo-small-label-"
                + String.valueOf(layoutCounter % 4));
        vLayout.addComponent(label2);

        if (addCssMiddle) {
            layout.addComponent(hLayout, layout.getComponentCount() / 2);
        } else {
            layout.addComponent(hLayout);
        }
    }

}
