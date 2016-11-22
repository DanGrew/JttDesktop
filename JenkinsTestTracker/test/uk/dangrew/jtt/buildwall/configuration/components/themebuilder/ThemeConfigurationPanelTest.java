/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static uk.dangrew.jtt.buildwall.configuration.components.themebuilder.ThemeConfigurationPanel.BAR_COLOUR_STRING;
import static uk.dangrew.jtt.buildwall.configuration.components.themebuilder.ThemeConfigurationPanel.COLUMN_PERCENTAGE;
import static uk.dangrew.jtt.buildwall.configuration.components.themebuilder.ThemeConfigurationPanel.TRACK_COLOUR_STRING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.utility.TestCommon;

@RunWith( JUnitParamsRunner.class )
public class ThemeConfigurationPanelTest {

   @Spy private JavaFxStyle styling;
   private BuildWallTheme theme;
   private ThemeConfigurationPanel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      theme = new BuildWallThemeImpl( "Test" );
      systemUnderTest = new ThemeConfigurationPanel( styling, theme );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullTheme() {
      new ThemeConfigurationPanel( null );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldUpdateEachBarColourPicker( BuildResultStatus status ) {
      assertThat( systemUnderTest.barColorPickerFor( status ).getValue(), is( nullValue() ) );
      theme.barColoursMap().put( status, Color.BISQUE );
      assertThat( systemUnderTest.barColorPickerFor( status ).getValue(), is( Color.BISQUE ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldUpdateEachTrackColourPicker( BuildResultStatus status ) {
      assertThat( systemUnderTest.trackColorPickerFor( status ).getValue(), is( nullValue() ) );
      theme.trackColoursMap().put( status, Color.BISQUE );
      assertThat( systemUnderTest.trackColorPickerFor( status ).getValue(), is( Color.BISQUE ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldLeavePreviousBarColourWhenCleared( BuildResultStatus status ) {
      theme.barColoursMap().put( status, Color.BISQUE );
      assertThat( systemUnderTest.barColorPickerFor( status ).getValue(), is( Color.BISQUE ) );
      theme.barColoursMap().put( status, null );
      assertThat( systemUnderTest.barColorPickerFor( status ).getValue(), is( Color.BISQUE ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldLeavePreviousTrackColourWhenCleared( BuildResultStatus status ) {
      theme.trackColoursMap().put( status, Color.BISQUE );
      assertThat( systemUnderTest.trackColorPickerFor( status ).getValue(), is( Color.BISQUE ) );
      theme.trackColoursMap().put( status, null );
      assertThat( systemUnderTest.trackColorPickerFor( status ).getValue(), is( Color.BISQUE ) );
   }//End Method

   @Test public void shouldProvidePaddingForStyling() {
      assertThat( systemUnderTest.getPadding().getBottom(), is( ThemeConfigurationPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( ThemeConfigurationPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( ThemeConfigurationPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( ThemeConfigurationPanel.PADDING ) );
   }//End Method
   
   @Test public void shouldSplitGridIntoColumns() {
      verify( styling ).configureConstraintsForColumnPercentages( systemUnderTest, COLUMN_PERCENTAGE );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideConfiguredBarColourPickers( BuildResultStatus status ) {
      ColorPicker picker = systemUnderTest.barColorPickerFor( status );
      GridPane wrapper = ( GridPane )systemUnderTest.titledPaneFor( status ).getContent();
      assertThat( wrapper.getChildren().contains( picker ), is( true ) );
      verify( styling ).configureColorPicker( Mockito.eq( picker ), Mockito.any() );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideConfiguredTrackColourPickers( BuildResultStatus status ) {
      ColorPicker picker = systemUnderTest.trackColorPickerFor( status );
      GridPane wrapper = ( GridPane )systemUnderTest.titledPaneFor( status ).getContent();
      assertThat( wrapper.getChildren().contains( picker ), is( true ) );
      verify( styling ).configureColorPicker( Mockito.eq( picker ), Mockito.any() );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideBarLabels( BuildResultStatus status ) {
      Label label = systemUnderTest.barLabelFor( status );
      GridPane wrapper = ( GridPane )systemUnderTest.titledPaneFor( status ).getContent();
      assertThat( wrapper.getChildren().contains( label ), is( true ) );
      assertThat( label.getText(), is( BAR_COLOUR_STRING ) );
      TestCommon.assertThatFontIsBold( label.getFont() );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideTrackLabels( BuildResultStatus status ) {
      Label label = systemUnderTest.trackLabelFor( status );
      GridPane wrapper = ( GridPane )systemUnderTest.titledPaneFor( status ).getContent();
      assertThat( wrapper.getChildren().contains( label ), is( true ) );
      assertThat( label.getText(), is( TRACK_COLOUR_STRING ) );
      TestCommon.assertThatFontIsBold( label.getFont() );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldUpdateThemeWhenBarPickersChange( BuildResultStatus status ) {
      assertThat( theme.barColoursMap().get( status ), is( nullValue() ) );
      systemUnderTest.barColorPickerFor( status ).setValue( Color.BLANCHEDALMOND );
      assertThat( theme.barColoursMap().get( status ), is( Color.BLANCHEDALMOND ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldUpdateThemeWhenTrackPickersChange( BuildResultStatus status ) {
      assertThat( theme.trackColoursMap().get( status ), is( nullValue() ) );
      systemUnderTest.trackColorPickerFor( status ).setValue( Color.BLANCHEDALMOND );
      assertThat( theme.trackColoursMap().get( status ), is( Color.BLANCHEDALMOND ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideTitledPanes( BuildResultStatus status ) {
      TitledPane pane = systemUnderTest.titledPaneFor( status );
      assertThat( pane.isCollapsible(), is( false ) );
      assertThat( systemUnderTest.getChildren().contains( pane ), is( true ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideStyledWrapperPanes( BuildResultStatus status ) {
      TitledPane pane = systemUnderTest.titledPaneFor( status );
      GridPane wrapper = ( GridPane ) pane.getContent();
      verify( styling ).configureConstraintsForColumnPercentages( 
               wrapper, ThemeConfigurationPanel.HALF_WIDTH_COLUMN, ThemeConfigurationPanel.HALF_WIDTH_COLUMN 
      );
   }//End Method
   
   @Test public void shouldBeAssociatedWithTheme(){
      assertThat( systemUnderTest.isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new BuildWallThemeImpl( "anything" ) ), is( false ) );
   }//End Method
}//End Class
