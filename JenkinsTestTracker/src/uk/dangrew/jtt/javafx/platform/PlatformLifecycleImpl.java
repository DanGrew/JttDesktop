/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.platform;

import com.sun.javafx.application.PlatformImpl;

/**
 * The {@link PlatformLifecycleImpl} provides the practically untestable JVM shutdown
 * the application needs to kill the process.
 */
public class PlatformLifecycleImpl {

   /**
    * Method to shutdown the {@link PlatformImpl} and system.
    */
   public void shutdownPlatform(){
      PlatformImpl.exit();
      System.exit( 0 );
   }//End Method
   
}//End Class
