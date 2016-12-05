/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.users;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Implementation of the {@link JenkinsUser}.
 */
public class JenkinsUserImpl implements JenkinsUser {

   private final ObjectProperty< String > nameProperty;
   
   /**
    * Constructs a new {@link JenkinsUserImpl}.
    * @param name the name of the user.
    */
   public JenkinsUserImpl( String name ) {
      if ( name == null ) {
         throw new IllegalArgumentException( "No User name provided." );
      }
      if ( name.trim().length() == 0 ) {
         throw new IllegalArgumentException( "Invalid User name provided." );
      }
      
      nameProperty = new SimpleObjectProperty<>( name );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< String > nameProperty() {
      return nameProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String toString() {
      return nameProperty.get();
   }//End Method

}//End Class
