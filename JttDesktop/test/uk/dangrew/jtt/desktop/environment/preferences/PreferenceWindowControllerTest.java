/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.preferences;


import javafx.scene.Parent;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.utility.javafx.TestableParent;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * {@link PreferenceWindowController} test.
 */
public class PreferenceWindowControllerTest {
   
   private Parent pane;
   private PreferenceWindowController systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      pane = new TestableParent();
      systemUnderTest = new PreferenceWindowController();
      systemUnderTest.associateWithConfiguration( pane );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowShowIfNotAssociated(){
      systemUnderTest = new PreferenceWindowController();
      systemUnderTest.showConfigurationWindow();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowHideIfNotAssociated(){
      systemUnderTest = new PreferenceWindowController();
      systemUnderTest.hideConfigurationWindow();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowIsShowingCheckIfNotAssociated(){
      systemUnderTest = new PreferenceWindowController();
      systemUnderTest.isConfigurationWindowShowing();
   }//End Method
   
   @Test public void shouldHaveInitialisedStage() {
      assertThat( systemUnderTest.stage(), is( notNullValue() ) );
   }//End Method
   
   @Test public void stageShouldHaveConfigurationWindowWithinScene(){
      assertThat( systemUnderTest.stage().getScene(), is( notNullValue() ) );
      assertThat( systemUnderTest.stage().getScene().getRoot(), is( notNullValue() ) );
      assertThat( systemUnderTest.stage().getScene().getRoot(), is( pane ) );
   }//End Method
   
   @Test public void stageShouldBeHiddenInitially(){
      assertThat( systemUnderTest.stage().isShowing(), is( false ) );
   }//End Method
   
   @Test public void stageShouldShowWhenTold(){
      assertThat( systemUnderTest.stage().isShowing(), is( false ) );
      systemUnderTest.showConfigurationWindow();
      JavaFxThreading.runAndWait( () -> {} );
      assertThat( systemUnderTest.stage().isShowing(), is( true ) );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( true ) );
   }//End Method
   
   @Test public void stageShouldHideWhenTold(){
      stageShouldShowWhenTold();
      assertThat( systemUnderTest.stage().isShowing(), is( true ) );
      systemUnderTest.hideConfigurationWindow();
      JavaFxThreading.runAndWait( () -> {} );
      assertThat( systemUnderTest.stage().isShowing(), is( false ) );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( false ) );
   }//End Method

   @Test public void shouldSizeStageAndMakeNotFullScreen(){
      assertThat( systemUnderTest.stage().isFullScreen(), is( false ) );
      assertThat( systemUnderTest.stage().widthProperty().get(), is( PreferenceWindowController.WIDTH ) );
      assertThat( systemUnderTest.stage().heightProperty().get(), is( PreferenceWindowController.HEIGHT ) );
   }//End Method
   
   @Test public void shouldHaveTitle(){
      assertThat( systemUnderTest.stage().getTitle(), is( PreferenceWindowController.CONFIGURATION_WINDOW_TITLE ) );
   }//End Method
}//End Class
