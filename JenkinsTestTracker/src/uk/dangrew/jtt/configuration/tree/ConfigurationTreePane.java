/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;

/**
 * The {@link ConfigurationTreePane} brings together the {@link ConfigurationTree} and
 * {@link ConfigurationTreeContent} into a single pane.
 */
public class ConfigurationTreePane extends BorderPane {
   
   static final double DEFAULT_DIVIDER_POSITION = 0.4;

   /**
    * Constructs a new {@link ConfigurationTreePane}.
    */
   public ConfigurationTreePane( ) {
      BuildWallConfiguration leftConfiguration = new BuildWallConfigurationImpl();
      BuildWallConfiguration rightConfiguration = new BuildWallConfigurationImpl();
      
      ConfigurationTreeContent content = new ConfigurationTreeContent();
      
      ConfigurationTreeController controller = new ConfigurationTreeController( content );
      ConfigurationTree tree = new ConfigurationTree( controller, leftConfiguration, rightConfiguration );
               
      SplitPane split = new SplitPane( tree, content );
      split.setDividerPositions( DEFAULT_DIVIDER_POSITION );
      
      setCenter( split );
   }//End Constructor

}//End Class
