/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.event.structure.Event;

/**
 * The {@link PreferenceOpener} provides a mechanism for listening to events to open
 * the preference window.
 */
public class PreferenceOpener {

   private final PreferenceWindowController controller;
   
   /**
    * Constructs a new {@link PreferenceOpener}.
    * @param configuration the {@link SystemConfiguration}.
    */
   public PreferenceOpener( SystemConfiguration configuration ) {
      this( new PreferenceWindowController(), configuration );
   }//End Constructor
   
   /**
    * Constructs a new {@link PreferenceOpener}.
    * @param controller the {@link PreferenceWindowController} for controlling the window.
    * @param configuration the {@link SystemConfiguration} associated.
    */
   PreferenceOpener( PreferenceWindowController controller, SystemConfiguration configuration ) {
      this.controller = controller;
      this.controller.associateWithConfiguration( configuration );
      
      new PreferencesOpenEvent().register( this::eventFired );
   }//End Constructor

   /**
    * Handler for the event when received.
    * @param event the {@link Event} fired.
    */
   private void eventFired( Event< Void, WindowPolicy > event ) {
      if ( event.getValue() == null ) {
         return;
      }
      switch ( event.getValue() ) {
         case Close:
            controller.hideConfigurationWindow();
            break;
         case Open:
            controller.showConfigurationWindow();
            break;
         default:
            break;
      }
   }//End Method
}//End Class
