package controllers.contactForm;
 
import play.*;
import play.mvc.*;

import controllers.*;

@CRUD.For(models.contactForm.Contact.class)
@Check("admin")
@With(Secure.class)
public class Contacts extends CRUD {    
}