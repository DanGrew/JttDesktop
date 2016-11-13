/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.progressbar;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.styling.BuildWallStyles;
import uk.dangrew.jtt.styling.BuildWallThemes;
import uk.dangrew.jtt.styling.SystemStyles;
import uk.dangrew.jtt.utility.conversion.ColorConverter;

public class DynamicProgressBarPropertiesTest {
   
   private static final String EMPTY_STYLE = "";
   
   private ColorConverter colorConverterForTesting;
   
   private Node trackLookup;
   private Node barLookup;
   @Mock private ProgressBar progressBar;
   @Spy private ColorConverter colorConverter;
   @Mock private SystemStyles styling;
   private DynamicProgressBarProperties systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      trackLookup = new BorderPane();
      barLookup = new BorderPane();
      
      colorConverterForTesting = new ColorConverter();
      systemUnderTest = new DynamicProgressBarProperties( styling, colorConverter );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException{
      progressBar = new ProgressBar( 0.4 );
      JavaFxInitializer.launchInWindow( () -> new BorderPane( progressBar ) );
      systemUnderTest = new DynamicProgressBarProperties();
      
      Thread.sleep( 2000 );
      systemUnderTest.applyCustomColours( Color.BLACK, Color.AQUAMARINE, progressBar );
      
      Thread.sleep( 2000 );
      systemUnderTest.applyCustomColours( Color.ORANGE, Color.SEASHELL, progressBar );
      
      Thread.sleep( 2000 );
      systemUnderTest.applyCustomColours( Color.INDIGO, Color.PEACHPUFF, progressBar );
      
      Thread.sleep( 1000000 );
   }//End Method

   @Test public void shouldApplyStandardSystemColours() {
      systemUnderTest.applyStandardColourFor( BuildWallStyles.ProgressBarFailed, progressBar );
      verify( styling ).applyStyle( BuildWallStyles.ProgressBarFailed, BuildWallThemes.Standard, progressBar );
   }//End Method
   
   @Test public void shouldApplyFrozenSystemColours() {
      systemUnderTest.applyFrozenColourFor( BuildWallStyles.ProgressBarFailed, progressBar );
      verify( styling ).applyStyle( BuildWallStyles.ProgressBarFailed, BuildWallThemes.Frozen, progressBar );
   }//End Method
   
   @Test public void shouldApplyCustomBarAndTrackColour(){
      when( progressBar.lookup( DynamicProgressBarProperties.BAR_LOOKUP ) ).thenReturn( barLookup );
      when( progressBar.lookup( DynamicProgressBarProperties.TRACK_LOOKUP ) ).thenReturn( trackLookup );
      
      systemUnderTest.applyCustomColours( Color.RED, Color.GREEN, progressBar );
      assertThat( barLookup.getStyle(), is( DynamicProgressBarProperties.formatBackgroundColourProperty( colorConverterForTesting.colorToHex( Color.RED ) ) ) );
      assertThat( trackLookup.getStyle(), is( DynamicProgressBarProperties.formatBackgroundColourProperty( colorConverterForTesting.colorToHex( Color.GREEN ) ) ) );
   }//End Method
   
   @Test public void shouldIdentifyMissingBarLookupNodeAndHandle(){
      when( progressBar.lookup( DynamicProgressBarProperties.BAR_LOOKUP ) ).thenReturn( null );
      when( progressBar.lookup( DynamicProgressBarProperties.TRACK_LOOKUP ) ).thenReturn( trackLookup );
      
      systemUnderTest.applyCustomColours( Color.RED, Color.GREEN, progressBar );
      assertThat( barLookup.getStyle(), is( EMPTY_STYLE ) );
      assertThat( trackLookup.getStyle(), is( EMPTY_STYLE ) );
   }//End Method
   
   @Test public void shouldIdentifyMissingTrackLookupNodeAndHandle(){
      when( progressBar.lookup( DynamicProgressBarProperties.BAR_LOOKUP ) ).thenReturn( barLookup );
      when( progressBar.lookup( DynamicProgressBarProperties.TRACK_LOOKUP ) ).thenReturn( null );
      
      systemUnderTest.applyCustomColours( Color.RED, Color.GREEN, progressBar );
      assertThat( barLookup.getStyle(), is( EMPTY_STYLE ) );
      assertThat( trackLookup.getStyle(), is( EMPTY_STYLE ) );
   }//End Method
   
   @Test public void shouldFormatColourStringIntoFxProperty(){
      assertThat( 
               DynamicProgressBarProperties.formatBackgroundColourProperty( "#120A8F" ),
               is( "-fx-background-color: #120A8F;" )
      );
   }//End Method

}//End Class
