/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.DualConfiguration;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;

/**
 * The {@link ConfigurationTreePane} brings together the {@link ConfigurationTree} and
 * {@link ConfigurationTreeContent} into a single pane.
 */
public class ConfigurationTreePane extends BorderPane {
   
   /**
    * Constructs a new {@link ConfigurationTreePane}.
    * @param dualConfiguration the {@link DualConfiguration} for configuring the dual wall itself.
    * @param leftConfiguration the {@link BuildWallConfiguration} for the left wall.
    * @param rightConfiguration the {@link BuildWallConfiguration} for the right wall.
    */
   public ConfigurationTreePane( DualConfiguration dualConfiguration, BuildWallConfiguration leftConfiguration, BuildWallConfiguration rightConfiguration ) {
      ConfigurationTreeContent content = new ConfigurationTreeContent();
      
      ConfigurationTreeController controller = new ConfigurationTreeController( content );
      ConfigurationTree tree = new ConfigurationTree( controller, dualConfiguration, leftConfiguration, rightConfiguration );
      
      tree.getSelectionModel().select( 0 );
      setLeft( tree );
      
      ScrollPane scroller = new ScrollPane( content );
      scroller.setHbarPolicy( ScrollBarPolicy.NEVER );
      scroller.setFitToWidth( true );
      setCenter( scroller );
   }//End Constructor

}//End Class
