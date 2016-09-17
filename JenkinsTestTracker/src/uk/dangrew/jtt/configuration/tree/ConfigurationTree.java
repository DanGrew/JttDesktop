/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import java.util.EnumMap;
import java.util.Map;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
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
import uk.dangrew.jtt.configuration.system.SystemVersionItem;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.mc.configuration.tree.item.JobProgressRootItem;
import uk.dangrew.jtt.mc.configuration.tree.item.ManagementConsoleRootItem;
import uk.dangrew.jtt.mc.configuration.tree.item.NotificationsRootItem;
import uk.dangrew.jtt.mc.configuration.tree.item.UserAssignmentsRootItem;

/**
 * The {@link ConfigurationTree} provides a {@link TreeView} for {@link ConfigurationItem}s.
 */
public class ConfigurationTree extends TreeView< ConfigurationItem > {
   
   private final PreferenceController controller;
   private final TreeItem< ConfigurationItem > root;
   private final TreeItem< ConfigurationItem > systemVersion;
   
   private final TreeItem< ConfigurationItem > dualWallProperties;
   private final TreeItem< ConfigurationItem > dualWallRoot;
   private final TreeItem< ConfigurationItem > leftWallRoot;
   private final TreeItem< ConfigurationItem > rightWallRoot;
   
   private final TreeItem< ConfigurationItem > mcRoot;
   private final TreeItem< ConfigurationItem > notificationsRoot;
   private final TreeItem< ConfigurationItem > userAssignmentsRoot;
   private final TreeItem< ConfigurationItem > jobProgressRoot;
   
   private final Map< ConfigurationTreeItems, TreeItem< ConfigurationItem > > itemMapping;
   
   /**
    * Constructs a new {@link ConfigurationTree}.
    * @param controller the {@link PreferenceController} use to control the
    * overall {@link ConfigurationTreePane}.
    * @param systemConfiguration the {@link SystemConfiguration}.
    */
   public ConfigurationTree( 
            PreferenceController controller, 
            SystemConfiguration systemConfiguration 
   ) {
      this.controller = controller;
      this.itemMapping = new EnumMap<>( ConfigurationTreeItems.class );
      
      this.root = new TreeItem<>( new ConfigurationRootItem() );
      
      this.systemVersion = new TreeItem<>( new SystemVersionItem( controller ) );
      this.systemVersion.setExpanded( true );
      this.root.getChildren().add( systemVersion );
      
      this.dualWallRoot = new TreeItem<>( new DualBuildWallRootItem( controller ) );
      this.dualWallRoot.setExpanded( true );
      this.root.getChildren().add( dualWallRoot );
      
      this.dualWallProperties = insertDualProperties( dualWallRoot, systemConfiguration.getDualConfiguration() );
      this.leftWallRoot = insertBuildWallConfiguration( dualWallRoot, "Left", systemConfiguration.getLeftConfiguration() );
      this.rightWallRoot = insertBuildWallConfiguration( dualWallRoot, "Right", systemConfiguration.getRightConfiguration() );
      
      this.mcRoot = insertManageConsoleConfiguration( root );
      this.notificationsRoot = insertNotificationsConfiguration( mcRoot );
      this.userAssignmentsRoot = insertUserAssignmentsConfiguration( mcRoot );
      this.jobProgressRoot = insertJobProgressConfiguration( mcRoot );
      
      setRoot( root );
      setShowRoot( false );
      
      populateItemMappings();
      
      getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
      getSelectionModel().selectedItemProperty().addListener( ( source, old, updated ) -> updated.getValue().handleBeingSelected() );
   }//End Constructor
   
   /**
    * Method to insert the {@link DualConfiguration} items into the given root.
    * @param root the {@link TreeItem} root to insert in to.
    * @param configuration the {@link DualConfiguration} to configure.
    */
   private TreeItem<ConfigurationItem> insertDualProperties( TreeItem< ConfigurationItem > root, DualWallConfiguration configuration ){
      TreeItem<ConfigurationItem> dualProperties = new TreeItem<>(  
               new DualPropertiesTreeItem( controller, configuration ) 
      );
      dualProperties.setExpanded( true );  
      
      root.getChildren().add( dualProperties );
      
      return dualProperties;
   }//End Method
   
   /**
    * Method to insert the {@link TreeItem}s for the given {@link BuildWallConfiguration} to the root.
    * @param root the {@link TreeItem} to add to.
    * @param identifier the identifier for the build wall being configured.
    * @param configuration the {@link BuildWallConfiguration} associated.
    * @return the {@link TreeItem} containing all specific items.
    */
   private TreeItem< ConfigurationItem > insertBuildWallConfiguration( TreeItem< ConfigurationItem > root, String identifier, BuildWallConfiguration configuration ) {
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
      
      return buildWallRoot;
   }//End Method
   
   /**
    * Method to insert the management console items.
    * @param root the root of the items.
    * @return the constructed root.
    */
   private TreeItem< ConfigurationItem > insertManageConsoleConfiguration( TreeItem< ConfigurationItem > root ) {
      return createNewTreeItemInsideRoot( new ManagementConsoleRootItem( controller ), true, root ); 
   }//End Method
   
   /**
    * Method to insert the notifications items.
    * @param root the root of the items.
    * @return the constructed root.
    */
   private TreeItem< ConfigurationItem > insertNotificationsConfiguration( TreeItem< ConfigurationItem > root ) {
      return createNewTreeItemInsideRoot( new NotificationsRootItem( controller ), true, root );
   }//End Method
   
