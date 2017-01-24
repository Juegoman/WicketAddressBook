package juegoman.addressbook;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import redis.clients.jedis.Jedis;

public class UpdateContactForm extends Form {
    
    private TextField firstNameField,
                      lastNameField,
                      addressField,
                      emailField,
                      phoneField;
    private byte[] originalContactBytes;
    
    public UpdateContactForm(String id, CompoundPropertyModel model) {
        super(id, model);
        
        Contact contact = (Contact) model.getObject();
        try {
            originalContactBytes = SerializationHelper.serialize(contact);
        } catch (IOException ex) {
            Logger.getLogger(UpdateContactForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        firstNameField = new TextField("firstName");
        lastNameField = new TextField("lastName");
        addressField = new TextField("address");
        emailField = new TextField("email");
        phoneField = new TextField("phone");
        
        add(firstNameField);
        add(lastNameField);
        add(addressField);
        add(emailField);
        add(phoneField);
        
        this.setVisibilityAllowed(false);
    }
    
    @Override
    public final void onSubmit() {
        String strkey = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
        byte[] key = strkey.getBytes(Charset.forName("UTF-8"));
        try (Jedis jedis = WicketApplication.jedisPool.getResource()) {
            jedis.lrem(key, 1, originalContactBytes);
            Contact contact = (Contact) this.getDefaultModelObject();
            byte[] serializedContact = SerializationHelper.serialize(contact);
            jedis.lpush(key, serializedContact);
            originalContactBytes = serializedContact;
        } catch (IOException ex) {
            Logger.getLogger(UpdateContactForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ListPage page = (ListPage) getPage();
        page.rebuildContactList();
    }
    
    public byte[] getContactBytes() {
        return this.originalContactBytes;
    }
    
}
