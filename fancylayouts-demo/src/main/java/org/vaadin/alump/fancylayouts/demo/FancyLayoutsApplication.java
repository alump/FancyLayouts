/**
 * FancyLayoutsApplication.java (FancyLayouts)
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

import org.vaadin.alump.fancylayouts.FancyNotifications;

import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 * Demo application using FancyLayouts components
 */
public class FancyLayoutsApplication extends Application {

    FancyNotifications notifications;

    @Override
    public void init() {

        setTheme("demo");

        Window mainWindow = new Window(
                "FancyLayouts Demo Application - version 0.3-SNAPSHOT");
        mainWindow.setContent(buildLayout());
        setMainWindow(mainWindow);
    }

    private ComponentContainer buildLayout() {

        CssLayout topLayout = new CssLayout();
        topLayout.setSizeFull();

        TabSheet tabs = new TabSheet();
        topLayout.addComponent(tabs);
        tabs.setSizeFull();

        tabs.addTab(buildWelcome(), "Welcome");
        tabs.addTab(new ImageDemo(), "FancyImage");
        tabs.addTab(new PanelDemo(), "FancyPanel");
        tabs.addTab(new CssLayoutDemo(), "FancyLayout");
        
        NotificationsDemo notDemo = new NotificationsDemo();
        tabs.addTab(notDemo, "FancyNotifications");
        
        notifications = new FancyNotifications();
        topLayout.addComponent(notifications);
        
        notDemo.init(notifications);

        return topLayout;
    }

    private ComponentContainer buildWelcome() {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth("100%");

        Label header = new Label(
                "This is online demo for FancyLayouts Vaadin AddOn.");
        header.addStyleName("demo-header");
        layout.addComponent(header);

        StringBuilder sb = new StringBuilder();
        sb.append("FancyLayouts adds transitions to UI when you replace content with new. This allows you to have fancier UI in your vaadin based application.");
        sb.append(" Currently package contains Image widget that can be used to present multiple images in one component slot. And Panel widget which is useful");
        sb.append(" if you have to replace content inside your UI often.");

        Label desc = new Label(sb.toString());
        desc.addStyleName("demo-desc");
        layout.addComponent(desc);
        layout.setExpandRatio(desc, 1.0f);

        Link link = new Link(
                "Source code of this demo application",
                new ExternalResource(
                        "https://github.com/alump/FancyLayouts/blob/master/src/org/vaadin/alump/fancylayouts/demo/FancyLayoutsApplication.java"));
        layout.addComponent(link);

        Button sourceLink = new Button();
        sourceLink.addStyleName(BaseTheme.BUTTON_LINK);

        return layout;
    }

    public FancyNotifications getNotifications() {
        return notifications;
    }

}
