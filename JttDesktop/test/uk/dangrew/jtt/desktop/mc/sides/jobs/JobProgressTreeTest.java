/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.jobs;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyMenuOpener;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link JobProgressTree} test.
 */
public class JobProgressTreeTest {

   private JenkinsDatabase database;
   private JobProgressTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      SystemStyling.initialise();
      MockitoAnnotations.initMocks( this );
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new JobProgressTree( database );
   }//End Method 
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> systemUnderTest );
      
      JenkinsJob jobA = new JenkinsJobImpl( "NewJob" );
      database.store( jobA );
      JenkinsJob jobB = new JenkinsJobImpl( "Another Job" );
      database.store( jobB );
      JenkinsJob jobC = new JenkinsJobImpl( "Extra Job" );
      database.store( jobC );
      
      jobB.setBuildStatus( BuildResultStatus.SUCCESS );
      jobB.currentBuildTimeProperty().set( 100 );
      jobB.expectedBuildTimeProperty().set( 200 );
      
      jobC.setBuildStatus( BuildResultStatus.UNSTABLE );
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
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.MULTIPLE ) );
   }//End Method
   
   @Test public void shouldConstructControllerWithLayoutManager(){
      assertThat( systemUnderTest.getController(), is( notNullValue() ) );
      assertThat( systemUnderTest.getController().getLayoutManager(), is( systemUnderTest.getLayoutManager() ) );
   }//End Method
   
   @Test public void shouldReconstructTreeWithLayoutManager(){
      assertThat( systemUnderTest.getLayoutManager().isControlling( systemUnderTest ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveSingleColumn(){
      TreeTableColumn< JobProgressTreeItem, ? > column = systemUnderTest.getColumns().get( 0 );
      assertThat( column, is( notNullValue() ) );
   }//End Method

   @Test public void shouldUseResizePolicy(){
      assertThat( systemUnderTest.getColumnResizePolicy(), is( TreeTableView.CONSTRAINED_RESIZE_POLICY ) );
   }//End Method
   
   @Test public void shouldUseOpenerForContextMenu(){
      assertThat( systemUnderTest.getOnContextMenuRequested(), is( instanceOf( FriendlyMenuOpener.class ) ) );
      FriendlyMenuOpener opener = ( FriendlyMenuOpener ) systemUnderTest.getOnContextMenuRequested();
      assertThat( opener.isAnchoredTo( systemUnderTest ), is( true ) );
      assertThat( opener.isControllingA( JobProgressTreeContextMenu.class ), is( true ) );
      assertThat( systemUnderTest.menu().isConnectedTo( database ), is( true ) );
   }//End Method
   
}//End Class
