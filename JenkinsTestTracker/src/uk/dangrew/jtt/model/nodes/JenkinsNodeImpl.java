/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.nodes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Implementation of the {@link JenkinsNode}.
 */
public class JenkinsNodeImpl implements JenkinsNode {

   private final ObjectProperty< String > nameProperty;
   
   /**
    * Constructs a new {@link JenkinsNodeImpl}.
    * @param name the name of the node.
    */
   public JenkinsNodeImpl( String name ) {
      if ( name == null ) {
         throw new IllegalArgumentException( "No Node name provided." );
      }
      if ( name.trim().length() == 0 ) {
         throw new IllegalArgumentException( "Invalid Node name provided." );
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
