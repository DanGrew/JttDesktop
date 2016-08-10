/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import javafx.scene.Node;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.configuration.tree.ConfigurationTree;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreePane;
import uk.dangrew.jtt.event.structure.Event;

/**
 * The {@link PreferenceOpener} provides a mechanism for listening to events to open
 * the preference window.
 */
public class PreferenceController {

   private final PreferenceWindowController controller;
   private final ConfigurationTreeContent contentHolder;
   private final ConfigurationTree tree;
   
   /**
    * Constructs a new {@link PreferenceOpener}.
    * @param configuration the {@link SystemConfiguration}.
    */
   public PreferenceController( SystemConfiguration configuration ) {
      this( new PreferenceWindowController(), configuration, new ConfigurationTreeContent() );
   }//End Constructor
   
   /**
    * Constructs a new {@link PreferenceOpener}.
    * @param controller the {@link PreferenceWindowController} for controlling the window.
    * @param configuration the {@link SystemConfiguration} associated.
    * @param contentHolder the {@link ConfigurationTreeContent}.
    */
   PreferenceController( PreferenceWindowController controller, SystemConfiguration configuration, ConfigurationTreeContent contentHolder ) {
      this.contentHolder = contentHolder;
      
      this.tree = new ConfigurationTree( this, configuration );
      ConfigurationTreePane configurationWindow = new ConfigurationTreePane( 
               tree, contentHolder
      );
      this.controller = controller;
      this.controller.associateWithConfiguration( configurationWindow );
      
      new PreferencesOpenEvent().register( this::eventFired );
   }//End Constructor

   /**
    * Handler for the event when received.
    * @param event the {@link Event} fired.
    */
   private void eventFired( Event< Void, PreferenceBehaviour > event ) {
      if ( event.getValue() == null ) {
         return;
      }
      switch ( event.getValue().getWindowPolicy() ) {
         case Close:
            controller.hideConfigurationWindow();
            break;
         case Open:
            controller.showConfigurationWindow();
            break;
         default:
            break;
      }
      
      if ( event.getValue().getSelection() == null ) {
         return;
      }
      tree.select( event.getValue().getSelection() );
   }//End Method
   
   /**
    * Method to display the content in the {@link ConfigurationTreeContent}.
    * @param title the {@link Node} providing the title.
    * @param content the {@link Node} providing the configuration content.
    */
   public void displayContent( Node title, Node content ){
      contentHolder.setContent( title, content );
   }//End Method
   
   ConfigurationTree tree(){
      return tree;
   }//End Method
}//End Class
