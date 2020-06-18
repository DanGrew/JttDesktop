/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.graphics;


import uk.dangrew.kode.javafx.platform.JavaFxThreading;

/**
 * {@link PlatformImpl} implementation of {@link PlatformDecoupler} to run
 * on the {@link PlatformImpl}.
 * @deprecated
 */
@Deprecated
public class PlatformDecouplerImpl implements PlatformDecoupler {

   /**
    * {@inheritDoc}
    */
   @Override public void run( Runnable runnable ) {
       JavaFxThreading.runLater( runnable );
   }//End Method

}//End Classπ
