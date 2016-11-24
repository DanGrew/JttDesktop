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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static uk.dangrew.jtt.buildwall.configuration.components.themebuilder.ThemeConfigurationPanel.COLUMN_PERCENTAGE;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.TitledPane;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

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
   @Test public void shouldProvideConfigurationForEachStatus( BuildResultStatus status ) {
      assertThat( systemUnderTest.titledPaneFor( status ), is( notNullValue() ) );
      StatusConfigurationPane pane = ( StatusConfigurationPane ) systemUnderTest.titledPaneFor( status ).getContent();
      assertThat( pane.isAssociatedWith( theme ), is( true ) );
      assertThat( pane.isAssociatedWith( status ), is( true ) );
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
   @Test public void shouldProvideTitledPanes( BuildResultStatus status ) {
      TitledPane pane = systemUnderTest.titledPaneFor( status );
      assertThat( pane.isCollapsible(), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( pane ), is( true ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithTheme(){
      assertThat( systemUnderTest.isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new BuildWallThemeImpl( "anything" ) ), is( false ) );
   }//End Method
}//End Class
