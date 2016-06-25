/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.tree.item.BuildWallRootItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.ColoursTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DimensionsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.FontsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.JobPolicyTreeItem;
import uk.dangrew.jtt.configuration.item.ConfigurationItem;
import uk.dangrew.jtt.configuration.item.ConfigurationRootItem;

/**
 * The {@link ConfigurationTree} provides a {@link TreeView} for {@link ConfigurationItem}s.
 */
public class ConfigurationTree extends TreeView< ConfigurationItem > {
   
   private final ConfigurationTreeController controller;
   
   /**
    * Constructs a new {@link ConfigurationTree}.
    * @param controller the {@link ConfigurationTreeController} use to control the
    * overall {@link ConfigurationTreePane}.
    * @param leftConfiguration the left {@link BuildWallConfiguration}.
    * @param rightConfiguration the right {@link BuildWallConfiguration}.
    */
   public ConfigurationTree( 
            ConfigurationTreeController controller, 
            BuildWallConfiguration leftConfiguration, 
            BuildWallConfiguration rightConfiguration 
   ) {
      this.controller = controller;
      
      TreeItem< ConfigurationItem > root = new TreeItem<>( new ConfigurationRootItem() );
      insertBuildWallConfiguration( root, "Left", leftConfiguration );
      insertBuildWallConfiguration( root, "Right", rightConfiguration );
      setRoot( root );
      setShowRoot( false );
      
      getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
      getSelectionModel().selectedItemProperty().addListener( ( source, old, updated ) -> updated.getValue().handleBeingSelected() );
   }//End Constructor
   
   /**
    * Method to insert the {@link TreeItem}s for the given {@link BuildWallConfiguration} to the root.
    * @param root the {@link TreeItem} to add to.
    * @param identifier the identifier for the build wall being configured.
    * @param configuration the {@link BuildWallConfiguration} associated.
    */
   private void insertBuildWallConfiguration( TreeItem< ConfigurationItem > root, String identifier, BuildWallConfiguration configuration ) {
      TreeItem<ConfigurationItem> buildWallRoot = new TreeItem<>(  
               new BuildWallRootItem( identifier, controller ) 
      );
      buildWallRoot.setExpanded( true );
      
      TreeItem<ConfigurationItem> dimensions = new TreeItem<>(
               new DimensionsTreeItem( controller, configuration ) 
      );            
      buildWallRoot.getChildren().add( dimensions );
      
      TreeItem<ConfigurationItem> policies = new TreeItem<>(
               new JobPolicyTreeItem( controller, configuration ) 
      );            
      buildWallRoot.getChildren().add( policies );
      
      TreeItem<ConfigurationItem> fonts = new TreeItem<>(
               new FontsTreeItem( controller, configuration ) 
      );            
      buildWallRoot.getChildren().add( fonts );
      
      TreeItem<ConfigurationItem> colours = new TreeItem<>(
               new ColoursTreeItem( controller, configuration ) 
      );            
      buildWallRoot.getChildren().add( colours );
      
      root.getChildren().add( buildWallRoot );
   }//End Method
   
}//End Class
