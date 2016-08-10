/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.item;

import javafx.scene.Node;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * The {@link SimpleConfigurationItem} provides a base class for {@link ConfigurationItem} that
 * simply call through to a {@link ConfigurationTreeController}.
 */
public abstract class SimpleConfigurationItem implements ConfigurationItem {

   private final String itemName;
   private final Node contentTitle;
   private final PreferenceController controller;
   private final Node content;
   
   /**
    * Constructs a new {@link SimpleConfigurationItem}.
    * @param itemName the name of the item in the {@link javafx.scene.control.TreeView}.
    * @param contentTitle the {@link Node} representing the title of the configuration.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    * @param content the {@link Node} providing the configuration options.
    */
   protected SimpleConfigurationItem( 
            String itemName, 
            Node contentTitle,
            PreferenceController controller, 
            Node content 
   ) {
      this.itemName = itemName;
      this.contentTitle = contentTitle;
      this.controller = controller;
      this.content = content;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void handleBeingSelected() {
      controller.displayContent( contentTitle, content );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWith( Object configuration ) {
      return false;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String toString() {
      return itemName;
   }//End Method
}//End Class
