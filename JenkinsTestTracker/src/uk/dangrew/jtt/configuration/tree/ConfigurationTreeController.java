/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import javafx.scene.Node;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;

/**
 * The {@link ConfigurationTreeController} provides an intermediate decoupling object
 * that handles communication between elements of the {@link ConfigurationTree}.
 */
public class ConfigurationTreeController {
   
   private final ConfigurationTreeContent contentHolder;
   
   /**
    * Constructs a new {@link ConfigurationTreeController}.
    * @param contentHolder the {@link ConfigurationTreeContent} to control.
    */
   public ConfigurationTreeController( ConfigurationTreeContent contentHolder ) {
      this.contentHolder = contentHolder;
   }//End Constructor
   
   /**
    * Method to display the content in the {@link ConfigurationTreeContent}.
    * @param title the {@link Node} providing the title.
    * @param content the {@link Node} providing the configuration content.
    */
   public void displayContent( Node title, Node content ){
      contentHolder.setContent( title, content );
   }//End Method

}//End Class
