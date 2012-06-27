/**
 * PanelDemo.java (FancyLayouts)
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

import org.vaadin.alump.fancylayouts.FancyPanel;
import org.vaadin.alump.fancylayouts.FancyTransition;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo using FancyPanel
 */
public class PanelDemo extends VerticalLayout {

    public PanelDemo() {
        setMargin(true);
        setSizeFull();
        setSpacing(true);

        final FancyPanel panel = new FancyPanel();

        Label desc = new Label(
                "FancyPanel is panel that offer scrolling and transition when "
                        + "you replace it's content with setContent() call. It's like "
                        + "Vaadin Panel, but I haven't added panel styling DOM "
                        + "elements to keep this clean and simple.");
        addComponent(desc);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        Button contA = new Button("Panel content A");
        buttonLayout.addComponent(contA);

        Button contB = new Button("Panel content B");
        buttonLayout.addComponent(contB);

        Button contC = new Button("Panel content C");
        buttonLayout.addComponent(contC);

        CheckBox scrollable = new CheckBox("scrollable");
        scrollable.setValue(panel.isScrollable());
        scrollable.setImmediate(true);
        buttonLayout.addComponent(scrollable);

        CheckBox transitions = new CheckBox("transitions");
        transitions.setValue(panel.isTransitionEnabled(FancyTransition.FADE));
        transitions.setImmediate(true);
        buttonLayout.addComponent(transitions);

        // Set start content
        panel.setContent(createPanelContentStart());
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(panel, 1.0f);

        contA.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                // Create dynamically new content to panel
                panel.setContent(createPanelContentA());

            }
        });

        contB.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                // Create dynamically new content to panel
                panel.setContent(createPanelContentB());
            }
        });

        contC.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                // Create dynamically new content to panel
                panel.setContent(createPanelContentC());
            }
        });

        scrollable.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                boolean enable = (Boolean) event.getProperty().getValue();
                // Set scrollable value of panel
                panel.setScrollable(enable);
            }
        });

        transitions.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                boolean enable = (Boolean) event.getProperty().getValue();
                // Enable/disable transitions
                panel.setFadeTransitionEnabled(enable);
            }
        });
    }

    /**
     * Start content
     * @return
     */
    private ComponentContainer createPanelContentStart() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label guide = new Label(
                "Please select content shown in panel from the buttons above");
        layout.addComponent(guide);

        return layout;
    }

    /**
     * Sample content for panel
     * @return
     */
    private ComponentContainer createPanelContentA() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Label label = new Label(LOREM_STR);
        label.setWidth("300px");
        layout.addComponent(label);

        Embedded image = new Embedded();
        image.setSource(new ExternalResource(
                "http://misc.siika.fi/fancy-demo1.jpg"));
        image.setWidth("500px");
        image.setHeight("281px");
        layout.addComponent(image);

        return layout;

    }

    /**
     * Sample content with more stuff
     * @return
     */
    private ComponentContainer createPanelContentB() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setWidth("100%");
        layout.setSpacing(true);

        Label label = new Label(BECON_STR);
        layout.addComponent(label);

        Embedded image = new Embedded();
        image.setSource(new ExternalResource(
                "http://misc.siika.fi/kaljanhimo.jpg"));
        image.setWidth("300px");
        image.setHeight("187px");
        layout.addComponent(image);

        Label label2 = new Label(BECON_STR);
        layout.addComponent(label2);

        return layout;

    }

    /**
     * Sample content with simple table
     * @return
     */
    private ComponentContainer createPanelContentC() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Table table = new Table();
        table.addContainerProperty("Name", String.class, "");
        table.addContainerProperty("Phone Number", String.class, "");
        table.addItem(new Object[] { "Matti Meikäläinen", "555 234 2344" },
                "Matti");
        table.addItem(new Object[] { "Donald Duck", "555 332 7782" }, "Donald");

        layout.addComponent(table);

        return layout;
    }

    private static final String LOREM_STR = new String(
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed "
                    + "do eiusmod tempor incididunt ut labore et dolore magna "
                    + "aliqua. Ut enim ad minim veniam, quis nostrud exercitation "
                    + "ullamco laboris nisi ut aliquip ex ea commodo consequat. "
                    + "Duis aute irure dolor in reprehenderit in voluptate velit "
                    + "esse cillum dolore eu fugiat nulla pariatur. Excepteur "
                    + "sint occaecat cupidatat non proident, sunt in culpa qui "
                    + "officia deserunt mollit anim id est laborum.");

    private static final String BECON_STR = new String(
            "Ball tip sausage beef, kielbasa pork loin boudin chicken ham "
                    + "shankle salami fatback short loin. Biltong ham pastrami "
                    + "beef ribs salami swine. Shank meatball ribeye flank, tongue "
                    + "shoulder bresaola chuck. Corned beef ball tip turducken, "
                    + "sirloin swine capicola chuck ground round shoulder "
                    + "bresaola. Cow fatback venison, jerky corned beef brisket "
                    + "swine t-bone flank. Tri-tip meatball ham, flank ribeye "
                    + "venison biltong. Capicola ground round pork chop, flank "
                    + "meatloaf tenderloin leberkas hamburger kielbasa ham tail "
                    + "beef ribs rump chuck. Beef ribs brisket t-bone pork chuck "
                    + "capicola, filet mignon pork belly drumstick short loin. "
                    + "Ground round tail filet mignon shankle pork loin, turducken "
                    + "ball tip beef pork chop rump. Pastrami sirloin strip steak, "
                    + "hamburger spare ribs tenderloin meatball fatback pork ham "
                    + "biltong boudin. Boudin salami bacon chicken spare ribs ham "
                    + "hock. Strip steak ground round salami tri-tip, corned beef "
                    + "chuck beef speck tail turducken boudin prosciutto venison. "
                    + "Pork belly ball tip pastrami boudin, biltong jerky brisket "
                    + "ribeye chuck capicola pork chop bacon tongue. Meatloaf ball "
                    + "tip tenderloin, jerky swine flank shank shoulder short ribs "
                    + "sausage. Ham chicken biltong meatball tail sausage, corned "
                    + "beef bresaola andouille spare ribs. Ball tip hamburger rump "
                    + "speck, jowl shankle bacon. Andouille bacon beef ribs short "
                    + "ribs sirloin, ribeye pork loin shoulder spare ribs capicola "
                    + "pork chop filet mignon biltong shankle bresaola. Pork swine "
                    + "t-bone ground round, venison pork loin kielbasa. Tri-tip "
                    + "chuck kielbasa pork loin pastrami flank. Corned beef pork "
                    + "loin flank, sirloin strip steak cow kielbasa pork ribeye "
                    + "drumstick ham. Beef ribs short loin bresaola, chicken "
                    + "hamburger spare ribs t-bone drumstick pork belly leberkas "
                    + "biltong pork loin pancetta. Strip steak sausage short loin "
                    + "corned beef chicken, brisket pork chop tenderloin. Bresaola "
                    + "ball tip tri-tip shank, jerky pig strip steak sausage "
                    + "ribeye tongue filet mignon. Tongue meatball ribeye filet "
                    + "mignon, andouille pastrami fatback jerky cow t-bone bacon "
                    + "drumstick turkey tenderloin pork loin. Chicken capicola "
                    + "sirloin t-bone short loin meatball. Does your lorem ipsum "
                    + "text long for something a little meatier? Give our "
                    + "generator a try… it’s tasty!");
}
