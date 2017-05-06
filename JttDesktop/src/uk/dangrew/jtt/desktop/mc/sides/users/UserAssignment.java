/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link UserAssignment} provides the definition of a task or responsibility associated
 * with a particular {@link JenkinsUser}.
 */
public class UserAssignment {

   private final JenkinsUser user;
   private final LongProperty timestamp;
   private final StringProperty description;
   private final StringProperty detail;
   
   /**
    * Constructs a new {@link UserAssignment}.
    * @param user the {@link JenkinsUser} associated.
    * @param timestamp the timestamp of the assignment.
    * @param description the outline description of the assignment.
    * @param detail the detail of the assignment with notes.
    */
   public UserAssignment( JenkinsUser user, long timestamp, String description, String detail ) {
      this.user = user;
      this.timestamp = new SimpleLongProperty( timestamp );
      this.description = new SimpleStringProperty( description );
      this.detail = new SimpleStringProperty( detail );
   }//End Constructor
   
   /**
    * Method to construct the {@link UserAssignmentsTreeItem} to place in the {@link UserAssignmentsTree}.
    * @return the {@link UserAssignmentsTreeItem} constructed.
    */
   public UserAssignmentsTreeItem constructTreeItem(){
      return new AssignedTreeItem( this );
   }//End Method

   /**
    * Getter for the associated {@link JenkinsUser}.
    * @return the {@link JenkinsUser}.
    */
   public JenkinsUser getJenkinsUser() {
      return user;
   }//End Method
   
   /**
    * Property for the timestamp of the assignment.
    * @return the {@link LongProperty}.
    */
   public LongProperty timestampProperty(){
      return timestamp;
   }//End Method
   
   /**
    * Property for the description of the assignment.
    * @return the {@link StringProperty}.
    */
   public StringProperty descriptionProperty(){
      return description;
   }//End Method
   
   /**
    * Property for the detail of the assignment.
    * @return the {@link StringProperty}.
    */
   public StringProperty detailProperty(){
      return detail;
   }//End Method

}//End Class
