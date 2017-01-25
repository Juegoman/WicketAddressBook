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
        
        //get the Contact object from the model to serialize it for storage.
        //The serialized Contact is used to access its entry in the redis list that is the database.
        Contact contact = (Contact) model.getObject();
        try {
            originalContactBytes = SerializationHelper.serialize(contact);
        } catch (IOException ex) {
            Logger.getLogger(UpdateContactForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //initialize and add all the form fields.
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
        
        //the update form is hidden by default.
        this.setVisibilityAllowed(false);
    }
    
    @Override
    public final void onSubmit() {
        //Generate the key of the user's database list 
        String strkey = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
        byte[] key = strkey.getBytes(Charset.forName("UTF-8"));
        
        try (Jedis jedis = WicketApplication.jedisPool.getResource()) {
            //remove the original serialized Contact from the user's list
            jedis.lrem(key, 1, originalContactBytes);
            
            //get the updated Contact and serialize it
            Contact contact = (Contact) this.getDefaultModelObject();
            byte[] serializedContact = SerializationHelper.serialize(contact);
            
            //push the updated serialized Contact to the user's list
            //and update the originalContactBytes with the updated serialized Contact.
            jedis.lpush(key, serializedContact);
            originalContactBytes = serializedContact;
        } catch (IOException ex) {
            Logger.getLogger(UpdateContactForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //rebuild the contact List.
        ListPage page = (ListPage) getPage();
        page.rebuildContactList();
    }
    
    //getter method for the serialized Contact of the model.
    public byte[] getContactBytes() {
        return this.originalContactBytes;
    }
    
}
