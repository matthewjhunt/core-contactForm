package models.contactForm;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import controllers.CRUD.Hidden;
 
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
  
  @Hidden
  private Date created;
  
  @Hidden
  private Date modified;
  
  public String toString() {
    return name;
  }
  
  @PrePersist
    protected void onCreate() {
      this.created = this.modified = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
      this.modified = new Date();
    }
}