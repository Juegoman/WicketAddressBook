package juegoman.addressbook;

import java.nio.charset.Charset;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import redis.clients.jedis.Jedis;

public class ContactList extends ListView<Contact> {
    public ContactList(String id, List<Contact> list) {
        super(id, list);
        //setReuseItems to true otherwise items don't retain their visible status.
        setReuseItems(true);
    }

    @Override
    protected void populateItem(ListItem<Contact> contact) {
        //set the contact's model to a CompoundPropertyModel.
        contact.setDefaultModel(new CompoundPropertyModel<>(contact.getModelObject()));
        
        //instantiate the Labels to display the Contact's information
        contact.add(new Label("fullName"));
        contact.add(new Label("address"));
        contact.add(new Label("email"));
        contact.add(new Label("phone"));
        
        //instantiate and add the Contact update form.
        contact.add(new UpdateContactForm("updateContactForm", (CompoundPropertyModel) contact.getDefaultModel()));
        
        //create a container for the delete confirm dialog to be able to set visibility
        //set the visibility to false to start.
        WebMarkupContainer deleteDialog = new WebMarkupContainer("deleteDialog");
        deleteDialog.setVisibilityAllowed(false);
        
        //closes the delete dialog box
        deleteDialog.add(new Link("cancelDelete") {
            @Override
            public void onClick() {
                Link cancel = (Link) contact.get("deleteButton");
                cancel.onClick();
            }
            
        });
        //deletes this Contact
        deleteDialog.add(new Link("confirmDelete") {
            @Override
            public void onClick() {
                //get the serialized Contact from the Contact update form
                UpdateContactForm form = (UpdateContactForm) contact.get("updateContactForm");
                byte[] contactBytes = form.getContactBytes();
                
                //generate the user's Contact list key
                String strkey = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
                byte[] key = strkey.getBytes(Charset.forName("UTF-8"));
                
                //delete the Contact from the user's Contact list
                try (Jedis jedis = WicketApplication.jedisPool.getResource()) {
                    jedis.lrem(key, 1, contactBytes);
                }
                
                //rebuild the Contact list.
                ListPage page = (ListPage) getPage();
                page.rebuildContactList();
            }
            
        });
        
        //add the delete dialog container.
        contact.add(deleteDialog);
        
        //toggles the visiblity of the edit Contact form.
        contact.add(new Link("editButton") {
            @Override
            public void onClick() {
                Component form = contact.get("updateContactForm");
                if (form.isVisibilityAllowed()) {
                    form.setVisibilityAllowed(false);
                } else {
                    form.setVisibilityAllowed(true);
                }
            }
        });
        
        //toggles the visiblity of the delete Contact dialog
        contact.add(new Link("deleteButton") {
            @Override
            public void onClick() {
                Component dialog = contact.get("deleteDialog");
                if (dialog.isVisibilityAllowed()) {
                    dialog.setVisibilityAllowed(false);
                } else {
                    dialog.setVisibilityAllowed(true);
                }
            }
        });
    }
    
}
