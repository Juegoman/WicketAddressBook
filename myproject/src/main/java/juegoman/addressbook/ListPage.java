package juegoman.addressbook;

import java.nio.charset.Charset;
import java.util.List;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import redis.clients.jedis.Jedis;

public class ListPage extends WebPage {
    
    private Link logout;
    private Label currentUsername,
                  contactListPanelMsg;
    private ContactListPanel contactListPanel;
    private AddContactForm addContactForm;
    
    public ListPage(final PageParameters parameters) {
        super(parameters);
        
        //get the current user from the session.
        currentUsername = new Label("username", Session.get().getAttribute("username"));
        
        //create the logout link, invalidates the session and redirects to the login.
        logout = new Link("logout") {
            @Override
            public void onClick() {
                Session.get().invalidate();
                setResponsePage(HomePage.class);
            }
        };
        
        //initializes the list of Contacts.
        rebuildContactList();
        
        //initialize the form for adding Contacts.
        addContactForm = new AddContactForm("addContactForm");

        //add Components.
        add(currentUsername);
        add(logout);
        add(addContactForm);
    }
    
    @Override
    protected void onBeforeRender() {
        //if the session doesn't have a user set, redirect to login.
        if(Session.get().getAttribute("username") == null) {
            setResponsePage(HomePage.class);
        }
        super.onBeforeRender();
    }
    
    public void rebuildContactList() {
        List<byte[]> contactByteList = null;
        
        //build the user's database list key and retrieve all elements within the list.
        try(Jedis jedis = WicketApplication.jedisPool.getResource()) {
            String keyStr = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
            contactByteList = jedis.lrange(keyStr.getBytes(Charset.forName("UTF-8")), 0, -1);
        }
        
        //if the list is empty, print a message encouraging the user to add some Contacts, otherwise create the Contact list. 
        if (contactByteList == null || contactByteList.isEmpty()) {
            contactListPanelMsg = new Label("contactListPanel", "There are no contacts. You should add some.");
            addOrReplace(contactListPanelMsg);
        } else {
            contactListPanel = new ContactListPanel("contactListPanel");
            addOrReplace(contactListPanel);
        }
    }
}
