/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.content;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * The {@link ConfigurationTreeContent} provides a layout for the content of configuring items
 * associated with {@link uk.dangrew.jtt.configuration.tree.ConfigurationTree} {@link uk.dangrew.jtt.configuration.item.ConfigurationItem}s.
 */
public class ConfigurationTreeContent extends BorderPane {

   static final double OUTER_OFFSETS = 10;
   static final double INNER_INSETS = 5;
   private final BorderPane top;
   private final BorderPane center;
   
   /**
    * Constructs a new {@link ConfigurationTreeContent}.
    */
   public ConfigurationTreeContent() {
      top = new BorderPane();
      top.setPadding( new Insets( INNER_INSETS ) );
      setTop( top );
      
      center = new BorderPane();
      center.setPadding( new Insets( INNER_INSETS ) );
      setCenter( center );
      
      setPadding( new Insets( OUTER_OFFSETS ) );
   }//End Constructor
   
   /**
    * Method to set the content to display in an appropriate way for configuring.
    * @param title the {@link Node} describing the title of the configuration.
    * @param content the {@link Node} providing the configuration options.
    */
   public void setContent( Node title, Node content ) {
      top.setCenter( title );
      center.setCenter( content );
   }//End Method
   
   BorderPane top(){
      return top;
   }//End Method
   
   BorderPane center(){
      return center;
   }//End Method
   
}//End Class
