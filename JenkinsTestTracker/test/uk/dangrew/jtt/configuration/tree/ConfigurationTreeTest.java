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
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.properties.DualConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.DualConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.tree.item.BuildWallRootItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.ColoursTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DimensionsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.DualPropertiesTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.FontsTreeItem;
import uk.dangrew.jtt.buildwall.configuration.tree.item.JobPolicyTreeItem;
import uk.dangrew.jtt.configuration.item.ConfigurationItem;
import uk.dangrew.jtt.configuration.item.ConfigurationRootItem;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;

/**
 * {@link ConfigurationTree} test.
 */
public class ConfigurationTreeTest {

   @Mock private ConfigurationTreeController controller;
   private DualConfiguration dualConfiguration;
   private BuildWallConfiguration leftConfiguration;
   private BuildWallConfiguration rightConfiguration;
   private ConfigurationTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      MockitoAnnotations.initMocks( this );
      dualConfiguration = new DualConfigurationImpl();
      leftConfiguration = new BuildWallConfigurationImpl();
      rightConfiguration = new BuildWallConfigurationImpl();
      systemUnderTest = new ConfigurationTree( controller, dualConfiguration, leftConfiguration, rightConfiguration );
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
      assertThat( root.getChildren(), hasSize( 1 ) );
      
      assertThat( getItem( root, 0 ), is( systemUnderTest.dualWallRoot().getValue() ) );
   }//End Method
   
   @Test public void dualWallRootShouldBePresentAndExpanded(){
      TreeItem< ConfigurationItem > dualWallRoot = systemUnderTest.dualWallRoot();
      assertThat( dualWallRoot.isExpanded(), is( true ) );
      
      assertThat( dualWallRoot.getChildren().get( 0 ).getChildren(), hasSize( 0 ) );
      getItem( dualWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      assertThat( getItem( dualWallRoot, 0 ), is( instanceOf( DualPropertiesTreeItem.class ) ) );
      
      assertThat( dualWallRoot.getChildren().get( 1 ).getChildren(), hasSize( 4 ) );
      getItem( dualWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
      assertThat( getItem( dualWallRoot, 1 ), is( instanceOf( BuildWallRootItem.class ) ) );
      
      assertThat( dualWallRoot.getChildren().get( 2 ).getChildren(), hasSize( 4 ) );
      getItem( dualWallRoot, 2 ).handleBeingSelected();
      verify( controller, times( 3 ) ).displayContent( Mockito.any(), Mockito.any() );
      assertThat( getItem( dualWallRoot, 2 ), is( instanceOf( BuildWallRootItem.class ) ) );
   }//End Method
   
   @Test public void dimensionItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 1 );
      assertThat( getItem( leftBuildWallRoot, 0 ), is( instanceOf( DimensionsTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 0 ).isAssociatedWith( leftConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 2 );
      assertThat( getItem( rightBuildWallRoot, 0 ), is( instanceOf( DimensionsTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 0 ).isAssociatedWith( rightConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void policyItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 1 );
      assertThat( getItem( leftBuildWallRoot, 1 ), is( instanceOf( JobPolicyTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 1 ).isAssociatedWith( leftConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 2 );
      assertThat( getItem( rightBuildWallRoot, 1 ), is( instanceOf( JobPolicyTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 1 ).isAssociatedWith( rightConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void fontItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 1 );
      assertThat( getItem( leftBuildWallRoot, 2 ), is( instanceOf( FontsTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 2 ).isAssociatedWith( leftConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 2 );
      assertThat( getItem( rightBuildWallRoot, 2 ), is( instanceOf( FontsTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 2 ).isAssociatedWith( rightConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void colourItemsShouldBePresentAndAssociated(){
      TreeItem< ConfigurationItem > leftBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 1 );
      assertThat( getItem( leftBuildWallRoot, 3 ), is( instanceOf( ColoursTreeItem.class ) ) );
      assertThat( getItem( leftBuildWallRoot, 3 ).isAssociatedWith( leftConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 0 ).handleBeingSelected();
      verify( controller ).displayContent( Mockito.any(), Mockito.any() );
      
      TreeItem< ConfigurationItem > rightBuildWallRoot = systemUnderTest.dualWallRoot().getChildren().get( 2 );
      assertThat( getItem( rightBuildWallRoot, 3 ), is( instanceOf( ColoursTreeItem.class ) ) );
      assertThat( getItem( rightBuildWallRoot, 3 ).isAssociatedWith( rightConfiguration ), is( true ) );
      
      getItem( leftBuildWallRoot, 1 ).handleBeingSelected();
      verify( controller, times( 2 ) ).displayContent( Mockito.any(), Mockito.any() );
   }//End Method
   
   @Test public void shouldHandleSelectionByPassingOnToItem(){
      ConfigurationItem item = spy( new ConfigurationRootItem() );
      TreeItem< ConfigurationItem > additionalItem = new TreeItem<>( item );
      
      systemUnderTest.getRoot().getChildren().add( additionalItem );
      verify( item, never() ).handleBeingSelected();
      systemUnderTest.getSelectionModel().select( additionalItem );
      verify( item ).handleBeingSelected();
   }//End Method
}//End Class
