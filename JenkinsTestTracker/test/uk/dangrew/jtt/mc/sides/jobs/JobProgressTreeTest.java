/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.javafx.tree.utility.ColumnHeaderHider;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.styling.SystemStyling;

/**
 * {@link JobProgressTree} test.
 */
public class JobProgressTreeTest {

   @Spy private ColumnHeaderHider hider;
   private JenkinsDatabase database;
   private JobProgressTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      SystemStyling.initialise();
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new JobProgressTree( database, hider );
   }//End Method 
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      JenkinsJob jobA = new JenkinsJobImpl( "NewJob" );
      database.store( jobA );
      JenkinsJob jobB = new JenkinsJobImpl( "Another Job" );
      database.store( jobB );
      JenkinsJob jobC = new JenkinsJobImpl( "Extra Job" );
      database.store( jobC );
      
      jobB.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      jobB.currentBuildTimeProperty().set( 100 );
      jobB.expectedBuildTimeProperty().set( 200 );
      
      jobC.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      jobC.currentBuildTimeProperty().set( 75 );
      jobC.expectedBuildTimeProperty().set( 100 );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldHaveRootSetAndConfigured(){
      assertThat( systemUnderTest.getRoot(), is( notNullValue() ) );
      assertThat( systemUnderTest.getRoot().isExpanded(), is( true ) );
      assertThat( systemUnderTest.isShowRoot(), is( false ) );
   }//End Method
   
   @Test public void shouldOnlySupportSingleSelection(){
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.SINGLE ) );
   }//End Method
   
   @Test public void shouldConstructControllerWithLayoutManager(){
      assertThat( systemUnderTest.controller(), is( notNullValue() ) );
      assertThat( systemUnderTest.controller().layoutManager(), is( systemUnderTest.layoutManager() ) );
   }//End Method
   
   @Test public void shouldHideColumnHeaders(){
      verify( hider ).hideColumnHeaders( systemUnderTest );
   }//End Method
   
   @Test public void shouldReconstructTreeWithLayoutManager(){
      assertThat( systemUnderTest.layoutManager().isControlling( systemUnderTest ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveSingleColumn(){
      TreeTableColumn< JobProgressTreeItem, ? > column = systemUnderTest.getColumns().get( 0 );
      assertThat( column, is( notNullValue() ) );
   }//End Method

   @Test public void shouldUseResizePolicy(){
      assertThat( systemUnderTest.getColumnResizePolicy(), is( TreeTableView.CONSTRAINED_RESIZE_POLICY ) );
   }//End Method
   
}//End Class
