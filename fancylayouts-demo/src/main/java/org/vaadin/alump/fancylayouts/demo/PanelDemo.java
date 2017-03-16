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

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo using FancyPanel
 */
@SuppressWarnings("serial")
public class PanelDemo extends VerticalLayout {

    protected FancyPanel panel;
    protected Component introPanel;
    protected Component panelA;
    protected Component panelB;
    protected Component panelC;
    protected CheckBox fade;
    protected CheckBox zoom;
    protected CheckBox rotate;
    protected CheckBox horizontal;

    public PanelDemo() {
        setMargin(true);
        setSizeFull();
        setSpacing(true);

        panel = new FancyPanel(createPanelContentStart());

        Label label = new Label(
                "FancyPanel is panel that offer scrolling and transition when "
                        + "you replace it's content with setContent() call. It's like "
                        + "Vaadin Panel, but I haven't added panel styling DOM "
                        + "elements to keep this clean and simple.");
        addComponent(label);

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
        buttonLayout.addComponent(scrollable);

        fade = new CheckBox("fade");
        buttonLayout.addComponent(fade);

        zoom = new CheckBox("zoom");
        buttonLayout.addComponent(zoom);

        rotate = new CheckBox("rotate");
        buttonLayout.addComponent(rotate);

        horizontal = new CheckBox("horizontal");
        horizontal.setValue(true);
        buttonLayout.addComponent(horizontal);

        panel.setSizeFull();
        addComponent(panel);
        setExpandRatio(panel, 1.0f);

        contA.addClickListener(event -> {
            if (panelA == null) {
                panelA = createPanelContentA();
            }
            panel.showComponent(panelA);
            removeIntro();
        });

        contB.addClickListener(event -> {
            if (panelB == null) {
                panelB = createPanelContentB();
            }
            panel.showComponent(panelB);
            removeIntro();
        });

        contC.addClickListener(event -> {
            if (panelC == null) {
                panelC = createPanelContentD();
            }
            panel.showComponent(panelC);
            removeIntro();
        });

        scrollable.addValueChangeListener(event -> {
            boolean enable = event.getValue();
            // Set scrollable value of panel
            panel.setScrollable(enable);
        });

        fade.addValueChangeListener(event -> {
            boolean enable = event.getValue();
            // Enable/disable transitions
            panel.setFadeTransition(enable);
            updateTransitionCheckboxes();
        });

        zoom.addValueChangeListener(event -> {
            boolean enable = event.getValue();
            // Enable/disable transitions
            panel.setZoomTransition(enable);
            updateTransitionCheckboxes();
        });

        rotate.addValueChangeListener(event -> {
            boolean enable = event.getValue();
            // Enable/disable transitions
            panel.setRotateTransition(enable, horizontal.getValue());
            updateTransitionCheckboxes();
        });

        horizontal.addValueChangeListener(event -> {
            if (rotate.getValue()) {
                boolean enable = event.getValue();
                // Enable/disable transitions
                panel.setRotateTransition(true, enable);
                updateTransitionCheckboxes();
            }
        });

        updateTransitionCheckboxes();
    }

    protected void updateTransitionCheckboxes() {
        fade.setValue(panel.isFadeTransition());
        zoom.setValue(panel.isZoomTransition());
        rotate.setValue(panel.isRotateTransition());
    }

    /**
     * Start content
     * 
     * @return
     */
    private ComponentContainer createPanelContentStart() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label guide = new Label(
                "Please select content shown in panel from the buttons above");
        layout.addComponent(guide);

        introPanel = layout;
        return layout;
    }

    private void removeIntro() {
        if (introPanel != null) {
            System.out.println("Remove intro page");
            panel.removeComponent(introPanel);
            introPanel = null;
        }
    }

    /**
     * Sample content for panel
     * 
     * @return
     */
    private ComponentContainer createPanelContentA() {

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        Label label = new Label(LOREM_STR);
        label.setWidth(100, Unit.PERCENTAGE);
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
     * 
     * @return
     */
    private ComponentContainer createPanelContentB() {

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        Label label = new Label(BECON_STR);
        label.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(label);

        Embedded image = new Embedded();
        image.setSource(new ExternalResource(
                "http://misc.siika.fi/kaljanhimo.jpg"));
        image.setWidth("300px");
        image.setHeight("187px");
        layout.addComponent(image);
        layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

        Label label2 = new Label(BECON_STR);
        label2.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(label2);

        return layout;

    }

    /**
     * Sample content with simple table (disabled as table is so broken in
     * Vaadin 7). To get table work you probably need some special timer.
     * 
     * @return
     */
    // private ComponentContainer createPanelContentC() {
    // VerticalLayout layout = new VerticalLayout();
    // layout.setWidth("100%");
    // layout.setMargin(true);
    // layout.setSpacing(true);
    //
    // Label label = new Label ("Table is quite broken in Vaadin 7?");
    // layout.addComponent(label);
    //
    // Table table = new Table();
    // table.setWidth("400px");
    // table.setHeight("500px");
    // table.addContainerProperty("Name", String.class, "");
    // table.addContainerProperty("Phone Number", String.class, "");
    // table.addItem(new Object[] { "Matti Meikäläinen", "555 234 2344" },
    // "Matti");
    // table.addItem(new Object[] { "Donald Duck", "555 332 7782" }, "Donald");
    //
    // layout.addComponent(table);
    //
    // return layout;
    // }

    private ComponentContainer createPanelContentD() {
        CssLayout layout = new CssLayout();
        layout.addStyleName("demo-panel-d");
        layout.setWidth("100%");
        layout.setHeight("100%");

        Image image = new Image();
        image.setSource(new ThemeResource("images/meme.jpg"));
        image.addStyleName("demo-meme");
        layout.addComponent(image);

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
