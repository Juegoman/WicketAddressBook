package com.mycompany;

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
        setReuseItems(true);
    }

    @Override
    protected void populateItem(ListItem<Contact> contact) {
        contact.setDefaultModel(new CompoundPropertyModel<>(contact.getModelObject()));
        contact.add(new Label("fullName"));
        contact.add(new Label("address"));
        contact.add(new Label("email"));
        contact.add(new Label("phone"));
        
        contact.add(new UpdateContactForm("updateContactForm", (CompoundPropertyModel) contact.getDefaultModel()));
        WebMarkupContainer deleteDialog = new WebMarkupContainer("deleteDialog");
        deleteDialog.setVisibilityAllowed(false);
        
        deleteDialog.add(new Link("cancelDelete") {
            @Override
            public void onClick() {
                Link cancel = (Link) contact.get("deleteButton");
                cancel.onClick();
            }
            
        });
        
        deleteDialog.add(new Link("confirmDelete") {
            @Override
            public void onClick() {
                UpdateContactForm form = (UpdateContactForm) contact.get("updateContactForm");
                byte[] contactBytes = form.getContactBytes();
                String strkey = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
                byte[] key = strkey.getBytes(Charset.forName("UTF-8"));
                try (Jedis jedis = WicketApplication.jedisPool.getResource()) {
                    jedis.lrem(key, 1, contactBytes);
                }
                ListPage page = (ListPage) getPage();
                page.rebuildContactList();
            }
            
        });
        
        contact.add(deleteDialog);
        

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
