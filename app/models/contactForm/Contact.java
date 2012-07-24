package models.contactForm;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Contact extends Model {

  public String company;
  
  @Required
  public String name;
  
  @Email
  @Required
  public String email;
  
  @Phone
  public String phone;
  
  @Lob
  @MaxSize(5000)
  @Required
  public String message;

  public String referral;
  
  public String toString() {
    return name;
  }
}