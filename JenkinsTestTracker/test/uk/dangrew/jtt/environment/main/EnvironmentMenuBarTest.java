/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.main;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javafx.event.ActionEvent;
import uk.dangrew.jtt.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.environment.preferences.WindowPolicy;
import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.event.structure.EventSubscription;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

/**
 * {@link EnvironmentMenuBar} test.
 */
public class EnvironmentMenuBarTest {
   
   private EnvironmentMenuBar systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      systemUnderTest = new EnvironmentMenuBar();
   }//End Method

   @Test public void shouldUseSystemMenuBar() {
      assertThat( systemUnderTest.isUseSystemMenuBar(), is( true ) );
   }//End Method
   
   @Test public void shouldContainMenus(){
      assertThat( systemUnderTest.getMenus(), contains( systemUnderTest.applicationMenu() ) );
   }//End Method
   
   @Test public void shouldProvideApplicationMenu(){
      assertThat( systemUnderTest.applicationMenu().getText(), is( EnvironmentMenuBar.JENKINS_TEST_TRACKER_MENU ) );
   }//End Method
   
   @Test public void shouldProvidePreferencesMenu(){
      assertThat( systemUnderTest.applicationMenu().getItems().contains( systemUnderTest.preferences() ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideAboutMenu(){
      assertThat( systemUnderTest.applicationMenu().getItems().contains( systemUnderTest.about() ), is( true ) );
   }//End Method
   
   @Test public void preferencesShouldOpenPreferencesWindowWithEvent(){
      @SuppressWarnings("unchecked")//mocking 
      EventSubscription< Void, WindowPolicy > registration = mock( EventSubscription.class );
      new PreferencesOpenEvent().register( registration );
      
      @SuppressWarnings("deprecation")//forClass does not compile 
      ArgumentCaptor< Event< Void, WindowPolicy > > eventCaptor = new ArgumentCaptor<>();
      
      systemUnderTest.preferences().getOnAction().handle( new ActionEvent() );
      verify( registration ).notify( eventCaptor.capture() );
      
      assertThat( eventCaptor.getValue().getSource(), is( nullValue() ) );
      assertThat( eventCaptor.getValue().getValue(), is( WindowPolicy.Open ) );
   }//End Method
   
}//End Class
