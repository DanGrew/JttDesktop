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
import javafx.util.converter.IntegerStringConverter;

/**
 * Defensive version of the {@link IntegerStringConverter} that does not crash when something
 * unexpected is parsed.
 */
public class DefensiveIntegerStringConverter extends IntegerStringConverter {

   private ObjectProperty< Integer > property;
   
   /**
    * Constructs a new {@link DefensiveIntegerStringConverter}.
    * @param propertyBeingUpdated the {@link ObjectProperty} to retain the value for.
    */
   public DefensiveIntegerStringConverter( ObjectProperty< Integer > propertyBeingUpdated ) {
      this.property = propertyBeingUpdated;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public Integer fromString( String string ) {
      try {
         Integer parsed = super.fromString( string );
         return parsed;
      } catch ( NumberFormatException exception ) {
         if ( property.get() == null ) return 0;
         return property.get();
      }
   }//End Method

}//End Class
