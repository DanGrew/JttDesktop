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

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

public class StatusConfigurationPaneTest {

   @Spy private JavaFxStyle styling; 
   private BuildWallTheme theme;
   private BuildResultStatus status;
   private StatusConfigurationPane systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      status = BuildResultStatus.SUCCESS;
      theme = new BuildWallThemeImpl( "Test" );
      theme.barColoursMap().put( status, Color.BLACK );
      theme.trackColoursMap().put( status, Color.RED );
      
      systemUnderTest = new StatusConfigurationPane( styling, theme, status );
   }//End Method
   
   @Test public void shouldUsePadding(){
      assertThat( systemUnderTest.getPadding().getBottom(), is( StatusConfigurationPane.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( StatusConfigurationPane.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( StatusConfigurationPane.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( StatusConfigurationPane.PADDING ) );
   }//End Method

   @Test public void shouldUseHalfWidthColumns() {
      verify( styling ).configureHalfWidthConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldHaveInitialBarColour(){
      assertThat( systemUnderTest.barPicker().getValue(), is( Color.BLACK ) );
   }//End Method
   
   @Test public void shouldHaveInitialTrackColour(){
      assertThat( systemUnderTest.trackPicker().getValue(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldProvideBarElements() {
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.barLabel() ), is( true ) );
      assertThat( systemUnderTest.barLabel().getText(), is( StatusConfigurationPane.BAR_COLOUR_STRING ) );
      verify( styling ).createBoldLabel( StatusConfigurationPane.BAR_COLOUR_STRING );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.barPicker() ), is( true ) );
      verify( styling ).configureColorPicker( systemUnderTest.barPicker(), theme.barColoursMap().get( status ) );
   }//End Method
   
   @Test public void shouldProvideTrackElements() {
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.trackLabel() ), is( true ) );
      assertThat( systemUnderTest.trackLabel().getText(), is( StatusConfigurationPane.TRACK_COLOUR_STRING ) );
      verify( styling ).createBoldLabel( StatusConfigurationPane.TRACK_COLOUR_STRING );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.trackPicker() ), is( true ) );
      verify( styling ).configureColorPicker( systemUnderTest.trackPicker(), theme.trackColoursMap().get( status ) );
   }//End Method
   
   @Test public void shouldUpdateBarElements() {
      theme.barColoursMap().put( status, Color.BLANCHEDALMOND );
      assertThat( systemUnderTest.barPicker().getValue(), is( Color.BLANCHEDALMOND ) );
      
      theme.barColoursMap().put( status, null );
      assertThat( systemUnderTest.barPicker().getValue(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldUpdateTrackElements() {
      theme.trackColoursMap().put( status, Color.BLANCHEDALMOND );
      assertThat( systemUnderTest.trackPicker().getValue(), is( Color.BLANCHEDALMOND ) );
      
      theme.trackColoursMap().put( status, null );
      assertThat( systemUnderTest.trackPicker().getValue(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldUpdateThemeFromBarElements() {
      systemUnderTest.barPicker().setValue( Color.BLANCHEDALMOND );
      assertThat( theme.barColoursMap().get( status ), is( Color.BLANCHEDALMOND ) );
   }//End Method
   
   @Test public void shouldUpdateThemeFromTrackElements() {
      systemUnderTest.trackPicker().setValue( Color.BLANCHEDALMOND );
      assertThat( theme.trackColoursMap().get( status ), is( Color.BLANCHEDALMOND ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithTheme(){
      assertThat( systemUnderTest.isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new BuildWallThemeImpl( "anything" ) ), is( false ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithStatus(){
      assertThat( systemUnderTest.isAssociatedWith( status ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( BuildResultStatus.FAILURE ), is( false ) );
   }//End Method

}//End Class
