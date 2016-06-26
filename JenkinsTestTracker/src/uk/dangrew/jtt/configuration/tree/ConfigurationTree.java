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
import uk.dangrew.jtt.buildwall.configuration.properties.DualConfiguration;
import uk.dangrew.jtt.buildwall.configuration.tree.item.BuildWallRootItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.ColoursTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DimensionsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DualBuildWallRootItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DualPropertiesTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.FontsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.JobPolicyTreeItem;
import uk.dangrew.jtt.configuration.item.ConfigurationItem;
import uk.dangrew.jtt.configuration.item.ConfigurationRootItem;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;

/**
 * The {@link ConfigurationTree} provides a {@link TreeView} for {@link ConfigurationItem}s.
 */
public class ConfigurationTree extends TreeView< ConfigurationItem > {
   
   private final ConfigurationTreeController controller;
   private final TreeItem< ConfigurationItem > root;
   private final TreeItem< ConfigurationItem > dualWallRoot;
   
   /**
    * Constructs a new {@link ConfigurationTree}.
    * @param controller the {@link ConfigurationTreeController} use to control the
    * overall {@link ConfigurationTreePane}.
    * @param systemConfiguration the {@link SystemConfiguration}.
    */
   public ConfigurationTree( 
            ConfigurationTreeController controller, 
            SystemConfiguration systemConfiguration 
   ) {
      this.controller = controller;
      
      root = new TreeItem<>( new ConfigurationRootItem() );
      
      dualWallRoot = new TreeItem<>( new DualBuildWallRootItem( controller ) );
      dualWallRoot.setExpanded( true );
      root.getChildren().add( dualWallRoot );
      
      insertDualProperties( dualWallRoot, systemConfiguration.getDualConfiguration() );
      insertBuildWallConfiguration( dualWallRoot, "Left", systemConfiguration.getLeftConfiguration() );
      insertBuildWallConfiguration( dualWallRoot, "Right", systemConfiguration.getRightConfiguration() );
      setRoot( root );
      setShowRoot( false );
      
      getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
      getSelectionModel().selectedItemProperty().addListener( ( source, old, updated ) -> updated.getValue().handleBeingSelected() );
   }//End Constructor
   
   /**
    * Method to insert the {@link DualConfiguration} items into the given root.
    * @param root the {@link TreeItem} root to insert in to.
    * @param configuration the {@link DualConfiguration} to configure.
    */
   private void insertDualProperties( TreeItem< ConfigurationItem > root, DualConfiguration configuration ){
      TreeItem<ConfigurationItem> dualProperties = new TreeItem<>(  
               new DualPropertiesTreeItem( controller, configuration ) 
      );
      dualProperties.setExpanded( true );  
      
      root.getChildren().add( dualProperties );
   }//End Method
   
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
   
   TreeItem< ConfigurationItem > root(){
      return root;
   }//End Method
   
   TreeItem< ConfigurationItem > dualWallRoot(){
      return dualWallRoot;
   }//End Method
   
}//End Class
