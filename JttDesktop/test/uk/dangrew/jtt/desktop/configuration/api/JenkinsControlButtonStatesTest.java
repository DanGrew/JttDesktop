/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.Button;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class JenkinsControlButtonStatesTest {

   @Mock private JenkinsConnectionControls controls;
   private Button first;
   private Button second;
   private Button third;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      first = new Button();
      second = new Button();
      third = new Button();
   }//End Method
   
   @Test public void shouldConfigureNoSelection() {
      JenkinsControlButtonStates.NoSelection.configure( first, second, third, controls );
      
      assertThat( first.getText(), is( JenkinsConnectionControls.NEW_TEXT ) );
      assertThat( second.getText(), is( JenkinsConnectionControls.SAVE_TEXT ) );
      assertThat( third.getText(), is( JenkinsConnectionControls.CANCEL_TEXT ) );
      
      assertThat( first.isDisabled(), is( false ) );
      assertThat( second.isDisabled(), is( true ) );
      assertThat( third.isDisabled(), is( true ) );
      
      first.fire();
      verify( controls ).createNew();
      
      assertThat( second.getOnAction(), is( nullValue() ) );
      assertThat( third.getOnAction(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldConfigureEnteringNew() {
      JenkinsControlButtonStates.EnteringNew.configure( first, second, third, controls );
      
      assertThat( first.getText(), is( JenkinsConnectionControls.NEW_TEXT ) );
      assertThat( second.getText(), is( JenkinsConnectionControls.SAVE_TEXT ) );
      assertThat( third.getText(), is( JenkinsConnectionControls.CANCEL_TEXT ) );
      
      assertThat( first.isDisabled(), is( true ) );
      assertThat( second.isDisabled(), is( false ) );
      assertThat( third.isDisabled(), is( false ) );
      
      assertThat( first.getOnAction(), is( nullValue() ) );
      
      second.fire();
      verify( controls ).saveConnection();
      
      third.fire();
      verify( controls ).cancelNew();
   }//End Method

   @Test public void shouldConfigureSelectedDisconnected() {
      JenkinsControlButtonStates.SelectedDisconnected.configure( first, second, third, controls );
      
      assertThat( first.getText(), is( JenkinsConnectionControls.NEW_TEXT ) );
      assertThat( second.getText(), is( JenkinsConnectionControls.CONNECTED_TEXT ) );
      assertThat( third.getText(), is( JenkinsConnectionControls.FORGET_TEXT ) );
      
      assertThat( first.isDisabled(), is( false ) );
      assertThat( second.isDisabled(), is( false ) );
      assertThat( third.isDisabled(), is( false ) );
      
      first.fire();
      verify( controls, Mockito.atLeastOnce() ).createNew();
      
      second.fire();
      verify( controls ).connect();
      
      third.fire();
      verify( controls ).forget();
   }//End Method
   
   @Test public void shouldConfigureSelectedConnected() {
      JenkinsControlButtonStates.SelectedConnected.configure( first, second, third, controls );
      
      assertThat( first.getText(), is( JenkinsConnectionControls.NEW_TEXT ) );
      assertThat( second.getText(), is( JenkinsConnectionControls.DISCONNECTED_TEXT ) );
      assertThat( third.getText(), is( JenkinsConnectionControls.FORGET_TEXT ) );
      
      assertThat( first.isDisabled(), is( false ) );
      assertThat( second.isDisabled(), is( false ) );
      assertThat( third.isDisabled(), is( true ) );
      
      first.fire();
      verify( controls, Mockito.atLeastOnce() ).createNew();
      
      second.fire();
      verify( controls ).disconnect();
      
      assertThat( third.getOnAction(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldPreserveStateAfterConsecutiveCalls(){
      shouldConfigureEnteringNew();
      shouldConfigureNoSelection();
      shouldConfigureSelectedConnected();
      shouldConfigureSelectedDisconnected();
   }//End Method
   
}//End Class
