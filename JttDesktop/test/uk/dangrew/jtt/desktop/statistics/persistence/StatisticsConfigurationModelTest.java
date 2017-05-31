/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static uk.dangrew.jtt.desktop.utility.TestableFonts.commonFont;
import static uk.dangrew.jtt.model.utility.TestCommon.precision;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.utility.conversion.ColorConverter;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class StatisticsConfigurationModelTest {

   private static final String ANYTHING = "anything";
   private static final String RED_HEX = new ColorConverter().colorToHex( Color.RED );
   
   private JenkinsJob firstJenkinsJob;
   private JenkinsJob secondJenkinsJob;
   private JenkinsJob thirdJenkinsJob;
   
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private StatisticsConfigurationModel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      firstJenkinsJob = new JenkinsJobImpl( "firstJob" );
      secondJenkinsJob = new JenkinsJobImpl( "secondJob" );
      thirdJenkinsJob = new JenkinsJobImpl( "thirdJob" );
      
      configuration = new StatisticsConfiguration();
      database = new TestJenkinsDatabaseImpl();
      database.store( firstJenkinsJob );
      database.store( secondJenkinsJob );
      database.store( thirdJenkinsJob );
      systemUnderTest = new StatisticsConfigurationModel( configuration, database );
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNotAcceptNullDatabase(){
      new StatisticsConfigurationModel( configuration, null );
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNotAcceptNullConfiguration(){
      new StatisticsConfigurationModel( null, database );
   }//End Method
   
   @Test public void shouldSetBackgroundColour(){
      systemUnderTest.setBackgroundColour( ANYTHING, RED_HEX );
      assertThat( configuration.statisticBackgroundColourProperty().get(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldSetTextColour(){
      systemUnderTest.setTextColour( ANYTHING, RED_HEX );
      assertThat( configuration.statisticTextColourProperty().get(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldSetValueTextFontFamily(){
      systemUnderTest.setValueTextFontFamily( ANYTHING, commonFont() );
      assertThat( configuration.statisticValueTextFontProperty().get().getFamily(), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldSetValueTextFontSize(){
      systemUnderTest.setValueTextFontSize( ANYTHING, 25.0 );
      assertThat( configuration.statisticValueTextFontProperty().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldSetDescriptionTextFontFamily(){
      systemUnderTest.setDescriptionTextFontFamily( ANYTHING, commonFont() );
      assertThat( configuration.statisticDescriptionTextFontProperty().get().getFamily(), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldSetDescriptionTextFontSize(){
      systemUnderTest.setDescriptionTextFontSize( ANYTHING, 25.0 );
      assertThat( configuration.statisticDescriptionTextFontProperty().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideNumberOfExclusions(){
      assertThat( systemUnderTest.getNumberOfExclusions( ANYTHING ), is( 0 ) );
      configuration.excludedJobs().addAll( database.jenkinsJobs() );
      assertThat( systemUnderTest.getNumberOfExclusions( ANYTHING ), is( database.jenkinsJobs().size() ) );
   }//End Method
   
   @Test public void shouldClearCurrentExclusionsOnRead(){
      configuration.excludedJobs().add( database.jenkinsJobs().get( 0 ) );
      systemUnderTest.startReadingExclusions( ANYTHING );
      assertThat( configuration.excludedJobs(), is( empty() ) );
   }//End Method
   
   @Test public void shouldAddExclusionFromDatabase(){
      systemUnderTest.setExclusion( ANYTHING, database.jenkinsJobs().get( 0 ).nameProperty().get() );
      assertThat( configuration.excludedJobs().contains( database.jenkinsJobs().get( 0 ) ), is( true ) );
   }//End Method
   
   @Test public void shouldCreateExclusionJobIfNotPresentInDatabase(){
      systemUnderTest.setExclusion( ANYTHING, ANYTHING );
      assertThat( database.jenkinsJobs().get( 3 ).nameProperty().get(), is( ANYTHING ) ); 
      assertThat( configuration.excludedJobs().contains( database.jenkinsJobs().get( 3 ) ), is( true ) );
   }//End Method
   
   @Test public void shouldWriteExclusions(){
      configuration.excludedJobs().addAll( database.jenkinsJobs() );
      systemUnderTest.startWritingExclusions( ANYTHING );
      
      for ( int i = 0; i < database.jenkinsJobs().size(); i++ ) {
         assertThat( systemUnderTest.getExclusion( ANYTHING, i ), is( database.jenkinsJobs().get( i ).nameProperty().get() ) );
      }
      
      assertThat( systemUnderTest.getExclusion( ANYTHING, 0 ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldClearExistingWriteWhenStarting(){
      configuration.excludedJobs().addAll( database.jenkinsJobs() );
      systemUnderTest.startWritingExclusions( ANYTHING );
      
      systemUnderTest.getExclusion( ANYTHING, 0 );
      systemUnderTest.getExclusion( ANYTHING, 0 );
      
      systemUnderTest.startWritingExclusions( ANYTHING );
      
      for ( int i = 0; i < database.jenkinsJobs().size(); i++ ) {
         assertThat( systemUnderTest.getExclusion( ANYTHING, i ), is( database.jenkinsJobs().get( i ).nameProperty().get() ) );
      }
   }//End Method
   
   @Test public void shouldNotWriteDuplicateExclusions(){
      configuration.excludedJobs().addAll( database.jenkinsJobs() );
      configuration.excludedJobs().addAll( database.jenkinsJobs() );
      systemUnderTest.startWritingExclusions( ANYTHING );
      
      for ( int i = 0; i < database.jenkinsJobs().size(); i++ ) {
         assertThat( systemUnderTest.getExclusion( ANYTHING, i ), is( database.jenkinsJobs().get( i ).nameProperty().get() ) );
      }
   }//End Method
   
   @Test public void shouldProvideValueTextFontFamily(){
      configuration.statisticValueTextFontProperty().set( Font.font( commonFont(), 25 ) );
      assertThat( systemUnderTest.getValueTextFontFamily( ANYTHING ), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldProvideDescriptionTextFontFamily(){
      configuration.statisticDescriptionTextFontProperty().set( Font.font( commonFont(), 25 ) );
      assertThat( systemUnderTest.getDescriptionTextFontFamily( ANYTHING ), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldProvideValueTextFontSize(){
      configuration.statisticValueTextFontProperty().set( Font.font( commonFont(), 25 ) );
      assertThat( systemUnderTest.getValueTextFontSize( ANYTHING ), is( 25.0 ) );
   }//End Method
   
   @Test public void shouldProvideDescriptionTextFontSize(){
      configuration.statisticDescriptionTextFontProperty().set( Font.font( commonFont(), 25 ) );
      assertThat( systemUnderTest.getDescriptionTextFontSize( ANYTHING ), is( 25.0 ) );
   }//End Method
   
   @Test public void shouldProvideBackgroundColour(){
      configuration.statisticBackgroundColourProperty().set( Color.RED );
      assertThat( systemUnderTest.getBackgroundColour( ANYTHING ), is( RED_HEX ) );
   }//End Method
   
   @Test public void shouldProvideTextColour(){
      configuration.statisticTextColourProperty().set( Color.RED );
      assertThat( systemUnderTest.getTextColour( ANYTHING ), is( RED_HEX ) );
   }//End Method
}//End Class
