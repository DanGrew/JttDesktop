/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link UserTreeItem} represents a {@link JenkinsUser} in the {@link UserAssignmentsTree}.
 */
public class UserTreeItem implements UserAssignmentsTreeItem {

   private final JenkinsUser user;
   private final ObjectProperty< Node > userProperty;

   /**
    * Constructs a new {@link UserTreeItem}.
    * @param user the {@link JenkinsUser} being represented.
    */
   public UserTreeItem( JenkinsUser user ) {
      this( user, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link UserTreeItem}.
    * @param user the {@link JenkinsUser} being represented.
    * @param styling the {@link JavaFxStyle} to use for components.
    */
   UserTreeItem( JenkinsUser user, JavaFxStyle styling ) {
      if ( user == null ) {
         throw new IllegalArgumentException( "Must supply non null user." );
      }
      
      this.user = user;
      this.userProperty = new SimpleObjectProperty<>( styling.createBoldLabel( user.nameProperty().get() ) );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public JenkinsUser getJenkinsUser(){
      return user;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > firstColumnProperty() {
      return userProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > secondColumnProperty() {
      return null;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > detailProperty() {
      return null;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWithUserAssignment( UserAssignment assignment ) {
      return false;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void detachFromSystem() {}
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isDetachedFromSystem() {
      return true;
   }//End Method
   
}//End Class
