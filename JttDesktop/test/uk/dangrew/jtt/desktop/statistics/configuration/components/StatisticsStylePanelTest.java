/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.configuration.components;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.configuration.components.StatisticsStylePanel;

public class StatisticsStylePanelTest {

   @Spy private JavaFxStyle styling;
   private StatisticsConfiguration configuration;
   private StatisticsStylePanel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new StatisticsConfiguration();
      systemUnderTest = new StatisticsStylePanel( styling, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideBackgroundColour(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.backgroundColourLabel() ), is( true ) );
      assertThat( systemUnderTest.backgroundColourLabel().getText(), is( StatisticsStylePanel.BACKGROUND_COLOUR_TEXT ) );
      verify( styling ).createBoldLabel( StatisticsStylePanel.BACKGROUND_COLOUR_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.backgroundColour() ), is( true ) );
      verify( styling ).configureColorPicker( systemUnderTest.backgroundColour(), configuration.statisticBackgroundColourProperty() );
   }//End Method
   
   @Test public void shouldProvideTextColour(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.textColourLabel() ), is( true ) );
      assertThat( systemUnderTest.textColourLabel().getText(), is( StatisticsStylePanel.TEXT_COLOUR_TEXT ) );
      verify( styling ).createBoldLabel( StatisticsStylePanel.TEXT_COLOUR_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.textColour() ), is( true ) );
      verify( styling ).configureColorPicker( systemUnderTest.textColour(), configuration.statisticTextColourProperty() );
   }//End Method
   
   @Test public void shouldProvideValueTextFontStyle(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.valueTextFontStyleLabel() ), is( true ) );
      assertThat( systemUnderTest.valueTextFontStyleLabel().getText(), is( StatisticsStylePanel.VALUE_TEXT_FONT_TEXT ) );
      verify( styling ).createBoldLabel( StatisticsStylePanel.VALUE_TEXT_FONT_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.valueTextFontStyle() ), is( true ) );
      assertThat( systemUnderTest.valueTextFontStyle().isBoundTo( configuration.statisticValueTextFontProperty() ), is( true ) );
      assertThat( systemUnderTest.valueTextFontStyle().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldProvideValueTextFontSize(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.valueTextFontSizeLabel() ), is( true ) );
      assertThat( systemUnderTest.valueTextFontSizeLabel().getText(), is( StatisticsStylePanel.VALUE_TEXT_FONT_SIZE_TEXT ) );
      verify( styling ).createBoldLabel( StatisticsStylePanel.VALUE_TEXT_FONT_SIZE_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.valueTextFontSize() ), is( true ) );
      verify( styling ).configureFontSizeSpinner( systemUnderTest.valueTextFontSize(), configuration.statisticValueTextFontProperty() );
   }//End Method
   
   @Test public void shouldProvideDescriptionTextFontStyle(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.descriptionTextFontStyleLabel() ), is( true ) );
      assertThat( systemUnderTest.descriptionTextFontStyleLabel().getText(), is( StatisticsStylePanel.DESCRIPTION_TEXT_FONT_TEXT ) );
      verify( styling ).createBoldLabel( StatisticsStylePanel.DESCRIPTION_TEXT_FONT_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.descriptionTextFontStyle() ), is( true ) );
      assertThat( systemUnderTest.descriptionTextFontStyle().isBoundTo( configuration.statisticDescriptionTextFontProperty() ), is( true ) );
      assertThat( systemUnderTest.descriptionTextFontStyle().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldProvideDescriptionTextFontSize(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.descriptionTextFontSizeLabel() ), is( true ) );
      assertThat( systemUnderTest.descriptionTextFontSizeLabel().getText(), is( StatisticsStylePanel.DESCRIPTION_TEXT_FONT_SIZE_TEXT ) );
      verify( styling ).createBoldLabel( StatisticsStylePanel.DESCRIPTION_TEXT_FONT_SIZE_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.descriptionTextFontSize() ), is( true ) );
      verify( styling ).configureFontSizeSpinner( systemUnderTest.descriptionTextFontSize(), configuration.statisticDescriptionTextFontProperty() );
   }//End Method
   
   @Test public void shouldEvenlySplitColumns(){
      verify( styling ).configureHalfWidthConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldBeAssociatedWithConfiguration(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new StatisticsConfiguration() ), is( false ) );
   }//End Method

}//End Class
