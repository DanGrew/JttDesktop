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

/**
 * The {@link ConfigurationTreePane} brings together the {@link ConfigurationTree} and
 * {@link ConfigurationTreeContent} into a single pane.
 */
public class ConfigurationTreePane extends BorderPane {
   
   private final ConfigurationTree tree;
   private final ConfigurationTreeContent content;
   
   /**
    * Constructs a new {@link ConfigurationTreePane}.
    * @param tree the {@link ConfigurationTree} to display.
    * @param content the {@link ConfigurationTreeContent} to place in the pane.
    */
   public ConfigurationTreePane(
            ConfigurationTree tree,
            ConfigurationTreeContent content 
   ) {
      this.tree = tree;
      this.tree.select( ConfigurationTreeItems.SystemVersion );
      this.content = content;
      setLeft( tree );
      
      ScrollPane scroller = new ScrollPane( content );
      scroller.setHbarPolicy( ScrollBarPolicy.NEVER );
      scroller.setFitToWidth( true );
      setCenter( scroller );
   }//End Constructor
   
   /**
    * Method to determine whether the given {@link ConfigurationTree} is associated.
    * @param tree the {@link ConfigurationTree} in question.
    * @return true if equal.
    */
   public boolean hasTree( ConfigurationTree tree ) {
      return this.tree == tree;
   }//End Method
   
   /**
    * Method to determine whether the given {@link ConfigurationTreeContent} is associated.
    * @param tree the {@link ConfigurationTreeContent} in question.
    * @return true if equal.
    */
   public boolean hasContent( ConfigurationTreeContent content ) {
      return this.content == content;
   }//End Method
   
   ConfigurationTree tree(){
      return tree;
   }//End Method

}//End Class
