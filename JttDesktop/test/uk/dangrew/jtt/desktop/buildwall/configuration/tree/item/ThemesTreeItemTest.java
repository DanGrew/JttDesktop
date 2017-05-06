/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.tree.item;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder.ThemeBuilderPanel;
import uk.dangrew.jtt.desktop.buildwall.configuration.tree.item.ThemesTreeItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;

public class ThemesTreeItemTest {

   @Mock private PreferenceController controller;
   @Captor private ArgumentCaptor< Node > contentTitleCaptor;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   
   private ThemesTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      systemUnderTest = new ThemesTreeItem( controller );
   }//End Method
   
   @Test public void shouldHandleClickByInstructingController() {
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( contentTitleCaptor.capture(), contentCaptor.capture() );
      
      assertThat( contentTitleCaptor.getValue(), is( instanceOf( SimpleConfigurationTitle.class ) ) );
      SimpleConfigurationTitle title = ( SimpleConfigurationTitle ) contentTitleCaptor.getValue();
      assertThat( title.getTitle(), is( ThemesTreeItem.TITLE ) );
      assertThat( title.getDescription(), is( ThemesTreeItem.DESCRIPTION ) );
      
      assertThat( contentCaptor.getValue(), is( instanceOf( ThemeBuilderPanel.class ) ) );
   }//End Method
   
   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( ThemesTreeItem.NAME ) );
   }//End Method
   
   @Test public void shouldNotBeAssociatedWithAnything(){
      assertThat( systemUnderTest.isAssociatedWith( new Object() ), is( false ) );
   }//End Method

}//End Class
