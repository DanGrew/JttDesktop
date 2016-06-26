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
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;

/**
 * The {@link ConfigurationTreePane} brings together the {@link ConfigurationTree} and
 * {@link ConfigurationTreeContent} into a single pane.
 */
public class ConfigurationTreePane extends BorderPane {
   
   /**
    * Constructs a new {@link ConfigurationTreePane}.
    * @param systemConfiguration the {@link SystemConfiguration}.
    */
   public ConfigurationTreePane( SystemConfiguration systemConfiguration ) {
      ConfigurationTreeContent content = new ConfigurationTreeContent();
      
      ConfigurationTreeController controller = new ConfigurationTreeController( content );
      ConfigurationTree tree = new ConfigurationTree( controller, systemConfiguration );
      
      tree.getSelectionModel().select( 0 );
      setLeft( tree );
      
      ScrollPane scroller = new ScrollPane( content );
      scroller.setHbarPolicy( ScrollBarPolicy.NEVER );
      scroller.setFitToWidth( true );
      setCenter( scroller );
   }//End Constructor

}//End Class
