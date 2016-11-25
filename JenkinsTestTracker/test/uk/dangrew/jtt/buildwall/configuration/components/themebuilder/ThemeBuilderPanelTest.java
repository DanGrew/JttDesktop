/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.styling.SystemStyling;

public class ThemeBuilderPanelTest {

   private BuildWallTheme theme;
   @Spy private JavaFxStyle styling;
   private ThemeBuilderShortcutProperties shortcuts;
   private ThemeBuilderPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      SystemStyling.initialise();
      MockitoAnnotations.initMocks( this );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      
      theme = new BuildWallThemeImpl( "Test" );
      shortcuts = new ThemeBuilderShortcutProperties();
      systemUnderTest = new ThemeBuilderPanel( styling, shortcuts, theme );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldConfigureFullWidthConstraints(){
      verify( styling ).configureFullWidthConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldHaveDisjointWallConnectedToTheme(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.wall() ), is( true ) );
      assertThat( systemUnderTest.wall().isAssociatedWith( theme ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveConfigurationPanelConnectedToTheme(){
      assertThat( systemUnderTest.scroller().getContent(), is( systemUnderTest.configuration() ) );
      assertThat( systemUnderTest.configuration().isAssociatedWith( theme ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveWrappedScrollerFittingToWidth(){
      assertThat( systemUnderTest.scrollerSplit().getCenter(), is( systemUnderTest.scroller() ) );
      assertThat( systemUnderTest.scroller().isFitToWidth(), is( true ) );
   }//End Method
   
   @Test public void shouldHaveFixedNonScrollerShortcuts(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.scrollerSplit() ), is( true ) );
      assertThat( systemUnderTest.scrollerSplit().getTop(), is( systemUnderTest.shortcutsPane() ) );
      assertThat( systemUnderTest.shortcutsPane().getContent(), is( instanceOf( ThemeBuilderShortcutsPane.class ) ) );
      
      ThemeBuilderShortcutsPane shortcutsPane = ( ThemeBuilderShortcutsPane ) systemUnderTest.shortcutsPane().getContent();
      assertThat( shortcutsPane.isAssociatedWith( shortcuts ), is( true ) );
      assertThat( systemUnderTest.shortcutsPane().getText(), is( ThemeBuilderPanel.SHORTCUTS_TITLE ) );
      
      verify( styling ).applyBasicPadding( systemUnderTest.shortcutsPane() );
   }//End Method

}//End Class
