/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions.FOLDER_NAME;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions.LEFT_BUILD_WALL_FILE_NAME;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions.RIGHT_BUILD_WALL_FILE_NAME;

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
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.desktop.main.JenkinsTestTracker;
import uk.dangrew.jtt.desktop.utility.TestableFonts;
import uk.dangrew.jtt.desktop.utility.conversion.ColorConverter;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;

/**
 * {@link BuildWallConfigurationSessions} test.
 */
public class BuildWallConfigurationSessionsTest {

   @Mock private JenkinsDatabase database;
   @Mock private JarJsonPersistingProtocol leftProtocol;
   @Mock private JarJsonPersistingProtocol rightProtocol;
   @Captor private ArgumentCaptor< JSONObject > objectCaptor;
   
   private ColorConverter colorConverter;
   
   private BuildWallConfigurationSessions systemUnderTest;
   private BuildWallConfiguration leftConfiguration;
   private BuildWallConfiguration rightConfiguration;
   
   private CountDownLatch latch;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      colorConverter = new ColorConverter();
      
      leftConfiguration = new BuildWallConfigurationImpl();
      rightConfiguration = new BuildWallConfigurationImpl();
      
      systemUnderTest = new BuildWallConfigurationSessions( database, leftConfiguration, leftProtocol, rightConfiguration, rightProtocol );
      
