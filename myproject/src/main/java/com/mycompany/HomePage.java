package com.mycompany;

import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
    
    private LoginForm loginForm;

    public HomePage(final PageParameters parameters) {
        super(parameters);
        
        loginForm = new LoginForm("loginForm");
        
        add(loginForm);
    }
    
    @Override
    protected void onBeforeRender() {
        if(Session.get().getAttribute("username") != null) {
            setResponsePage(ListPage.class);
        }
        super.onBeforeRender();
    }
}
