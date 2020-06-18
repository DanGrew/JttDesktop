/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import java.time.Instant;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.javafx.registrations.ChangeListenerBindingImpl;
import uk.dangrew.jtt.desktop.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.desktop.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link AssignedTreeItem} represents a {@link UserAssignment} in the {@link UserAssignmentsTree}.
 */
public class AssignedTreeItem implements UserAssignmentsTreeItem {
   
   private final UserAssignment assignment;
   private final RegistrationManager registrations;
   
   private final ObjectProperty< Node > timestampProperty;
   private final ObjectProperty< Node > issueDescriptionProperty;
   private final ObjectProperty< Node > issueDetailProperty;
   
   /**
    * Constructs a new {@link AssignedTreeItem}.
    * @param assignment the associated {@link UserAssignment}.
    */
   public AssignedTreeItem( UserAssignment assignment ) {
      this( assignment, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link AssignedTreeItem}.
    * @param assignment the associated {@link UserAssignment}.
    * @param styling the {@link JavaFxStyle} for styling graphics.
    */
   AssignedTreeItem( UserAssignment assignment, JavaFxStyle styling ) {
      this.assignment = assignment;
      this.registrations = new RegistrationManager();
      
      Label timestampLabel = new Label( formatTimestamp( assignment.timestampProperty().get() ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               assignment.timestampProperty(), 
               ( s, o, u ) -> timestampLabel.setText( formatTimestamp( u.longValue() ) ) ) 
      );
      this.timestampProperty = new SimpleObjectProperty<>( timestampLabel );
      
      Label descriptionLabel = styling.createWrappedTextLabel( assignment.descriptionProperty().get() );
      registrations.apply( new ChangeListenerBindingImpl<>( assignment.descriptionProperty(), descriptionLabel.textProperty() ) );
      this.issueDescriptionProperty = new SimpleObjectProperty<>( descriptionLabel );
      
      Label detailLabel = styling.createWrappedTextLabel( assignment.detailProperty().get() );
      registrations.apply( new ChangeListenerBindingImpl<>( assignment.detailProperty(), detailLabel.textProperty() ) );
      this.issueDetailProperty = new SimpleObjectProperty<>( detailLabel );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > firstColumnProperty() {
      return timestampProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > secondColumnProperty() {
      return issueDescriptionProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > detailProperty() {
      return issueDetailProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsUser getJenkinsUser(){
      return assignment.getJenkinsUser();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public UserAssignment getAssignment() {
      return assignment;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWithUserAssignment( UserAssignment assignment ) {
      return this.assignment == assignment;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void detachFromSystem() {
      registrations.shutdown();
   }//End Method
   
   /**
    * {@inheritDoc}}
    */
   @Override public boolean isDetachedFromSystem() {
      return registrations.isEmpty();
   }//End Method
   
   /**
    * Method to format the given timestamp in seconds to an appropriate date/time format.
    * @param timestamp the number of seconds since epoch.
    * @return the formatted {@link String}.
    */
   static String formatTimestamp( long timestamp ) {
      return Instant.ofEpochSecond( timestamp ).toString().replace( "T", " at " ).replace( "Z", "" );
   }//End Method
   
}//End Class
