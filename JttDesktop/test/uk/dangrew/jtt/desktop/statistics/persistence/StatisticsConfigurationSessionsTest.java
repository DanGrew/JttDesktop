/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationSessions.FILE_NAME;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationSessions.FOLDER_NAME;

import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.main.JenkinsTestTracker;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.utility.TestableFonts;
import uk.dangrew.jtt.desktop.utility.conversion.ColorConverter;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;

public class StatisticsConfigurationSessionsTest {

   private static final Color TEST_COLOUR = Color.RED;
   private static final String TEST_COLOUR_HEX = new ColorConverter().colorToHex( TEST_COLOUR );
   
   @Mock private JenkinsDatabase database;
   @Mock private JarJsonPersistingProtocol protocol;
   @Captor private ArgumentCaptor< JSONObject > objectCaptor;
   
   private StatisticsConfigurationSessions systemUnderTest;
   private StatisticsConfiguration configuration;
   
   private CountDownLatch latch;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      configuration = new StatisticsConfiguration();
      
      systemUnderTest = new StatisticsConfigurationSessions( configuration, protocol );
      
      latch = new CountDownLatch( 1 );
      when( protocol.writeToLocation( Mockito.any() ) ).thenAnswer( invocation -> { latch.countDown(); return true; } );
   }//End Method
   
   @After public void tearDown(){
      systemUnderTest.shutdownSessions();
   }//End Method
   
   @Test public void publicConstructorShouldDefineCorrectFileLocations(){
      systemUnderTest = new StatisticsConfigurationSessions( configuration );
      assertThat( 
               systemUnderTest.configurationFileLocation().getLocation(), 
               is( new JarJsonPersistingProtocol( FOLDER_NAME, FILE_NAME, JenkinsTestTracker.class ).getLocation() ) 
      );
   }//End Method
   
   @Test public void shouldOnConstruction(){
      verify( protocol ).readFromLocation();
   }//End Method
   
   @Test public void shutdownShouldStopWriting(){
      systemUnderTest.shutdownSessions();
      
      configuration.statisticBackgroundColourProperty().set( Color.ANTIQUEWHITE );
      
      verify( protocol, never() ).writeToLocation( Mockito.any() );
   }//End Method
   
   @Test public void shouldTriggerWriteWhenBackgroundColourChanged() throws InterruptedException {
      assertResponseToBackgroundColourChange( configuration, protocol );
   }//End Method
   
   private void assertResponseToBackgroundColourChange(
            StatisticsConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse
   ) throws InterruptedException {
      configuration.statisticBackgroundColourProperty().set( TEST_COLOUR );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"BackgroundColour\":\"" + TEST_COLOUR_HEX ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void shouldTriggerWriteWhenTextColourChanged() throws InterruptedException {
      assertResponseToTextColourChange( configuration, protocol );
   }//End Method
   
   private void assertResponseToTextColourChange(
            StatisticsConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse
   ) throws InterruptedException {
      configuration.statisticTextColourProperty().set( TEST_COLOUR );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"TextColour\":\"" + TEST_COLOUR_HEX ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenValueTextFontChanged() throws InterruptedException {
      assertResponseToJobNameFontChange( configuration, protocol );
   }//End Method
   
   private void assertResponseToJobNameFontChange(
            StatisticsConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse
   ) throws InterruptedException {
      configuration.statisticValueTextFontProperty().set( Font.font( TestableFonts.commonFont(), 14 ) );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"ValueTextFamily\":\"" + TestableFonts.commonFont() + "\"" ) );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"ValueTextSize\":14" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenDescriptionTextFontChanged() throws InterruptedException {
      assertResponseToDescriptionFontChange( configuration, protocol );
   }//End Method
   
   private void assertResponseToDescriptionFontChange(
            StatisticsConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse
   ) throws InterruptedException {
      configuration.statisticDescriptionTextFontProperty().set( Font.font( TestableFonts.commonFont(), 14 ) );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"DescriptionTextFamily\":\"" + TestableFonts.commonFont() + "\"" ) );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"DescriptionTextSize\":14" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void shouldRespondToExclusionsChange() throws InterruptedException{
      configuration.excludedJobs().add( new JenkinsJobImpl( "anything" ) );
      latch.await();
      
      verify( protocol ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"Exclusions\":[\"anything" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocol );
   }//End Method
   
   /**
    * Method to verify that read always happens before write on the given {@link JarJsonPersistingProtocol}.
    * @param protocolToUse the {@link JarJsonPersistingProtocol} to verify.
    */
   private void verifyReadAlwaysHappensBeforeWrite( JarJsonPersistingProtocol protocolToUse ){
      InOrder order = inOrder( protocolToUse );
      order.verify( protocolToUse ).readFromLocation();
      order.verify( protocolToUse ).writeToLocation( Mockito.any() );
   }//End Method
   
   @Test public void shouldUseCorrectConfiguration(){
      assertThat( systemUnderTest.uses( configuration ), is( true ) );
      assertThat( systemUnderTest.uses( new StatisticsConfiguration() ), is( false ) );
   }//End Method
   
}//End Class
