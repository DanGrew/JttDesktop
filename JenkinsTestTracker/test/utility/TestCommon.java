/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package utility;

/**
 * Common test values and properties used.
 */
public final class TestCommon {
   
   private static final double PRECISION = 0.001;
   
   /**
    * Gets the precision to use for double comparisons.
    * @return the error to compare to.
    */
   public static final double precision(){
      return PRECISION;
   }//End Method

}//End Class
