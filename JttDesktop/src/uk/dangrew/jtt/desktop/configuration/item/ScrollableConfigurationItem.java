/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.item;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.javafx.css.DynamicCssOnlyProperties;

/**
 * {@link ScrollableConfigurationItem} provides a base class that wraps the given content
 * in a {@link ScrollPane}. This is important to make sure content is completely usable
 * but a choice as because a wrapping {@link ScrollPane} may effect nested {@link ScrollPane}s
 * if used.
 */
public class ScrollableConfigurationItem extends SimpleConfigurationItem {
   
   /**
    * Constructs a new {@link ScrollableConfigurationItem}.
    * @param itemName the name of the item in the {@link javafx.scene.control.TreeView}.
    * @param contentTitle the {@link Node} representing the title of the configuration.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    * @param content the {@link Node} providing the configuration options.
    */
   protected ScrollableConfigurationItem( 
            String itemName, 
            Node contentTitle, 
            PreferenceController controller, 
            Node content 
   ) {
      this( new DynamicCssOnlyProperties(), itemName, contentTitle, controller, content );
   }//End Constructor
   
   /**
    * Constructs a new {@link SimpleConfigurationItem}.
    * @param css the {@link DynamicCssOnlyProperties}.
    * @param itemName the name of the item in the {@link javafx.scene.control.TreeView}.
    * @param contentTitle the {@link Node} representing the title of the configuration.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    * @param content the {@link Node} providing the configuration options.
    */
   protected ScrollableConfigurationItem( 
            DynamicCssOnlyProperties css,
            String itemName, 
            Node contentTitle, 
            PreferenceController controller, 
            Node content 
   ) {
      super( itemName, contentTitle, controller, wrapInScroller( css, content ) );
   }//End Constructor

   /**
    * Method to wrap the content in a {@link ScrollPane}.
    * @param css the {@link DynamicCssOnlyProperties} for configuring the {@link ScrollPane}.
    * @param content the {@link Node} to wrap.
    * @return the {@link ScrollPane} wrapping.
    */
   private static Node wrapInScroller( DynamicCssOnlyProperties css, Node content ){
      ScrollPane scroller = new ScrollPane( content );
      scroller.setHbarPolicy( ScrollBarPolicy.NEVER );
      scroller.setFitToWidth( true );
      css.removeScrollPaneBorder( scroller );
      return scroller;
   }//End Method

}//End Class
