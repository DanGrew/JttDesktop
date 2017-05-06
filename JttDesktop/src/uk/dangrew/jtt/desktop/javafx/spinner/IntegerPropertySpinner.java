/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.spinner;

import java.util.function.Function;

import javafx.beans.property.IntegerProperty;

/**
 * {@link IntegerPropertySpinner} provides a convenient extension of {@link PropertySpinner}
 * that does not convert {@link Integer}s and binds an {@link IntegerProperty}.
 */
public class IntegerPropertySpinner extends PropertySpinner< Integer, Integer > {
   
   private static final Function< Integer, Integer > RETURN_PARAMETER_FUNCTION = parameter -> parameter;

   /**
    * Method to bind the given {@link IntegerProperty}.
    * @param property the {@link IntegerProperty} to bind.
    */
   public void bindProperty( IntegerProperty property ) {
      super.bindProperty( property.asObject(), RETURN_PARAMETER_FUNCTION, RETURN_PARAMETER_FUNCTION );
   }//End Method
   
}//End Class
