/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.BuildWallRootItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.ColoursTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DimensionsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DualBuildWallRootItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DualPropertiesTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.FontsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.JobPolicyTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.SoundsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.ThemesTreeItem;
import uk.dangrew.jtt.configuration.item.ConfigurationItem;
import uk.dangrew.jtt.configuration.item.ConfigurationRootItem;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.configuration.system.SystemVersionItem;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.mc.configuration.tree.item.JobProgressRootItem;
import uk.dangrew.jtt.mc.configuration.tree.item.ManagementConsoleRootItem;
import uk.dangrew.jtt.mc.configuration.tree.item.NotificationsRootItem;
import uk.dangrew.jtt.mc.configuration.tree.item.UserAssignmentsRootItem;

/**
 * {@link ConfigurationTree} test.
 */
public class ConfigurationTreeTest {

   private static final int LEFT_ROOT_INDEX = 3;
   private static final int RIGHT_ROOT_INDEX = 4;
   
   @Mock private PreferenceController controller;
   private SystemConfiguration systemConfiguration;
   private ConfigurationTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      MockitoAnnotations.initMocks( this );
      systemConfiguration = new SystemConfiguration();
      systemUnderTest = new ConfigurationTree( controller, systemConfiguration );
   }//End Method

   @Test public void shouldOnlyAllowSingleItemSelections(){
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.SINGLE ) );
   }//End Method
   
   @Test public void rootShouldBePresentAndExpandedButHidden(){
      assertThat( systemUnderTest.isShowRoot(), is( false ) );
      
      TreeItem< ConfigurationItem > root = systemUnderTest.getRoot();
      assertThat( root.isExpanded(), is( true ) );
   }//End Method
      
   /**
    * Convenience method to get the {@link ConfigurationItem} from a parent.
    * @param parent the {@link TreeItem} to get from.
    * @param index the index of the child.
    * @return the {@link ConfigurationItem} associated with the child at index.
    */
   private ConfigurationItem getItem( TreeItem< ConfigurationItem > parent, int index ) {
      return parent.getChildren().get( index ).getValue();
   }//End Method
   
   @Test public void buildWallRootShouldBePresentAndExpanded(){
      TreeItem< ConfigurationItem > root = systemUnderTest.getRoot();
      assertThat( root.getChildren(), hasSize( 3 ) );
      
      assertThat( getItem( root, 0 ), is( systemUnderTest.systemVersion().getValue() ) );
      assertThat( systemUnderTest.systemVersion().isExpanded(), is( true ) );
      assertThat( systemUnderTest.systemVersion().getValue(), is( instanceOf( SystemVersionItem.class ) ) );
      
      assertThat( getItem( root, 1 ), is( systemUnderTest.dualWallRoot().getValue() ) );
      assertThat( systemUnderTest.dualWallRoot().isExpanded(), is( true ) );
      assertThat( systemUnderTest.dualWallRoot().getValue(), is( instanceOf( DualBuildWallRootItem.class ) ) );
      
      assertThat( getItem( root, 2 ), is( systemUnderTest.mcRoot().getValue() ) );
      assertThat( systemUnderTest.mcRoot().isExpanded(), is( true ) );
      assertThat( systemUnderTest.mcRoot().getValue(), is( instanceOf( ManagementConsoleRootItem.class ) ) );
   }//End Method
   
   @Test public void dualWallRootShouldBePresentAndExpanded(){
      TreeItem< ConfigurationItem > dualWallRoot = systemUnderTest.dualWallRoot();
      assertThat( dualWallRoot.isExpanded(), is( true ) );
      
      assertThat( dualWallRoot.getChildren().get( 0 ).getChildren(), hasSize( 0 ) );
      getItem( dualWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      assertThat( getItem( dualWallRoot, 0 ), is( instanceOf( DualPropertiesTreeItem.class ) ) );
      assertThat( getItem( dualWallRoot, 0 ).isAssociatedWith( systemConfiguration.getDualConfiguration() ), is( true ) );
      
      assertThat( dualWallRoot.getChildren().get( LEFT_ROOT_INDEX ).getChildren(), hasSize( 4 ) );
      getItem( dualWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
      assertThat( getItem( dualWallRoot, LEFT_ROOT_INDEX ), is( instanceOf( BuildWallRootItem.class ) ) );
      
      assertThat( dualWallRoot.getChildren().get( RIGHT_ROOT_INDEX ).getChildren(), hasSize( 4 ) );
      getItem( dualWallRoot, 2 ).handleBeingSelected();
      verify( controller, times( 3 ) ).displayContent( Mockito.any(), Mockito.any() );
      assertThat( getItem( dualWallRoot, RIGHT_ROOT_INDEX ), is( instanceOf( BuildWallRootItem.class ) ) );
   }//End Method
   
   @Test public void dimensionItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( LEFT_ROOT_INDEX );
      assertThat( getItem( leftBuildWallRoot, 0 ), is( instanceOf( DimensionsTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 0 ).isAssociatedWith( systemConfiguration.getLeftConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( RIGHT_ROOT_INDEX );
      assertThat( getItem( rightBuildWallRoot, 0 ), is( instanceOf( DimensionsTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 0 ).isAssociatedWith( systemConfiguration.getRightConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void themesShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > themes = systemUnderTest.dualWallRoot().getChildren().get( 1 );
      assertThat( themes.getValue(), is( instanceOf( ThemesTreeItem.class ) ) );
      assertThat( themes, is( systemUnderTest.themes() ) );
      
      themes.getValue().handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void soundsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > sounds = systemUnderTest.dualWallRoot().getChildren().get( 2 );
      assertThat( sounds.getValue(), is( instanceOf( SoundsTreeItem.class ) ) );
      assertThat( sounds, is( systemUnderTest.sounds() ) );
      
      sounds.getValue().handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void policyItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( LEFT_ROOT_INDEX );
      assertThat( getItem( leftBuildWallRoot, 1 ), is( instanceOf( JobPolicyTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 1 ).isAssociatedWith( systemConfiguration.getLeftConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( RIGHT_ROOT_INDEX );
      assertThat( getItem( rightBuildWallRoot, 1 ), is( instanceOf( JobPolicyTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 1 ).isAssociatedWith( systemConfiguration.getRightConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void fontItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( LEFT_ROOT_INDEX );
      assertThat( getItem( leftBuildWallRoot, 2 ), is( instanceOf( FontsTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 2 ).isAssociatedWith( systemConfiguration.getLeftConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( RIGHT_ROOT_INDEX );
      assertThat( getItem( rightBuildWallRoot, 2 ), is( instanceOf( FontsTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 2 ).isAssociatedWith( systemConfiguration.getRightConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void colourItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( LEFT_ROOT_INDEX );
      assertThat( getItem( leftBuildWallRoot, 3 ), is( instanceOf( ColoursTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 3 ).isAssociatedWith( systemConfiguration.getLeftConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( RIGHT_ROOT_INDEX );
      assertThat( getItem( rightBuildWallRoot, 3 ), is( instanceOf( ColoursTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 3 ).isAssociatedWith( systemConfiguration.getRightConfiguration() ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void managementConsoleShouldBePresent(){
      TreeItem< ConfigurationItem > root = systemUnderTest.mcRoot();
      assertThat( root.getValue(), is( instanceOf( ManagementConsoleRootItem.class ) ) );
      root.getValue().handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      assertThat( root.isExpanded(), is( true ) );
   }//End Method
   
   @Test public void managementConsoleChildrenShouldBePresent(){
      TreeItem< ConfigurationItem > root = systemUnderTest.mcRoot();
      assertThat( getItem( root, 0 ), is( systemUnderTest.notificationsRoot().getValue() ) );
      assertThat( getItem( root, 0 ), is( instanceOf( NotificationsRootItem.class ) ) );
      getItem( root, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      assertThat( getItem( root, 1 ), is( systemUnderTest.userAssignmentsRoot().getValue() ) );
      assertThat( getItem( root, 1 ), is( instanceOf( UserAssignmentsRootItem.class ) ) );
      getItem( root, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
      
      assertThat( getItem( root, 2 ), is( systemUnderTest.jobProgressRoot().getValue() ) );
      assertThat( getItem( root, 2 ), is( instanceOf( JobProgressRootItem.class ) ) );
      getItem( root, 2 ).handleBeingSelected();
      verify( controller, times( 3 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void shouldHandleSelectionByPassingOnToItem(){
      ConfigurationItem item = spy( new ConfigurationRootItem() );
      TreeItem< ConfigurationItem > additionalItem = new TreeItem<>( item );
      
      systemUnderTest.getRoot().getChildren().add( additionalItem );
      verify( item, never() ).handleBeingSelected();
      systemUnderTest.getSelectionModel().select( additionalItem );
      verify( item ).handleBeingSelected();
   }//End Method
   
   @Test public void shouldSelectRoots(){
      systemUnderTest.select( ConfigurationTreeItems.SystemVersion );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.systemVersion() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.SystemVersion ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.DualWallRoot );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.dualWallRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.DualWallRoot ), is( true ) );
   }//End Method
   
   @Test public void shouldSelectDualWallProperties(){
      systemUnderTest.select( ConfigurationTreeItems.DualWallProperties );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.dualWallProperties() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.DualWallProperties ), is( true ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.DualWallRoot ), is( false ) );
   }//End Method
   
   @Test public void shouldSelectThemeBuilder(){
      systemUnderTest.select( ConfigurationTreeItems.Themes );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.themes() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.Themes ), is( true ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.DualWallRoot ), is( false ) );
   }//End Method   
   
   @Test public void shouldSelectSounds(){
      systemUnderTest.select( ConfigurationTreeItems.Sounds );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.sounds() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.Sounds ), is( true ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.DualWallRoot ), is( false ) );
   }//End Method   
   
   @Test public void shouldSelectWallRoots(){
      systemUnderTest.select( ConfigurationTreeItems.LeftWallRoot );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.leftWallRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.LeftWallRoot ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.RightWallRoot );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.rightWallRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.RightWallRoot ), is( true ) );
   }//End Method
   
   @Test public void shouldSelectLeftSubItems(){
      systemUnderTest.select( ConfigurationTreeItems.LeftDimension );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.leftWallRoot().getChildren().get( 0 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.LeftDimension ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.LeftJobPolicies );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.leftWallRoot().getChildren().get( 1 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.LeftJobPolicies ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.LeftFonts );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.leftWallRoot().getChildren().get( 2 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.LeftFonts ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.LeftColours );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.leftWallRoot().getChildren().get( 3 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.LeftColours ), is( true ) );
   }//End Method
   
   @Test public void shouldSelectRightSubItems(){
      systemUnderTest.select( ConfigurationTreeItems.RightDimension );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.rightWallRoot().getChildren().get( 0 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.RightDimension ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.RightJobPolicies );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.rightWallRoot().getChildren().get( 1 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.RightJobPolicies ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.RightFonts );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.rightWallRoot().getChildren().get( 2 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.RightFonts ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.RightColours );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.rightWallRoot().getChildren().get( 3 ) ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.RightColours ), is( true ) );
   }//End Method
   
   @Test public void shouldSelectManagementConsoleAndSubsections(){
      systemUnderTest.select( ConfigurationTreeItems.ManagementConsole );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.mcRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.ManagementConsole ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.Notifications );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.notificationsRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.Notifications ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.UserAssignments );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.userAssignmentsRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.UserAssignments ), is( true ) );
      
      systemUnderTest.select( ConfigurationTreeItems.JobProgress );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( systemUnderTest.jobProgressRoot() ) );
      assertThat( systemUnderTest.isSelected( ConfigurationTreeItems.JobProgress ), is( true ) );
   }//End Method
   
   @Test public void shouldIgnoreNullSelect(){
      systemUnderTest.select( null );
   }//End Method
}//End Class
