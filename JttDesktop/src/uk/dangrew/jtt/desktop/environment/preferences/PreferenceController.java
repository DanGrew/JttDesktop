/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.preferences;

import javafx.scene.Node;
import uk.dangrew.jtt.desktop.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.desktop.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.desktop.configuration.tree.ConfigurationTree;
import uk.dangrew.jtt.desktop.configuration.tree.ConfigurationTreePane;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link PreferenceOpener} provides a mechanism for listening to events to open
 * the preference window.
 */
public class PreferenceController {

   private final SystemConfiguration configuration;
   private final JenkinsDatabase database;
   private final PreferenceWindowController controller;
   private final ConfigurationTreeContent contentHolder;
   private final ConfigurationTree tree;
   
   /**
    * Constructs a new {@link PreferenceOpener}.
    * @param configuration the {@link SystemConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   public PreferenceController( SystemConfiguration configuration, JenkinsDatabase database ) {
      this( new PreferenceWindowController(), configuration, new ConfigurationTreeContent(), database );
   }//End Constructor
   
   /**
    * Constructs a new {@link PreferenceOpener}.
    * @param controller the {@link PreferenceWindowController} for controlling the window.
    * @param configuration the {@link SystemConfiguration} associated.
    * @param contentHolder the {@link ConfigurationTreeContent}.
    * @param database the {@link JenkinsDatabase}.
    */
   PreferenceController( 
            PreferenceWindowController controller, 
            SystemConfiguration configuration, 
            ConfigurationTreeContent contentHolder, 
            JenkinsDatabase database 
   ) {
      this.configuration = configuration;
      this.contentHolder = contentHolder;
      this.database = database;
      
      this.tree = new ConfigurationTree( this, database, configuration );
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
   private void eventFired( Event< PreferenceBehaviour > event ) {
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
   
   /**
    * Method to determine whether the given {@link SystemConfiguration} is used by this {@link PreferenceController}.
    * @param configuration the {@link SystemConfiguration} in question.
    * @return true if same as given.
    */
   public boolean usesConfiguration( SystemConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   /**
    * Method to determine whether the given is used by this {@link PreferenceController}.
    * @param database the {@link JenkinsDatabase} in question.
    * @return true if same as given.
    */
   public boolean usesDatabase( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
   ConfigurationTree tree(){
      return tree;
   }//End Method
}//End Class
