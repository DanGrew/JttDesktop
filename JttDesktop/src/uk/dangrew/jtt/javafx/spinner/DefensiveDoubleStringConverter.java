/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.spinner;

import javafx.beans.property.ObjectProperty;
import javafx.util.converter.DoubleStringConverter;

/**
 * Defensive version of the {@link DoubleStringConverter} that does not crash when something
 * unexpected is parsed.
 */
public class DefensiveDoubleStringConverter extends DoubleStringConverter {

   private ObjectProperty< Double > property;
   
   /**
    * Constructs a new {@link DefensiveDoubleStringConverter}.
    * @param propertyBeingUpdated the {@link ObjectProperty} to retain the value for.
    */
   public DefensiveDoubleStringConverter( ObjectProperty< Double > propertyBeingUpdated ) {
      this.property = propertyBeingUpdated;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public Double fromString( String string ) {
      try {
         return super.fromString( string );
      } catch ( NumberFormatException exception ) {
         if ( property.get() == null ) return 0.0;
         
         return property.get();
      }
   }//End Method

}//End Class
