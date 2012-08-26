package controllers.contactForm;

import play.*;
import play.mvc.*;
import play.libs.*;
import play.cache.*;
import play.libs.Mail;
import org.apache.commons.mail.*;
import org.apache.commons.mail.EmailException; 

import java.util.*;

import models.*;
import controllers.*;

import play.data.validation.*;

public class ContactForm extends Application {
    
    @Before
    static void addDefaults() {
        renderArgs.put("contactShowCompanyField", Play.configuration.getProperty("contactForm.showCompanyField"));
        renderArgs.put("contactShowPhoneField", Play.configuration.getProperty("contactForm.showPhoneField"));
        renderArgs.put("contactShowReferralField", Play.configuration.getProperty("contactForm.showReferralField"));
        renderArgs.put("contactReferralLabel", Play.configuration.getProperty("contactForm.referralLabel"));
        renderArgs.put("contactEnableCaptcha", Play.configuration.getProperty("contactForm.enableCaptcha"));
        renderArgs.put("siteAnalytics", Play.configuration.getProperty("site.analyticsId"));
    }
    
    public static void index() {
      models.contactForm.Contact contact = new models.contactForm.Contact();
      String randomID = Codec.UUID();
      render(randomID);
    }
   
    public static void sendMessage(@Valid models.contactForm.Contact contact, String special, String code, String randomID) {
      //System.out.println(request.headers.get("referer"));
      if (special != null) {
        if (special.length() > 0) {
          return;
        }
      }
      if (Play.configuration.getProperty("contactForm.enableCaptcha") == "true") {
        // Check for errors
        if (code != null && randomID != null) {
        validation.equals(
                code.toLowerCase(), Cache.get(randomID).toString().toLowerCase()
            ).message("Invalid code. Please try again");
        } else {
          validation.addError("code", "The code is required");
        }
      }
      
      /*
      if (code == " ") {
        if (!code.equalsIgnoreCase(Cache.get(randomID).toString())) {
          validation.addError("code", "Please retype the code");
        }
      }
      */
      
      if(validation.hasErrors()) {
        params.flash(); // add http parameters to the flash scope
        render("@index", contact, renderArgs, randomID);
      }
      contact.save();
      //Add user input to message
      String b = System.getProperty("line.separator");
      String message = "Name: "+contact.name+b+
                       "\nCompany: "+contact.company+b+
                       "\nEmail: " +contact.email+b+
                       "\nPhone: " +contact.phone+b+
                       "\nMessage: " +contact.message+b+
                       "\nHow they heard about us: " +contact.referral+b;
      //create email and send
      SimpleEmail email = new SimpleEmail();
      try{ 
        email.setFrom(Play.configuration.getProperty("contactForm.fromAddress"), Play.configuration.getProperty("site.name"));
        email.addReplyTo(contact.email, contact.name);
        if (Play.configuration.getProperty("site.email") != null) {
          email.addTo(Play.configuration.getProperty("contactForm.sendTo"), Play.configuration.getProperty("site.name"));
        } else {
          email.addTo("divmedium@gmail.com", "Matthew Hunt");
        }
        //email.addTo("digitaladventuresinc@gmail.com", "Digital Ad Venture");
        email.setSubject("Online Contact Form - "+Play.configuration.getProperty("site.name"));
        email.setMsg(message);
      
      } catch (EmailException e) { 
        e.printStackTrace(); 
      }
      Mail.send(email);
      success();
    }
    
    public static void success() {
      render();
    }
}