   /**
    * Method to insert the user assignments items.
    * @param root the root of the items.
    * @return the constructed root.
    */
   private TreeItem< ConfigurationItem > insertUserAssignmentsConfiguration( TreeItem< ConfigurationItem > root ) {
      return createNewTreeItemInsideRoot( new UserAssignmentsRootItem( controller ), true, root );
   }//End Method
   
   /**
    * Method to insert the job progress items.
    * @param root the root of the items.
    * @return the constructed root.
    */
   private TreeItem< ConfigurationItem > insertJobProgressConfiguration( TreeItem< ConfigurationItem > root ) {
      return createNewTreeItemInsideRoot( new JobProgressRootItem( controller ), true, root );
   }//End Method
   
   /**
    * Method to create a {@link TreeItem} with the given {@link ConfigurationItem}, epxanded and present in the given root.
    * @param item the {@link ConfigurationItem} associated.
    * @param expanded expanded or not.
    * @param root the {@link TreeItem} root to place it in.
    * @return the constructed {@link TreeItem}.
    */
   private TreeItem< ConfigurationItem > createNewTreeItemInsideRoot( ConfigurationItem item, boolean expanded, TreeItem< ConfigurationItem > root ) {
      TreeItem<ConfigurationItem> treeItem = new TreeItem<>( item );
      treeItem.setExpanded( expanded );
      root.getChildren().add( treeItem );
      return treeItem;
   }//End Method
   
   /**
    * Method to populate the item mappings for selection.
    */
   private void populateItemMappings(){
      itemMapping.put( ConfigurationTreeItems.SystemVersion, systemVersion );
      
      itemMapping.put( ConfigurationTreeItems.DualWallProperties, dualWallProperties ); 
      itemMapping.put( ConfigurationTreeItems.DualWallRoot, dualWallRoot );
      itemMapping.put( ConfigurationTreeItems.LeftWallRoot, leftWallRoot );
      itemMapping.put( ConfigurationTreeItems.LeftDimension, leftWallRoot.getChildren().get( 0 ) );
      itemMapping.put( ConfigurationTreeItems.LeftJobPolicies, leftWallRoot.getChildren().get( 1 ) );
      itemMapping.put( ConfigurationTreeItems.LeftFonts, leftWallRoot.getChildren().get( 2 ) );
      itemMapping.put( ConfigurationTreeItems.LeftColours, leftWallRoot.getChildren().get( 3 ) );
      itemMapping.put( ConfigurationTreeItems.RightWallRoot, rightWallRoot );
      itemMapping.put( ConfigurationTreeItems.RightDimension, rightWallRoot.getChildren().get( 0 ) );
      itemMapping.put( ConfigurationTreeItems.RightJobPolicies, rightWallRoot.getChildren().get( 1 ) );
      itemMapping.put( ConfigurationTreeItems.RightFonts, rightWallRoot.getChildren().get( 2 ) );
      itemMapping.put( ConfigurationTreeItems.RightColours, rightWallRoot.getChildren().get( 3 ) );
      
      itemMapping.put( ConfigurationTreeItems.ManagementConsole, mcRoot );
      itemMapping.put( ConfigurationTreeItems.Notifications, notificationsRoot );
      itemMapping.put( ConfigurationTreeItems.UserAssignments, userAssignmentsRoot );
      itemMapping.put( ConfigurationTreeItems.JobProgress, jobProgressRoot );
//      itemMapping.put( ConfigurationTreeItems.JobProgressFiltering, jobProgressRoot.getChildren().get( 0 ) );
   }//End Method
   
   /**
    * Method to determine whether the given {@link ConfigurationTreeItems} is selected.
    * @param item the {@link ConfigurationTreeItems} in question.
    * @return true if selected.
    */
   public boolean isSelected( ConfigurationTreeItems item ) {
      TreeItem< ConfigurationItem > treeItem = itemMapping.get( item );
      TreeItem< ConfigurationItem > selected = getSelectionModel().getSelectedItem();
      return treeItem == selected;
   }//End Method
   
   /**
    * Method to select the item associated with the given {@link ConfigurationTreeItems}.
    * @param item the {@link ConfigurationTreeItems} to select.
    */
   public void select( ConfigurationTreeItems item ) {
      TreeItem< ConfigurationItem > treeItem = itemMapping.get( item );
      if ( treeItem == null ) {
         return;
      }
      
      PlatformImpl.runAndWait( () -> getSelectionModel().select( treeItem ) );
   }//End Method
   
   TreeItem< ConfigurationItem > root(){
      return root;
   }//End Method
   
   TreeItem< ConfigurationItem > systemVersion(){
      return systemVersion;
   }//End Method
   
   TreeItem< ConfigurationItem > dualWallRoot(){
      return dualWallRoot;
   }//End Method
   
   TreeItem< ConfigurationItem > dualWallProperties(){
      return dualWallProperties;
   }//End Method
   
   TreeItem< ConfigurationItem > leftWallRoot(){
      return leftWallRoot;
   }//End Method
   
   TreeItem< ConfigurationItem > rightWallRoot(){
      return rightWallRoot;
   }//End Method
   
   TreeItem< ConfigurationItem > mcRoot() {
      return mcRoot;
   }//End Method
   
   TreeItem< ConfigurationItem > notificationsRoot() {
      return notificationsRoot;
   }//End Method
   
   TreeItem< ConfigurationItem > userAssignmentsRoot() {
      return userAssignmentsRoot;
   }//End Method
   
   TreeItem< ConfigurationItem > jobProgressRoot() {
      return jobProgressRoot;
   }//End Method
   
}//End Class
