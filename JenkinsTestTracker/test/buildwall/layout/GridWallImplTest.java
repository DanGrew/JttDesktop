/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.layout;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.panel.JobBuildSimulator;
import buildwall.panel.JobPanelImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import graphics.TestPlatformDecouplerImpl;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import styling.SystemStyling;
import utility.TestCommon;

/**
 * {@link GridWallImpl} test.
 */
public class GridWallImplTest {

   private BuildWallConfiguration configuration;
   private JenkinsDatabase database;
   private GridWallImpl systemUnderTest;
   
   @BeforeClass public static void initialiseStylings(){
      SystemStyling.initialise();
   }//End Method
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      database = new JenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "Some Job" ) );
      database.store( new JenkinsJobImpl( "A Nice Complicated Title" ) );
      database.store( new JenkinsJobImpl( "Another Job" ) );
      database.store( new JenkinsJobImpl( "Please don't break!" ) );
      database.store( new JenkinsJobImpl( "The Odd One Out..." ) );
      
      configuration = new BuildWallConfigurationImpl();
      JavaFxInitializer.startPlatform();
      systemUnderTest = new GridWallImpl( configuration, database );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      JavaFxInitializer.launchInWindow( () -> { return new BorderPane( systemUnderTest ); } );
      
      Thread.sleep( 2000 );
      configuration.jobNameFont().set( new Font( 20 ) );
      Thread.sleep( 2000 );
      configuration.jobNameFont().set( new Font( 25 ) );
      Thread.sleep( 2000 );
      configuration.jobNameFont().set( new Font( 30 ) );
      Thread.sleep( 2000 );
      configuration.jobNameFont().set( new Font( 35 ) );
      Thread.sleep( 2000 );
      configuration.jobNameFont().set( new Font( 40 ) );
      Thread.sleep( 2000 );
      configuration.jobNameFont().set( new Font( 45 ) );
      
      JobBuildSimulator.simulateConcurrentBuilding( database.jenkinsJobs().get( 0 ), BuildResultStatus.FAILURE, 233, 300000, 300 );
      JobBuildSimulator.simulateConcurrentBuilding( database.jenkinsJobs().get( 1 ), BuildResultStatus.SUCCESS, 1002, 600000, 300 );
      JobBuildSimulator.simulateConcurrentBuilding( database.jenkinsJobs().get( 2 ), BuildResultStatus.UNSTABLE, 111, 20000, 300 );
      JobBuildSimulator.simulateConcurrentBuilding( database.jenkinsJobs().get( 3 ), BuildResultStatus.ABORTED, 3463456, 234768, 300 );
      JobBuildSimulator.simulateConcurrentBuilding( database.jenkinsJobs().get( 4 ), BuildResultStatus.SUCCESS, 34, 90000, 300 );
      
      Thread.sleep( 4000 );
      configuration.numberOfColumns().set( 1 );
      Thread.sleep( 4000 );
      configuration.numberOfColumns().set( 2 );
      Thread.sleep( 4000 );
      configuration.numberOfColumns().set( 3 );
      Thread.sleep( 4000 );
      configuration.numberOfColumns().set( 4 );
      Thread.sleep( 4000 );
      configuration.numberOfColumns().set( 5 );
      Thread.sleep( 4000 );
      configuration.numberOfColumns().set( 6 );
      Thread.sleep( 4000 );

      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldUpdateWithConfiguration() {
      configuration.numberOfColumns().set( 7 );
      assertIndexConstraints( 5, 1 );
      configuration.numberOfColumns().set( 1 );
      assertIndexConstraints( 1, 5 );
   }//End Method
   
   @Test public void shouldDisplayAllJobsInDatabase() {
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         Assert.assertNotNull( systemUnderTest.getPanelFor( job ) );
         Assert.assertEquals( job, systemUnderTest.getPanelFor( job ).getJenkinsJob() );
      }
   }//End Method
   
   @Test public void shouldDisplayJobsWhenAdded() {
      shouldDisplayAllJobsInDatabase();
      database.store( new JenkinsJobImpl( "Late Addition : )" ) );
      shouldDisplayAllJobsInDatabase();
   }//End Method
   
   @Test public void shouldClearConstraintsWhenRedrawn() {
      shouldDisplayAllJobsInDatabase();
      JobPanelImpl firstPanel = systemUnderTest.getPanelFor( database.jenkinsJobs().get( 0 ) );
      Assert.assertNotNull( GridPane.getColumnIndex( firstPanel ) );
      
      systemUnderTest.constructLayout();
      Assert.assertNull( GridPane.getColumnIndex( firstPanel ) );
   }//End Method
   
   @Test public void shouldClearMappingWhenRedrawn() {
      shouldDisplayAllJobsInDatabase();
      
      JenkinsJob job = database.jenkinsJobs().get( 0 );
      JobPanelImpl firstPanel = systemUnderTest.getPanelFor( job );
      Assert.assertNotNull( GridPane.getColumnIndex( firstPanel ) );
      
      database.removeJenkinsJob( job );
      Assert.assertNull( systemUnderTest.getPanelFor( job ) );
   }//End Method
   
   @Test public void shouldPlaceJobsAccordingToNumberOfColumns() {
      assertIndexConstraints( configuration.numberOfColumns().get(), 3 );
   }//End Method
   
   /**
    * Method to assert that the constraints of the positions in the {@link GridPane} are obeyed.
    * @param numberOfColumns the number of columns expected.
    * @param numberOfRows the number of rows expected.
    */
   private void assertIndexConstraints( int numberOfColumns, int numberOfRows ) {
      for ( Node node : systemUnderTest.getChildren() ) {
         Assert.assertTrue( GridPane.getColumnIndex( node ) < numberOfColumns );
         Assert.assertTrue( GridPane.getRowIndex( node ) < numberOfRows );
      }
   }//End Method
   
   @Test public void shouldHandleZeroColumns() {
      configuration.numberOfColumns().set( 0 );
      systemUnderTest = new GridWallImpl( configuration, database );
      assertIndexConstraints( 1, 5 );
   }//End Method
   
   @Test public void shouldHandleInvalidColumns() {
      configuration.numberOfColumns().set( -1 );
      systemUnderTest = new GridWallImpl( configuration, database );
      assertIndexConstraints( 1, 5 );
   }//End Method
   
   @Test public void shouldConstrainWidthToBeEqual() {
      Assert.assertFalse( systemUnderTest.getColumnConstraints().isEmpty() );
      double expectedWidth = systemUnderTest.getColumnConstraints().get( 0 ).getPercentWidth();
      for ( int i = 1; i < systemUnderTest.getColumnConstraints().size(); i++ ) {
         Assert.assertEquals( expectedWidth, systemUnderTest.getColumnConstraints().get( i ).getPercentWidth(), TestCommon.precision() );
      }
   }//End Method
   
   @Test public void shouldConstrainHeightToBeEqual() {
      Assert.assertFalse( systemUnderTest.getRowConstraints().isEmpty() );
      double expectedheight = systemUnderTest.getRowConstraints().get( 0 ).getPercentHeight();
      for ( int i = 1; i < systemUnderTest.getRowConstraints().size(); i++ ) {
         Assert.assertEquals( expectedheight, systemUnderTest.getRowConstraints().get( i ).getPercentHeight(), TestCommon.precision() );
      }
   }//End Method
   
   @Test public void shouldExpandSingleItemRowIntoTwoColumns(){
      assertLastElementSpans( 2 );
   }//End Method
   
   /**
    * Method to assert that the last element in the {@link GridPane} spans the correct 
    * number of columns.
    * @param expectedSpan the expected number of columns to span.
    */
   private void assertLastElementSpans( int expectedSpan ){
      ObservableList< Node > children = systemUnderTest.getChildren();
      for ( int i = 0; i < children.size() - 1; i++ ) {
         Assert.assertNull( GridPane.getColumnSpan( children.get( i ) ) );
      }
      if ( expectedSpan == 1 ) {
         Assert.assertNull( GridPane.getColumnSpan( children.get( children.size() - 1 ) ) );
      } else {
         Assert.assertEquals( expectedSpan, GridPane.getColumnSpan( children.get( children.size() - 1 ) ).intValue() );
      }
   }//End Method
   
   @Test public void shouldExpandLastAcrossAllColumnsOnMultipleRows(){
      configuration.numberOfColumns().set( 4 );
      systemUnderTest = new GridWallImpl( configuration, database );
      assertLastElementSpans( 4 );
   }//End Method
   
   @Test public void shouldNotExpandLastWhenMoreColumnsThanJobs(){
      configuration.numberOfColumns().set( 7 );
      systemUnderTest = new GridWallImpl( configuration, database );
      assertLastElementSpans( 1 );
      assertIndexConstraints( 5, 1 );
   }//End Method

   @Test public void shouldCalculateProportionsAccountingForRounding(){
      Assert.assertTrue( GridWallImpl.calculateProportion( 0 ) > 100 );
      assertProportionScales( 2 );
      assertProportionScales( 2 );
      assertProportionScales( 3 );
      assertProportionScales( 4 );
      assertProportionScales( 5 );
      assertProportionScales( 6 );
      assertProportionScales( 7 );
      assertProportionScales( 20 );
   }//End Method
   
   /**
    * Method to assert that the correct proportion is found for the given number of items.
    * @param numberOfItems the number of items the proportion is for.
    */
   private void assertProportionScales( int numberOfItems ) {
      Assert.assertTrue( GridWallImpl.calculateProportion( numberOfItems ) * numberOfItems > 100 );
   }//End Method
   
}//End Class
