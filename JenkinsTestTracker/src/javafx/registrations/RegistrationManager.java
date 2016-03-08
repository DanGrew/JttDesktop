/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.registrations;

import java.util.HashSet;
import java.util.Set;

/**
 * The {@link RegistrationManager} is responsible for controlling registrations while
 * the associated object is active. Once destroyed the manager is shutdown to remove listeners.
 */
public class RegistrationManager {

   private Set< RegistrationImpl > registrations;
   
   /**
    * Constructs a new {@link RegistrationManager}.
    */
   public RegistrationManager() {
      registrations = new HashSet<>();
   }//End Constructor
   
   /**
    * Method to apply the given {@link RegistrationImpl}. This will immediately register.
    * @param registration the {@link RegistrationImpl} to apply.
    */
   public void apply( RegistrationImpl registration ) {
      registrations.add( registration );
      registration.register();
   }//End Method

   /**
    * Method to shut the manager down. This will unregister all {@link RegistrationImpl}s.
    */
   public void shutdown() {
      registrations.forEach( registration -> registration.unregister() );
      registrations.clear();
   }//End Method

   /**
    * Method to determine whether this {@link RegistrationManager} has any {@link RegistrationImpl}s held.
    * @return true if none held.
    */
   public boolean isEmpty() {
      return registrations.isEmpty();
   }//End Method

}//End Class
