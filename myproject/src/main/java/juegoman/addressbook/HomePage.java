package juegoman.addressbook;

import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
    
    private LoginForm loginForm;

    public HomePage(final PageParameters parameters) {
        super(parameters);
        
        //initialize and add the login form.        
        loginForm = new LoginForm("loginForm");
        
        add(loginForm);
    }
    
    @Override
    protected void onBeforeRender() {
        //if the session has a user redirect to the Contact list page.
        if(Session.get().getAttribute("username") != null) {
            setResponsePage(ListPage.class);
        }
        super.onBeforeRender();
    }
}
