/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.main;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import uk.dangrew.jtt.desktop.configuration.tree.ConfigurationTreeItems;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceBehaviour;
import uk.dangrew.jtt.desktop.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.desktop.environment.preferences.WindowPolicy;
import uk.dangrew.jtt.desktop.utility.system.OperatingSystem;
import uk.dangrew.kode.event.structure.Event;

/**
 * {@link EnvironmentMenuBar} provides a {@link MenuBar} for the {@link EnvironmentWindow}.
 */
public class EnvironmentMenuBar extends MenuBar {
   
   static final String JENKINS_TEST_TRACKER_MENU = "Jenkins Test Tracker";
   static final String PREFERENCES_MENU = "Preferences";
   static final String ABOUT_MENU = "About";
   
   private final Menu applicationMenu;
   private final MenuItem preferences;
   private final MenuItem about;
   
   /**
    * Constructs a new {@link EnvironmentMenuBar}.
    */
   public EnvironmentMenuBar() {
      this( new OperatingSystem() );
   }//End Constructor
   
   /**
    * Constructs a new {@link EnvironmentMenuBar}.
    * @param operatingSystem the {@link OperatingSystem} for enabling platform specific features.
    */
   EnvironmentMenuBar( OperatingSystem operatingSystem ) {
      if ( operatingSystem.isMac() ) {
         setUseSystemMenuBar( true );
      }
      
      applicationMenu = new Menu( JENKINS_TEST_TRACKER_MENU );
      
      PreferencesOpenEvent preferenceOpener = new PreferencesOpenEvent();
      preferences = new MenuItem( PREFERENCES_MENU );
      preferences.setOnAction( event -> preferenceOpener.fire( 
               new Event<>( new PreferenceBehaviour( WindowPolicy.Open, null ) )
      ) );
      about = new MenuItem( ABOUT_MENU );
      about.setOnAction( event -> preferenceOpener.fire( 
               new Event<>( new PreferenceBehaviour( WindowPolicy.Open, ConfigurationTreeItems.SystemVersion ) ) 
      ) );
      applicationMenu.getItems().addAll( preferences, new SeparatorMenuItem(), about );
      
      getMenus().add( applicationMenu );
   }//End Constructor

   Menu applicationMenu(){
      return applicationMenu;
   }//End Method
   
   MenuItem preferences(){
      return preferences;
   }//End Method
   
   MenuItem about(){
      return about;
   }//End Method 
   
}//End Class
