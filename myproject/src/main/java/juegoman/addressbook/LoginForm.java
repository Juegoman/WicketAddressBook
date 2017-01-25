package juegoman.addressbook;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

public class LoginForm extends Form {
    
    private TextField usernameField;
    
    public LoginForm(String id) {
        super(id);
        
        //initialize and add the text field.        
        usernameField = new TextField("nameInput", Model.of(""));
        
        add(usernameField);
    }
    
    @Override
    public final void onSubmit() {
        //set the current user in the session.
        String username = (String)usernameField.getDefaultModelObject();
        
        Session.get().setAttribute("username", username);
    }
    
}