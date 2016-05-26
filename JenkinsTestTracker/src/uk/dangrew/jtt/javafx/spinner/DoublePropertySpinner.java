/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.spinner;

import java.util.function.Function;

import javafx.beans.property.DoubleProperty;

/**
 * {@link DoublePropertySpinner} provides a convenient extension of {@link PropertySpinner}
 * that does not convert {@link Double}s and binds an {@link DoubleProperty}.
 */
public class DoublePropertySpinner extends PropertySpinner< Double, Double > {
   
   private static final Function< Double, Double > RETURN_PARAMETER_FUNCTION = parameter -> parameter;

   /**
    * Method to bind the given {@link IntegerProperty}.
    * @param property the {@link IntegerProperty} to bind.
    */
   public void bindProperty( DoubleProperty property ) {
      super.bindProperty( property.asObject(), RETURN_PARAMETER_FUNCTION, RETURN_PARAMETER_FUNCTION );
   }//End Method
   
}//End Class
