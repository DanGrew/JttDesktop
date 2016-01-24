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
 * {@link PlatformImpl} implementation of {@link PlatformDecoupler} to run
 * on the {@link PlatformImpl}.
 */
public class PlatformDecouplerImpl implements PlatformDecoupler {

   /**
    * {@inheritDoc}
    */
   @Override public void run( Runnable runnable ) {
      PlatformImpl.runLater( runnable );
   }//End Method

}//End Class
