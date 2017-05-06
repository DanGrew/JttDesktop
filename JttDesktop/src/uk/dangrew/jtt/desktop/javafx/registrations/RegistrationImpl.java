/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.registrations;

/**
 * The {@link RegistrationImpl} provides a base class for registrations that can be 
 * controlled by a {@link RegistrationManager} to avoid memory leaks.
 */
public abstract class RegistrationImpl {
   
   /**
    * Method to apply the registration. Call only once.
    */
   protected abstract void register();
   
   /**
    * Method to remove the registration.
    */
   protected abstract void unregister();

}//End Class
