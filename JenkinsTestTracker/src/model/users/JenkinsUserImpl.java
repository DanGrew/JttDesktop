/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package model.users;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Implementation of the {@link JenkinsUser}.
 */
public class JenkinsUserImpl implements JenkinsUser {

   private final StringProperty nameProperty;
   
   /**
    * Constructs a new {@link JenkinsUserImpl}.
    * @param name the name of the user.
    */
   public JenkinsUserImpl( String name ) {
      if ( name == null ) throw new IllegalArgumentException( "No User name provided." );
      if ( name.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid User name provided." );
      
      nameProperty = new SimpleStringProperty( name );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty nameProperty() {
      return nameProperty;
   }//End Method

}//End Class