      latch = new CountDownLatch( 1 );
      when( leftProtocol.writeToLocation( Mockito.any() ) ).thenAnswer( invocation -> { latch.countDown(); return true; } );
      when( rightProtocol.writeToLocation( Mockito.any() ) ).thenAnswer( invocation -> { latch.countDown(); return true; } );
   }//End Method
   
   @After public void tearDown(){
      systemUnderTest.shutdownSessions();
   }//End Method
   
   @Test public void publicConstructorShouldDefineCorrectFileLocations(){
      systemUnderTest = new BuildWallConfigurationSessions( database, leftConfiguration, rightConfiguration );
      assertThat( 
               systemUnderTest.leftConfigurationFileLocation().getLocation(), 
               is( new JarJsonPersistingProtocol( FOLDER_NAME, LEFT_BUILD_WALL_FILE_NAME, JenkinsTestTracker.class ).getLocation() ) 
      );
      
      assertThat( 
               systemUnderTest.rightConfigurationFileLocation().getLocation(), 
               is( new JarJsonPersistingProtocol( FOLDER_NAME, RIGHT_BUILD_WALL_FILE_NAME, JenkinsTestTracker.class ).getLocation() ) 
      );
   }//End Method
   
   @Test public void shouldReadRightAndLeftOnConstruction(){
      verify( leftProtocol ).readFromLocation();
      verify( rightProtocol ).readFromLocation();
   }//End Method
   
   @Test public void shutdownShouldStopWriting(){
      systemUnderTest.shutdownSessions();
      
      leftConfiguration.numberOfColumns().set( 40 );
      rightConfiguration.numberOfColumns().set( 50 );
      
      verify( leftProtocol, never() ).writeToLocation( Mockito.any() );
      verify( rightProtocol, never() ).writeToLocation( Mockito.any() );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenNumberOfColumnsChanged() throws InterruptedException {
      assertResponseToNumberOfColumnsChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenNumberOfColumnsChanged() throws InterruptedException {
      assertResponseToNumberOfColumnsChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToNumberOfColumnsChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.numberOfColumns().set( 20 );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"Columns\":20" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenDescriptionTypeChanged() throws InterruptedException {
      assertResponseToDescriptionTypeChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenDescriptionTypeChanged() throws InterruptedException {
      assertResponseToDescriptionTypeChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToDescriptionTypeChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"DescriptionType\":\"Default\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenJobAddedToPolicies() throws InterruptedException {
      assertResponseToJobPolicyChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenJobAddedToPolicies() throws InterruptedException {
      assertResponseToJobPolicyChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToJobPolicyChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.jobPolicies().put( new JenkinsJobImpl( "SomeJob" ), BuildWallJobPolicy.OnlyShowFailures );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( 
               "{\"Policy\":\"OnlyShowFailures\",\"JobName\":\"SomeJob\"}" 
      ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenJobNameFontChanged() throws InterruptedException {
      assertResponseToJobNameFontChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenJobNameFontChanged() throws InterruptedException {
      assertResponseToJobNameFontChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToJobNameFontChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.jobNameFont().set( Font.font( TestableFonts.commonFont(), 14 ) );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"JobNameFamily\":\"" + TestableFonts.commonFont() + "\"" ) );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"JobNameSize\":14" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenBuildNumberFontChanged() throws InterruptedException {
      assertResponseToBuildNumberFontChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenBuildNumberFontChanged() throws InterruptedException {
      assertResponseToBuildNumberFontChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToBuildNumberFontChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.buildNumberFont().set( Font.font( TestableFonts.commonFont(), 14 ) );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"BuildNumberFamily\":\"" + TestableFonts.commonFont() + "\"" ) );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"BuildNumberSize\":14" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenCompletionEstimateFontChanged() throws InterruptedException {
      assertResponseToCompletionEstimateFontChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenCompletionEstimateFontChanged() throws InterruptedException {
      assertResponseToCompletionEstimateFontChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToCompletionEstimateFontChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.completionEstimateFont().set( Font.font( TestableFonts.commonFont(), 14 ) );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"CompletionEstimateFamily\":\"" + TestableFonts.commonFont() + "\"" ) );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"CompletionEstimateSize\":14" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenDetailFontChanged() throws InterruptedException {
      assertResponseToDetailFontChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenDetailFontChanged() throws InterruptedException {
      assertResponseToDetailFontChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToDetailFontChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.detailFont().set( Font.font( TestableFonts.commonFont(), 14 ) );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"DetailFamily\":\"" + TestableFonts.commonFont() + "\"" ) );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"DetailSize\":14" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenJobNameColourChanged() throws InterruptedException {
      assertResponseToJobNameColourChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenJobNameColourChanged() throws InterruptedException {
      assertResponseToJobNameColourChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToJobNameColourChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.jobNameColour().set( Color.RED );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"JobNameColour\":\"" + colorConverter.colorToHex( Color.RED ) + "\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenBuildNumberColourChanged() throws InterruptedException {
      assertResponseToBuildNumberColourChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenBuildNumberColourChanged() throws InterruptedException {
      assertResponseToBuildNumberColourChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToBuildNumberColourChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.buildNumberColour().set( Color.RED );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"BuildNumberColour\":\"" + colorConverter.colorToHex( Color.RED ) + "\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenCompletionEstimateColourChanged() throws InterruptedException {
      assertResponseToCompletionEstimateColourChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenCompletionEstimateColourChanged() throws InterruptedException {
      assertResponseToCompletionEstimateColourChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToCompletionEstimateColourChange(
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.completionEstimateColour().set( Color.RED );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"CompletionEstimateColour\":\"" + colorConverter.colorToHex( Color.RED ) + "\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
   }//End Method
   
   @Test public void leftShouldTriggerWriteWhenDetailColourChanged() throws InterruptedException {
      assertResponseToDetailColourChange( leftConfiguration, leftProtocol, rightProtocol );
   }//End Method
   
   @Test public void rightShouldTriggerWriteWhenDetailColourChanged() throws InterruptedException {
      assertResponseToDetailColourChange( rightConfiguration, rightProtocol, leftProtocol );
   }//End Method
   
   private void assertResponseToDetailColourChange( 
            BuildWallConfiguration configuration, 
            JarJsonPersistingProtocol protocolToUse,
            JarJsonPersistingProtocol protocolToAvoid
   ) throws InterruptedException {
      configuration.detailColour().set( Color.RED );
      latch.await();
      
      verify( protocolToUse ).writeToLocation( objectCaptor.capture() );
      verify( protocolToAvoid, never() ).writeToLocation( Mockito.any() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"DetailColour\":\"" + colorConverter.colorToHex( Color.RED ) + "\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite( protocolToUse );
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
   
   @Test public void leftConfigurationShouldUseDefaults(){
      assertThat( leftConfiguration.numberOfColumns().get(), is( 1 ) );
      assertThat( leftConfiguration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Simple ) );
   }//End Method
   
   @Test public void rightConfigurationShouldUseDefaults(){
      assertThat( rightConfiguration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Detailed ) );
   }//End Method
   
   @Test public void shouldUseCorrectConfiguration(){
      assertThat( systemUnderTest.uses( leftConfiguration, rightConfiguration ), is( true ) );
      assertThat( systemUnderTest.uses( new BuildWallConfigurationImpl(), rightConfiguration ), is( false ) );
      assertThat( systemUnderTest.uses( leftConfiguration, new BuildWallConfigurationImpl() ), is( false ) );
      assertThat( systemUnderTest.uses( new BuildWallConfigurationImpl(), new BuildWallConfigurationImpl() ), is( false ) );
   }//End Method
   
}//End Class
