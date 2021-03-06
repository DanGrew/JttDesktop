/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.main;

import javafx.event.ActionEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.dangrew.jtt.desktop.configuration.tree.ConfigurationTreeItems;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceBehaviour;
import uk.dangrew.jtt.desktop.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.desktop.environment.preferences.WindowPolicy;
import uk.dangrew.jtt.desktop.utility.system.OperatingSystem;
import uk.dangrew.kode.event.structure.EventAssertions;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * {@link EnvironmentMenuBar} test.
 */
public class EnvironmentMenuBarTest {
   
   @Mock private OperatingSystem os;
   private EnvironmentMenuBar systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      when( os.isMac() ).thenReturn( true );
      systemUnderTest = new EnvironmentMenuBar( os );
   }//End Method

   @Test public void shouldUseSystemMenuBarIfMac() {
      assertThat( systemUnderTest.isUseSystemMenuBar(), is( true ) );
   }//End Method
   
   @Test public void shouldNotUseSystemMenuBarIfNoyMac() {
      when( os.isMac() ).thenReturn( false );
      systemUnderTest = new EnvironmentMenuBar( os );
      assertThat( systemUnderTest.isUseSystemMenuBar(), is( false ) );
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
