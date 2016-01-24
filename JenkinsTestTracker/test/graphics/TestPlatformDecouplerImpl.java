/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package graphics;

import com.sun.javafx.application.PlatformImpl;

/**
 * Implementation of {@link PlatformDecoupler} to simply run the {@link Runnable}
 * without the {@link PlatformImpl} thread. This is used for testing.
 */
public class TestPlatformDecouplerImpl implements PlatformDecoupler {

   /**
    * {@inheritDoc}
    */
   @Override public void run( Runnable runnable ) {
      runnable.run();
   }//End Method

}//End Class
