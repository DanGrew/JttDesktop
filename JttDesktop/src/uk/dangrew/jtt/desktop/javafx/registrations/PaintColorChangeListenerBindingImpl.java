/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.registrations;

import java.util.function.Function;

import javafx.beans.property.Property;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * {@link PaintColorChangeListenerBindingImpl} provides a custom extension for conversion between
 * {@link Color} and {@link Paint} {@link ObjectProperty}s.
 */
public class PaintColorChangeListenerBindingImpl extends ChangeListenerMismatchBindingImpl< Color, Paint >{

   private static final Function< Paint, Color > toPropertyConverter = ( Paint color ) -> { return ( Color )color; };
   private static final Function< Color, Paint > fromPropertyConverter = ( Color paint ) -> { return paint; }; 
   
   /**
    * Constructs a new {@link PaintColorChangeListenerBindingImpl}.
    * @param propertyA the {@link Color} property.
    * @param propertyB the {@link Paint} property.
    */
   public PaintColorChangeListenerBindingImpl( 
            Property< Color > propertyA, 
            Property< Paint > propertyB 
   ) {
      super( propertyA, propertyB, toPropertyConverter, fromPropertyConverter );
   }//End Constructor

}//End Class
