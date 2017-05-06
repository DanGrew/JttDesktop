/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration.components;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;

public class StatisticsExclusionsPanelTest {

   @Spy private JavaFxStyle styling;
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private StatisticsExclusionsPanel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      JavaFxInitializer.startPlatform();
      configuration = new StatisticsConfiguration();
      database = new TestJenkinsDatabaseImpl();
      for ( int i = 0; i < 10; i++ ) {
         database.store( new JenkinsJobImpl( "Job " + i ) );
      }
      systemUnderTest = new StatisticsExclusionsPanel( styling, database, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 100000000 );
   }

   @Test public void shouldUpdateExclusionsWhenTicked() {
      JenkinsJob job = database.jenkinsJobs().get( 4 );
      CheckBox box = systemUnderTest.checkBoxFor( job );
      assertThat( configuration.excludedJobs().contains( job ), is( false ) );
      box.setSelected( false );
      assertThat( configuration.excludedJobs().contains( job ), is( true ) );
      box.setSelected( true );
      assertThat( configuration.excludedJobs().contains( job ), is( false ) );
   }//End Method
   
   @Test public void shouldUpdateJobListWhenJobAddedToDatabase() {
      CheckBox randomBox = systemUnderTest.checkBoxFor( database.jenkinsJobs().get( 4 ) );
      JenkinsJob extra = new JenkinsJobImpl( "extra" );
      database.store( extra );
      assertThat( systemUnderTest.checkBoxFor( database.jenkinsJobs().get( 4 ) ), not( randomBox ) );
      
      assertThat( systemUnderTest.checkBoxFor( extra ), is( notNullValue() ) );
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         assertThat( systemUnderTest.checkBoxFor( job ), is( notNullValue() ) );
      }
   }//End Method
   
   @Test public void shouldUpdateJobListWhenJobRemovedFromDatabase() {
      CheckBox randomBox = systemUnderTest.checkBoxFor( database.jenkinsJobs().get( 4 ) );
      database.removeJenkinsJob( database.jenkinsJobs().get( 3 ) );
      assertThat( systemUnderTest.checkBoxFor( database.jenkinsJobs().get( 4 ) ), not( randomBox ) );
      
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         assertThat( systemUnderTest.checkBoxFor( job ), is( notNullValue() ) );
      }
   }//End Method
   
   @Test public void shouldUpdateTicksWhenConfigurationChanges() {
      JenkinsJob job = database.jenkinsJobs().get( 4 );
      CheckBox box = systemUnderTest.checkBoxFor( job );
      assertThat( box.isSelected(), is( true ) );
      configuration.excludedJobs().add( job );
      assertThat( box.isSelected(), is( false ) );
      configuration.excludedJobs().remove( job );
      assertThat( box.isSelected(), is( true ) );
   }//End Method
   
   @Test public void shouldContainCheckPerJob() {
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         assertThat( systemUnderTest.checkBoxFor( job ), is( notNullValue() ) );
         assertThat( systemUnderTest.getChildren().contains( systemUnderTest.checkBoxFor( job ) ), is( true ) );
      }
   }//End Method
   
   @Test public void shouldUsePadding() {
      assertThat( systemUnderTest.getPadding().getBottom(), is( StatisticsExclusionsPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( StatisticsExclusionsPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( StatisticsExclusionsPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( StatisticsExclusionsPanel.PADDING ) );
   }//End Method
   
   @Test public void shouldDetachChecksFromSystemWhenRefreshed() {
      JenkinsJob job = database.jenkinsJobs().get( 4 );
      CheckBox randomBox = systemUnderTest.checkBoxFor( job );
      database.removeJenkinsJob( database.jenkinsJobs().get( 3 ) );
      
      assertThat( configuration.excludedJobs().contains( job ), is( false ) );
      randomBox.setSelected( true );
      assertThat( configuration.excludedJobs().contains( job ), is( false ) );
   }//End Method
   
   @Test public void shouldExpandToFullWidthOfParent() {
      verify( styling ).configureFullWidthConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldContainSelectedAndDeselectAll(){
      assertThat( systemUnderTest.selectAll(), is( notNullValue() ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.selectAll() ), is( true ) );
   }//End Method
   
   @Test public void shouldSelectedAndDeselectAll(){
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         assertThat( systemUnderTest.checkBoxFor( job ).isSelected(), is( true ) );
      }
      systemUnderTest.selectAll().setSelected( false );
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         assertThat( systemUnderTest.checkBoxFor( job ).isSelected(), is( false ) );
      }
      systemUnderTest.selectAll().setSelected( true );
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         assertThat( systemUnderTest.checkBoxFor( job ).isSelected(), is( true ) );
      }
   }//End Method

   @Test public void shouldRetainSelectDeselectAll(){
      CheckBox selectAll = systemUnderTest.selectAll();
      database.removeJenkinsJob( database.jenkinsJobs().get( 0 ) );
      assertThat( systemUnderTest.selectAll(), is( selectAll ) );
   }//End Method
   
   @Test public void shouldSortJobsAlphabetically(){
      List< JenkinsJob > existing = new ArrayList<>();
      existing.addAll( database.jenkinsJobs() );
      existing.forEach( j -> database.removeJenkinsJob( j ) );
      
      JenkinsJob jobA = new JenkinsJobImpl( "aaaa" );
      JenkinsJob jobB = new JenkinsJobImpl( "bbbb" );
      JenkinsJob jobC = new JenkinsJobImpl( "dddd" );
      JenkinsJob jobD = new JenkinsJobImpl( "xxxx" );
      JenkinsJob jobE = new JenkinsJobImpl( "zzzz" );
      JenkinsJob jobF = new JenkinsJobImpl( "bcde" );
      database.store( jobF );
      database.store( jobB );
      database.store( jobA );
      database.store( jobD );
      database.store( jobE );
      database.store( jobC );
      
      assertThat( GridPane.getRowIndex( systemUnderTest.checkBoxFor( jobA ) ), is( 1 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.checkBoxFor( jobB ) ), is( 2 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.checkBoxFor( jobC ) ), is( 4 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.checkBoxFor( jobD ) ), is( 5 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.checkBoxFor( jobE ) ), is( 6 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.checkBoxFor( jobF ) ), is( 3 ) );
   }//End Method
   
}//End Class
