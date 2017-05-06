/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.event.ActionEvent;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.DimensionsPanel;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

/**
 * {@link DimensionsPanel} test.
 */
public class DimensionsPanelTest {
   
   private BuildWallConfiguration configuration;
   @Spy private JavaFxStyle styling;
   private DimensionsPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new DimensionsPanel( configuration, styling );
   }//End Method

   @Test public void shouldUseBoldLabels(){
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.columnsSpinnerLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.descriptionTypeLabel().getFont().getStyle() ) );
   }//End Method
   
   @Test public void simpleDescriptionShouldUpdateConfiguration(){
      systemUnderTest.defaultDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), not( JobPanelDescriptionProviders.Simple ) );
      
      systemUnderTest.simpleDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Simple ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void defaultDescriptionShouldUpdateConfiguration(){
      systemUnderTest.simpleDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), not( JobPanelDescriptionProviders.Default ) );
      
      systemUnderTest.defaultDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Default ) );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void detailedDescriptionShouldUpdateConfiguration(){
      systemUnderTest.simpleDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), not( JobPanelDescriptionProviders.Detailed ) );
      
      systemUnderTest.detailedDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Detailed ) );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void configurationShouldUpdateSimpleDescription(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void configurationShouldUpdateDefaultDescription(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void configurationShouldUpdateDetailedDescription(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void spinnerShouldAcceptInvalidInput(){
      assertThat( 
               systemUnderTest.columnsSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.columnsSpinner().getValue() ) 
      );
   }//End Method
   
   @Test public void eachSpinnerShouldBeEditable(){
      assertThat( systemUnderTest.columnsSpinner().isEditable(), is( true ) );
   }//End Method
   
   @Test public void descriptionTypeButtonsShouldChangeWhenConfigurationChanges(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void shouldUpdateColumnsSpinnerWhenConfigurationChanges(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      configuration.numberOfColumns().set( 100 );
      Assert.assertEquals( 100, systemUnderTest.columnsSpinner().getValue().intValue() );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenColumnsSpinnerChanges(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      systemUnderTest.columnsSpinner().getValueFactory().setValue( 100 );
      Assert.assertEquals( 100, configuration.numberOfColumns().get() );
   }//End Method
   
   @Test public void columnsSpinnerShouldBeBound(){
      IntegerSpinnerValueFactory factory = ( IntegerSpinnerValueFactory )systemUnderTest.columnsSpinner().getValueFactory();
      Assert.assertEquals( 
               DimensionsPanel.MINIMUM_COLUMNS, 
               factory.getMin() 
      );
      Assert.assertEquals( 
               DimensionsPanel.MAXIMUM_COLUMNS, 
               factory.getMax() 
      );
   }//End Method
   
   @Test public void numberOfColumnsShouldUseInitialConfiguration(){
      configuration.numberOfColumns().set( 20 );
      assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.columnsSpinner() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.simpleDescriptionButton() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.defaultDescriptionButton() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailedDescriptionButton() ) );
      
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.columnsSpinnerLabel() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.descriptionTypeLabel() ) );
   }//End Method
   
   @Test public void descriptionTypeSHouldTakeInitialValueFromConfiguration(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      systemUnderTest = new DimensionsPanel( configuration );
      
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void shouldApplyStyleForBuildWallConfiguration(){
      verify( styling ).configureColumnConstraints( systemUnderTest );
   }//End Method
   
   @Test public void radioButtonsShouldChangeTogether(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( true ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
      
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
   }//End Method

   @Test public void shouldDetermineWhetherItHasGivenConfiguration(){
      assertThat( systemUnderTest.hasConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.hasConfiguration( new BuildWallConfigurationImpl() ), is( false ) );
   }//End Method
}//End Constructor
