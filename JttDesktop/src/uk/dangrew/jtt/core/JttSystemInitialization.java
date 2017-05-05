/*
 * ---------------------------------------- Jenkins Test Tracker
 * ---------------------------------------- Produced by Dan Grew 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

/**
 * The {@link JttSystemInitialization} provides an interface for notifying the system launching
 * the {@link JttCoreInitializer} at each stage of the initialization so that it can take appropriate
 * action such as launching graphics.
 */
public interface JttSystemInitialization {

   /**
    * Method to begin initializing. This should prepare any data, graphics, etc ready for launch.
    */
   public void beginInitializing();

   /**
    * Method to indicate that the system is ready for use.
    */
   public void systemReady();

}//End Interface