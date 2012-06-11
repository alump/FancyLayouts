package org.vaadin.alump.fancylayouts;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public class FancyLayoutsApplication extends Application {

    private int layoutCounter = 0;

    @Override
    public void init() {

        setTheme("demo");

        Window mainWindow = new Window(
                "FancyLayouts Demo Application - version 0.0.3");
        mainWindow.setContent(buildLayout());
        setMainWindow(mainWindow);
    }

    private ComponentContainer buildLayout() {
        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();

        tabs.addTab(buildWelcome(), "Welcome");
        tabs.addTab(buildFancyImage(), "FancyImage");
        tabs.addTab(buildFancyPanel(), "FancyPanel");
        tabs.addTab(buildFancyLayout(), "FancyLayout");

        return tabs;
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
                        "https://github.com/alump/FancyLayouts/blob/master/src/org/vaadin/alump/fancylayouts/FancyLayoutsApplication.java"));
        layout.addComponent(link);

        Button sourceLink = new Button();
        sourceLink.addStyleName(BaseTheme.BUTTON_LINK);

        return layout;
    }

    private ComponentContainer buildFancyImage() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth("100%");

        Label desc = new Label(
                "FancyImage can be used when you want to present multiple images in one place. This component allows you to select which image to show when or just enabled the automatic slide show mode.");
        layout.addComponent(desc);

        final FancyImage image = new FancyImage();
        image.setWidth("500px");
        image.setHeight("281px");

        final Resource resources[] = new Resource[] {
                new ExternalResource("http://misc.siika.fi/fancy-demo1.jpg"),
                new ExternalResource("http://misc.siika.fi/fancy-demo2.jpg"),
                new ExternalResource("http://misc.siika.fi/fancy-demo3.jpg") };

        for (Resource resource : resources) {
            image.addResource(resource);
        }

        image.setSlideShowTimeout(3000);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.addComponent(buttonLayout);
        layout.addComponent(image);
        layout.setComponentAlignment(image, Alignment.TOP_CENTER);

        final Button pic1 = new Button("Pic 1");
        buttonLayout.addComponent(pic1);
        pic1.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                image.showResource(resources[0]);

            }
        });

        final Button pic2 = new Button("Pic 2");
        buttonLayout.addComponent(pic2);
        pic2.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                image.showResource(resources[1]);

            }
        });

        final Button pic3 = new Button("Pic 3");
        buttonLayout.addComponent(pic3);
        pic3.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
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
        timeout.addListener(new TextChangeListener() {

            public void textChange(TextChangeEvent event) {
                try {
                    int value = Integer.parseInt(event.getText());
                    image.setSlideShowTimeout(value);
                } catch (NumberFormatException e) {
                }
            }

        });

        auto.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                Boolean value = (Boolean) event.getProperty().getValue();
                image.setSlideShowEnabled(value);
                pic1.setEnabled(!value);
                pic2.setEnabled(!value);
                pic3.setEnabled(!value);
            }
        });

        return layout;

    }

    private ComponentContainer buildFancyPanel() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();
        layout.setSpacing(true);

        Label desc = new Label(
                "FancyPanel is panel that offer scrolling and transition when you replace it's content with setContent() call. It's like Vaadin Panel, but I haven't added panel styling DOM elements to keep this clean and simple.");
        layout.addComponent(desc);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.addComponent(buttonLayout);

        Button contA = new Button("Panel content A");
        buttonLayout.addComponent(contA);

        Button contB = new Button("Panel content B");
        buttonLayout.addComponent(contB);

        Button contC = new Button("Panel content C");
        buttonLayout.addComponent(contC);

        CheckBox scrollable = new CheckBox("scrollable");
        scrollable.setImmediate(true);
        buttonLayout.addComponent(scrollable);

        CheckBox transitions = new CheckBox("transitions");
        transitions.setValue(true);
        transitions.setImmediate(true);
        buttonLayout.addComponent(transitions);

        final FancyPanel panel = new FancyPanel();
        // panel.setTransitionsDisabled(true);
        panel.setContent(createPanelContentStart());
        panel.setSizeFull();

        layout.addComponent(panel);
        layout.setExpandRatio(panel, 1.0f);

        contA.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                panel.setContent(createPanelContentA());

            }
        });

        contB.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                panel.setContent(createPanelContentB());
            }
        });

        contC.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                panel.setContent(createPanelContentC());
            }
        });

        scrollable.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                boolean enable = (Boolean) event.getProperty().getValue();
                panel.setScrollable(enable);
            }
        });

        transitions.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                boolean enable = (Boolean) event.getProperty().getValue();
                panel.setTransitionsDisabled(!enable);
            }
        });

        return layout;
    }

    private ComponentContainer createPanelContentStart() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label guide = new Label(
                "Please select content shown in panel from the buttons above");
        layout.addComponent(guide);

        return layout;
    }

    private ComponentContainer createPanelContentA() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Label label = new Label(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
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

    private ComponentContainer createPanelContentB() {

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);

        Label label = new Label(
                "Ball tip sausage beef, kielbasa pork loin boudin chicken ham shankle salami fatback short loin. Biltong ham pastrami beef ribs salami swine. Shank meatball ribeye flank, tongue shoulder bresaola chuck. Corned beef ball tip turducken, sirloin swine capicola chuck ground round shoulder bresaola. Cow fatback venison, jerky corned beef brisket swine t-bone flank. Tri-tip meatball ham, flank ribeye venison biltong. Capicola ground round pork chop, flank meatloaf tenderloin leberkas hamburger kielbasa ham tail beef ribs rump chuck. Beef ribs brisket t-bone pork chuck capicola, filet mignon pork belly drumstick short loin. Ground round tail filet mignon shankle pork loin, turducken ball tip beef pork chop rump. Pastrami sirloin strip steak, hamburger spare ribs tenderloin meatball fatback pork ham biltong boudin. Boudin salami bacon chicken spare ribs ham hock. Strip steak ground round salami tri-tip, corned beef chuck beef speck tail turducken boudin prosciutto venison. Pork belly ball tip pastrami boudin, biltong jerky brisket ribeye chuck capicola pork chop bacon tongue. Meatloaf ball tip tenderloin, jerky swine flank shank shoulder short ribs sausage. Ham chicken biltong meatball tail sausage, corned beef bresaola andouille spare ribs. Ball tip hamburger rump speck, jowl shankle bacon. Andouille bacon beef ribs short ribs sirloin, ribeye pork loin shoulder spare ribs capicola pork chop filet mignon biltong shankle bresaola. Pork swine t-bone ground round, venison pork loin kielbasa. Tri-tip chuck kielbasa pork loin pastrami flank. Corned beef pork loin flank, sirloin strip steak cow kielbasa pork ribeye drumstick ham. Beef ribs short loin bresaola, chicken hamburger spare ribs t-bone drumstick pork belly leberkas biltong pork loin pancetta. Strip steak sausage short loin corned beef chicken, brisket pork chop tenderloin. Bresaola ball tip tri-tip shank, jerky pig strip steak sausage ribeye tongue filet mignon. Tongue meatball ribeye filet mignon, andouille pastrami fatback jerky cow t-bone bacon drumstick turkey tenderloin pork loin. Chicken capicola sirloin t-bone short loin meatball. Does your lorem ipsum text long for something a little meatier? Give our generator a try… it’s tasty!");
        layout.addComponent(label);

        Embedded image = new Embedded();
        image.setSource(new ExternalResource(
                "http://misc.siika.fi/kaljanhimo.jpg"));
        image.setWidth("300px");
        image.setHeight("187px");
        layout.addComponent(image);

        Label label2 = new Label(
                "Ball tip sausage beef, kielbasa pork loin boudin chicken ham shankle salami fatback short loin. Biltong ham pastrami beef ribs salami swine. Shank meatball ribeye flank, tongue shoulder bresaola chuck. Corned beef ball tip turducken, sirloin swine capicola chuck ground round shoulder bresaola. Cow fatback venison, jerky corned beef brisket swine t-bone flank. Tri-tip meatball ham, flank ribeye venison biltong. Capicola ground round pork chop, flank meatloaf tenderloin leberkas hamburger kielbasa ham tail beef ribs rump chuck. Beef ribs brisket t-bone pork chuck capicola, filet mignon pork belly drumstick short loin. Ground round tail filet mignon shankle pork loin, turducken ball tip beef pork chop rump. Pastrami sirloin strip steak, hamburger spare ribs tenderloin meatball fatback pork ham biltong boudin. Boudin salami bacon chicken spare ribs ham hock. Strip steak ground round salami tri-tip, corned beef chuck beef speck tail turducken boudin prosciutto venison. Pork belly ball tip pastrami boudin, biltong jerky brisket ribeye chuck capicola pork chop bacon tongue. Meatloaf ball tip tenderloin, jerky swine flank shank shoulder short ribs sausage. Ham chicken biltong meatball tail sausage, corned beef bresaola andouille spare ribs. Ball tip hamburger rump speck, jowl shankle bacon. Andouille bacon beef ribs short ribs sirloin, ribeye pork loin shoulder spare ribs capicola pork chop filet mignon biltong shankle bresaola. Pork swine t-bone ground round, venison pork loin kielbasa. Tri-tip chuck kielbasa pork loin pastrami flank. Corned beef pork loin flank, sirloin strip steak cow kielbasa pork ribeye drumstick ham. Beef ribs short loin bresaola, chicken hamburger spare ribs t-bone drumstick pork belly leberkas biltong pork loin pancetta. Strip steak sausage short loin corned beef chicken, brisket pork chop tenderloin. Bresaola ball tip tri-tip shank, jerky pig strip steak sausage ribeye tongue filet mignon. Tongue meatball ribeye filet mignon, andouille pastrami fatback jerky cow t-bone bacon drumstick turkey tenderloin pork loin. Chicken capicola sirloin t-bone short loin meatball. Does your lorem ipsum text long for something a little meatier? Give our generator a try… it’s tasty!");
        layout.addComponent(label2);

        return layout;

    }

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

    private ComponentContainer buildFancyLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label todo = new Label(
                "NOTICE: This component is still under development. This is just development preview of current state!<br/>"
                        + "FancyCssLayout adds transitions to added and removed components!");
        layout.addComponent(todo);

        Button addContent = new Button("Add new content item");
        layout.addComponent(addContent);

        final FancyCssLayout cssLayout = new FancyCssLayout();
        cssLayout.setSizeFull();
        layout.addComponent(cssLayout);
        layout.setExpandRatio(cssLayout, 1.0f);

        for (int i = 0; i < 10; ++i) {
            addCssLayoutContent(cssLayout);
        }

        addContent.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                addCssLayoutContent(cssLayout);
            }
        });

        return layout;

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

        layout.addComponent(hLayout);
    }
}
