package juegoman.addressbook;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import redis.clients.jedis.Jedis;

public class AddContactForm extends Form {
    
    private TextField firstNameField,
                      lastNameField,
                      addressField,
                      emailField,
                      phoneField;
    
    public AddContactForm(String id) {
        super(id);
        
        firstNameField = new TextField("firstNameField", Model.of(""));
        lastNameField = new TextField("lastNameField", Model.of(""));
        addressField = new TextField("addressField", Model.of(""));
        emailField = new TextField("emailField", Model.of(""));
        phoneField = new TextField("phoneField", Model.of(""));
        
        add(firstNameField);
        add(lastNameField);
        add(addressField);
        add(emailField);
        add(phoneField);
    }
    
    @Override
    public final void onSubmit() {
        String firstName = firstNameField.getDefaultModelObjectAsString();
        String lastName = lastNameField.getDefaultModelObjectAsString();
        String address = addressField.getDefaultModelObjectAsString();
        String email = emailField.getDefaultModelObjectAsString();
        String phone = phoneField.getDefaultModelObjectAsString();
        
        Contact newContact = new Contact(firstName, lastName);
        if (!address.equals("")) {
            newContact.setAddress(address);
        }
        if (!email.equals("")) {
            newContact.setEmail(email);
        }
        if (!phone.equals("")) {
            newContact.setPhone(phone);
        }
        
        String keyStr = WicketApplication.KEYPREFIX + Session.get().getAttribute("username");
        byte[] key = keyStr.getBytes(Charset.forName("UTF-8"));
        
        try {
            byte[] serializedContact = SerializationHelper.serialize(newContact);
            try (Jedis jedis = WicketApplication.jedisPool.getResource()) {
                jedis.lpush(key, serializedContact);
            }
        } catch (IOException ex) {
            Logger.getLogger(AddContactForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        firstNameField.setDefaultModel(Model.of(""));
        lastNameField.setDefaultModel(Model.of(""));
        addressField.setDefaultModel(Model.of(""));
        emailField.setDefaultModel(Model.of(""));
        phoneField.setDefaultModel(Model.of(""));
        
        ListPage page = (ListPage) getPage();
        page.rebuildContactList();
    }
}
