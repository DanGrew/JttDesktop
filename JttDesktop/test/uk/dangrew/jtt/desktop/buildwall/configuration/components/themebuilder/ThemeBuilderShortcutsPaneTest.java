/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class ThemeBuilderShortcutsPaneTest {

   @Spy private JavaFxStyle styling;
   private ThemeBuilderShortcutProperties shortcuts;
   private ThemeBuilderShortcutsPane systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      shortcuts = new ThemeBuilderShortcutProperties();
      systemUnderTest = new ThemeBuilderShortcutsPane( styling, shortcuts );
   }//End Method

   @Test public void shouldUseHalfWidthConstraints() {
      verify( styling ).configureHalfWidthConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldContainGraphicalElements() {
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.label() ), is( true ) );
      assertThat( systemUnderTest.label().getText(), is( ThemeBuilderShortcutsPane.LABEL_TEXT ) );
      verify( styling ).createBoldLabel( ThemeBuilderShortcutsPane.LABEL_TEXT );
      
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.picker() ), is( true ) );
      verify( styling ).configureColorPicker( systemUnderTest.picker(), ( Color )null );
   }//End Method
   
   @Test public void shouldApplyBasicPadding() {
      verify( styling ).applyBasicPadding( systemUnderTest );
   }//End Method
   
   @Test public void shouldPickerShouldUpdateShortcut() {
      systemUnderTest.picker().setValue( Color.BLUE );
      assertThat( shortcuts.shortcutColorProperty().get(), is( Color.BLUE ) );
   }//End Method
   
   @Test public void shortcutShouldUpdatePicker() {
      shortcuts.shortcutColorProperty().set( Color.BLUE );
      assertThat( systemUnderTest.picker().getValue(), is( Color.BLUE ) );
   }//End Method
   
   @Test public void shouldDetachFromSystem() {
      systemUnderTest.detachFromSystem();
      shortcuts.shortcutColorProperty().set( Color.BLUE );
      assertThat( systemUnderTest.picker().getValue(), is( not( Color.BLUE ) ) );
      
      shortcuts.shortcutColorProperty().set( Color.RED );
      assertThat( systemUnderTest.picker().getValue(), is( not( Color.RED ) ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithShortcuts(){
      assertThat( systemUnderTest.isAssociatedWith( shortcuts ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new ThemeBuilderShortcutProperties() ), is( false ) );
   }//End Method

}//End Class
