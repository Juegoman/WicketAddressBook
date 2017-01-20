package com.mycompany;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ListPage extends WebPage {
    
    private Link logout;
    private Label currentUsername;
    private ContactListPanel contactListPanel;
    private AddContactForm addContactForm;
    
    public ListPage(final PageParameters parameters) {
        super(parameters);
        
        currentUsername = new Label("username", Session.get().getAttribute("username"));
        logout = new Link("logout") {
            @Override
            public void onClick() {
                Session.get().invalidate();
                setResponsePage(HomePage.class);
            }
        };
        contactListPanel = new ContactListPanel("contactListPanel");
        addContactForm = new AddContactForm("addContactForm");

        add(currentUsername);
        add(logout);
        add(contactListPanel);
        add(addContactForm);
    }
    
    @Override
    protected void onBeforeRender() {
        if(Session.get().getAttribute("username") == null) {
            setResponsePage(HomePage.class);
        }
        super.onBeforeRender();
    }
}
