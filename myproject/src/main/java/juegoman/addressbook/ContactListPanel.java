package juegoman.addressbook;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.panel.Panel;
import redis.clients.jedis.Jedis;

public class ContactListPanel extends Panel {
    private List<Contact> contacts;
    private ContactList contactList;
    
    public ContactListPanel(String id) {
        super(id);
        
        List<byte[]> contactByteList = null;
        contacts = new ArrayList<>();
        
        try(Jedis jedis = WicketApplication.jedisPool.getResource()) {
            String keyStr = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
            contactByteList = jedis.lrange(keyStr.getBytes(Charset.forName("UTF-8")), 0, -1);
        }
        contactByteList.forEach(contactBytes -> {
            Contact contact = new Contact();
            try {
                contact = (Contact) SerializationHelper.deserialize(contactBytes);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ContactListPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            contacts.add(contact);
        });
        contactList = new ContactList("contactList", contacts);

        addOrReplace(contactList);
        
    }
    
}
