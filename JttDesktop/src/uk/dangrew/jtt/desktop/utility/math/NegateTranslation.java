/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.utility.math;

import java.util.function.Function;

/**
 * {@link NegateTranslation} simply negates the given value.
 */
public class NegateTranslation implements Function< Double, Double > {

   /**
    * {@inheritDoc}
    */
   @Override public Double apply( Double t ) {
      return -t;
   }//End Method

}//End Class
