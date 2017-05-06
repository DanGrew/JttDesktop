/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static uk.dangrew.jtt.model.utility.TestCommon.precision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.DualPropertiesPanel;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

/**
 * {@link DualPropertiesPanel} test.
 */
public class DualPropertiesPanelTest {
   
   private DualWallConfiguration configuration;
   @Spy private JavaFxStyle styling;
   private DualPropertiesPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new DualWallConfigurationImpl();
      systemUnderTest = new DualPropertiesPanel( configuration, styling );
   }//End Method

   @Test public void shouldUseBoldLabels(){
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.positionLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.orientationLabel().getFont().getStyle() ) );
   }//End Method
   
   @Test public void verticalOrientationShouldUpdateConfiguration(){
      systemUnderTest.horizontalButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.dividerOrientationProperty().get(), not( Orientation.VERTICAL ) );
      
      systemUnderTest.verticalButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.dividerOrientationProperty().get(), is( Orientation.VERTICAL ) );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void horizontalOrientationShouldUpdateConfiguration(){
      systemUnderTest.verticalButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.dividerOrientationProperty().get(), not( Orientation.HORIZONTAL ) );
      
      systemUnderTest.horizontalButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.dividerOrientationProperty().get(), is( Orientation.HORIZONTAL ) );
      assertThat( systemUnderTest.verticalButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void configurationShouldUpdateVertical(){
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      assertThat( systemUnderTest.verticalButton().isSelected(), is( false ) );
      
      configuration.dividerOrientationProperty().set( Orientation.VERTICAL );
      assertThat( systemUnderTest.verticalButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void configurationShouldUpdateHorizontal(){
      configuration.dividerOrientationProperty().set( Orientation.VERTICAL );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( false ) );
      
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void spinnerShouldAcceptInvalidInput(){
      assertThat( 
               systemUnderTest.positionSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.positionSpinner().getValue() ) 
      );
   }//End Method
   
   @Test public void eachSpinnerShouldBeEditable(){
      assertThat( systemUnderTest.positionSpinner().isEditable(), is( true ) );
   }//End Method
   
   @Test public void orientationButtonsShouldChangeWhenConfigurationChanges(){
      configuration.dividerOrientationProperty().set( Orientation.VERTICAL );
      assertThat( systemUnderTest.verticalButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void shouldUpdatePositionSpinnerWhenConfigurationChanges(){
      assertThat( 
               configuration.dividerPositionProperty().get(), 
               is( closeTo( systemUnderTest.positionSpinner().getValue().doubleValue(), precision() ) ) 
      );
      configuration.dividerPositionProperty().set( 0.78 );
      assertThat( systemUnderTest.positionSpinner().getValue().doubleValue(), is( closeTo( 0.78, precision() ) ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenPositionSpinnerChanges(){
      assertThat( 
               configuration.dividerPositionProperty().get(), 
               is( closeTo( systemUnderTest.positionSpinner().getValue().doubleValue(), precision() ) ) 
      );
      systemUnderTest.positionSpinner().getValueFactory().setValue( 0.43 );
      assertThat( 
               configuration.dividerPositionProperty().get(),
               is( closeTo( 0.43, precision() ) ) 
      );
   }//End Method
   
   @Test public void positionSpinnerShouldBeBound(){
      DoubleSpinnerValueFactory factory = ( DoubleSpinnerValueFactory )systemUnderTest.positionSpinner().getValueFactory();
      assertThat( 
               factory.getMin() ,
               is( closeTo( DualPropertiesPanel.MINIMUM_POSITION, precision() ) ) 
      );
      assertThat( 
               factory.getMax() ,
               is( closeTo( DualPropertiesPanel.MAXIMUM_POSITION, precision() ) ) 
      );
      assertThat( 
               factory.getAmountToStepBy() ,
               is( closeTo( DualPropertiesPanel.POSITION_INTERVAL, precision() ) ) 
      );
   }//End Method
   
   @Test public void positionShouldUseInitialConfiguration(){
      configuration.dividerPositionProperty().set( 0.22 );
      assertThat( 
               configuration.dividerPositionProperty().get(), 
               is( closeTo( systemUnderTest.positionSpinner().getValue().doubleValue(), precision() ) ) 
      );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.positionSpinner() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.verticalButton() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.horizontalButton() ) );
      
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.positionLabel() ) );
      assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.orientationLabel() ) );
   }//End Method
   
   @Test public void orientationShouldTakeInitialValueFromConfiguration(){
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      systemUnderTest = new DualPropertiesPanel( configuration );
      
      assertThat( systemUnderTest.verticalButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void shouldApplyStyleForBuildWallConfiguration(){
      verify( styling ).configureColumnConstraints( systemUnderTest );
   }//End Method
   
   @Test public void radioButtonsShouldChangeTogether(){
      configuration.dividerOrientationProperty().set( Orientation.VERTICAL );
      
      assertThat( systemUnderTest.verticalButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( false ) );
      
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      
      assertThat( systemUnderTest.verticalButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.horizontalButton().isSelected(), is( true ) );
   }//End Method

   @Test public void shouldDetermineWhetherItHasGivenConfiguration(){
      assertThat( systemUnderTest.hasConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.hasConfiguration( new DualWallConfigurationImpl() ), is( false ) );
   }//End Method
}//End Constructor
