/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.registrations;

/**
 * Provides an interface for components that use the {@link RegistrationManager}.
 */
public interface RegisteredComponent {

   /**
    * Method to detach this object and its {@link RegistrationImpl}s from the system.
    */
   public void detachFromSystem();
   
   /**
    * Method to determine whether the {@link RegisteredComponent} is detached and
    * not registered with anything in the system.
    * @return true if no registrations held.
    */
   public boolean isDetached();
   
}//End Interface
