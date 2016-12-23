/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.panel;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;

public class StatisticPanelTest {
   
   private static final String DESCRIPTION = "anything describing the stat";
   private static final String INITIAL_VALUE = "really good stat";
   
   @Spy private JavaFxStyle styling;
   private StatisticsConfiguration configuration;
   private StatisticPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new StatisticsConfiguration();
      systemUnderTest = constructSut( styling, configuration );
   }//End Method
   
   /**
    * Method to construct the system under test.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    */
   protected StatisticPanel constructSut( JavaFxStyle styling, StatisticsConfiguration configuration ){
      return new StatisticPanel( styling, configuration, DESCRIPTION, INITIAL_VALUE );
   }//End Method
   
   /**
    * Getter for the description text used by the sut.
    * @return the {@link String}.
    */
   protected String getDescriptionText(){
      return DESCRIPTION;
   }//End Method
   
   /**
    * Getter for the initial value text used by the sut.
    * @return the {@link String}.
    */
   protected String getInitialValue(){
      return INITIAL_VALUE;
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldSetStatisticValueText(){
      assertThat( systemUnderTest.statValue().getText(), is( getInitialValue() ) );
      String value = "something";
      systemUnderTest.setStatisticValue( value );
      assertThat( systemUnderTest.statValue().getText(), is( value ) );
   }//End Method
   
   @Test public void shouldHaveStatisticDescription(){
      assertThat( systemUnderTest.statDescription().getText(), is( getDescriptionText() ) );
   }//End Method
   
   @Test public void shouldUseBackgroundConfigurationColour(){
      assertThat( 
               systemUnderTest.button().getBackground().getFills().get( 0 ).getFill(), 
               is( configuration.statisticBackgroundProperty().get() ) 
      );
      configuration.statisticBackgroundProperty().set( Color.RED );
      assertThat( 
               systemUnderTest.button().getBackground().getFills().get( 0 ).getFill(), 
               is( configuration.statisticBackgroundProperty().get() ) 
      );
   }//End Method
   
   @Test public void shouldUseTextConfigurationColour(){
      assertThat( 
               systemUnderTest.statValue().getTextFill(), 
               is( configuration.statisticTextProperty().get() ) 
      );
      assertThat( 
               systemUnderTest.statDescription().getTextFill(), 
               is( configuration.statisticTextProperty().get() ) 
      );
      configuration.statisticTextProperty().set( Color.RED );
      assertThat( 
               systemUnderTest.statValue().getTextFill(), 
               is( configuration.statisticTextProperty().get() ) 
      );
      assertThat( 
               systemUnderTest.statDescription().getTextFill(), 
               is( configuration.statisticTextProperty().get() ) 
      );
   }//End Method
   
   @Test public void shouldShapeButtonWithRoundedEdges(){
      Shape shape = systemUnderTest.button().getShape();
      assertThat( shape, is( instanceOf( Rectangle.class ) ) );
      
      Rectangle rectangle = ( Rectangle ) shape;
      assertThat( rectangle.getWidth(), is( StatisticPanel.SHAPE_SIDE_LENGTH ) );
      assertThat( rectangle.getHeight(), is( StatisticPanel.SHAPE_SIDE_LENGTH ) );
      assertThat( rectangle.getArcWidth(), is( StatisticPanel.SHAPE_ARC ) );
      assertThat( rectangle.getArcHeight(), is( StatisticPanel.SHAPE_ARC ) );
   }//End Method
   
   @Test public void shouldExpandButtonSizes(){
      assertThat( systemUnderTest.button().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method

   @Test public void shouldContainButtonWithComponents(){
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.button() ) );
      BorderPane wrapper = ( BorderPane ) systemUnderTest.button().getGraphic();
      assertThat( wrapper.getCenter(), is( systemUnderTest.statValue() ) );
      assertThat( wrapper.getBottom(), is( systemUnderTest.statDescription() ) );
   }//End Method
   
   @Test public void shouldAlignElementsAppropriately(){
      assertThat( BorderPane.getAlignment( systemUnderTest.statValue() ), is( Pos.CENTER ) );
      assertThat( BorderPane.getAlignment( systemUnderTest.statDescription() ), is( Pos.CENTER ) );
   }//End Method
   
   @Test public void shouldPadButton(){
      assertThat( systemUnderTest.getPadding().getBottom(), is( StatisticPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( StatisticPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( StatisticPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( StatisticPanel.PADDING ) );
   }//End Method
   
}//End Class
