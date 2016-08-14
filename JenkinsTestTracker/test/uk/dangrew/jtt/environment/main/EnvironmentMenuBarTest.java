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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.event.ActionEvent;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreeItems;
import uk.dangrew.jtt.environment.preferences.PreferenceBehaviour;
import uk.dangrew.jtt.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.environment.preferences.WindowPolicy;
import uk.dangrew.jtt.event.structure.EventAssertions;
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
      EventAssertions.assertEventRaised( 
               () -> new PreferencesOpenEvent(), 
               () -> systemUnderTest.preferences().getOnAction().handle( new ActionEvent() ), 
               new PreferenceBehaviour( WindowPolicy.Open, null ) 
      );
   }//End Method
   
   @Test public void aboutShouldOpenPreferencesWindowWithEventAndSelectVersion(){
      EventAssertions.assertEventRaised( 
               () -> new PreferencesOpenEvent(), 
               () -> systemUnderTest.about().getOnAction().handle( new ActionEvent() ), 
               new PreferenceBehaviour( WindowPolicy.Open, ConfigurationTreeItems.SystemVersion ) 
      );
   }//End Method
}//End Class
