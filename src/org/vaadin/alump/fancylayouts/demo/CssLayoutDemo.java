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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo using FancyCssLayout
 */
public class CssLayoutDemo extends VerticalLayout {

    private int layoutCounter = 0;
    private boolean addCssMiddle = false;
    private int clickCounter = 0;
    private int layoutClickCounter = 0;

    public CssLayoutDemo() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        final FancyCssLayout cssLayout = new FancyCssLayout();

        Label todo = new Label(
                "FancyCssLayout adds transitions when you add or remove components from it.");
        todo.setContentMode(Label.CONTENT_XHTML);
        addComponent(todo);

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        addComponent(hLayout);

        Button addContent = new Button("Add new content item");
        hLayout.addComponent(addContent);

        CheckBox middleCbox = new CheckBox("add middle");
        middleCbox.setValue(addCssMiddle);
        hLayout.addComponent(middleCbox);

        CheckBox marginCbox = new CheckBox("slide");
        marginCbox.setValue(cssLayout
                .isTransitionEnabled(FancyTransition.SLIDE));
        hLayout.addComponent(marginCbox);

        final Label counterLabel = new Label(getClickCounterCaption());
        hLayout.addComponent(counterLabel);

        cssLayout.setSizeFull();
        addComponent(cssLayout);
        setExpandRatio(cssLayout, 1.0f);

        for (int i = 0; i < 10; ++i) {
            addCssLayoutContent(cssLayout);
        }

        addContent.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                addCssLayoutContent(cssLayout);
            }
        });

        middleCbox.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                addCssMiddle = (Boolean) event.getProperty().getValue();
            }

        });

        marginCbox.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                cssLayout.setSlideEnabled((Boolean) event.getProperty()
                        .getValue());
            }

        });

        cssLayout.addListener(new LayoutClickListener() {

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
        hLayout.setSpacing(true);
        hLayout.setWidth("100%");
        Button remove = new Button("X");
        remove.addListener(new Button.ClickListener() {

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
