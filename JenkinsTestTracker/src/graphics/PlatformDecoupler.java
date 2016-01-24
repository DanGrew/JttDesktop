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
 * The {@link PlatformDecoupler} aims to replace the interface to {@link PlatformImpl}
 * so that it is not so difficult to test. Exploratory testing may be needed to identify 
 * graphical issues and concurrency problems.
 */
public interface PlatformDecoupler {
   
   /**
    * Method to simply run the {@link Runnable} appropriately.
    * @param runnable the {@link Runnable}
    */
   public void run( Runnable runnable );
}//End Interface
