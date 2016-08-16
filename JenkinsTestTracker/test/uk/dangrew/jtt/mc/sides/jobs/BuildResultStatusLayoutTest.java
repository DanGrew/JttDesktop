/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.styling.SystemStyling;

/**
 * {@link BuildResultStatusLayout} test.
 */
public class BuildResultStatusLayoutTest {

   private JenkinsJob jobA;
   private JenkinsJob jobB;
   private JenkinsJob jobC;
   private JenkinsJob jobD;
   private JenkinsJob jobE;
   private JenkinsJob jobF;
   
   private JenkinsDatabase database;
   private JobProgressTree tree;
   private BuildResultStatusLayout systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      SystemStyling.initialise();
      
      database = new JenkinsDatabaseImpl();
      jobA = new JenkinsJobImpl( "JobA" );
      jobB = new JenkinsJobImpl( "JobB" );
      jobC = new JenkinsJobImpl( "JobC" );
      jobD = new JenkinsJobImpl( "JobD" );
      jobE = new JenkinsJobImpl( "JobE" );
      jobF = new JenkinsJobImpl( "JobF" );
      
      tree = new JobProgressTree( database );
      systemUnderTest = new BuildResultStatusLayout( tree );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullJobsList() {
      systemUnderTest.reconstructTree( null );
   }//End Method
   
   @Test public void shouldLayoutJobsAccordingToBuildResultStatusAlphabetically() {
      jobA.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      jobB.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      jobC.lastBuildStatusProperty().set( BuildResultStatus.NOT_BUILT );
      jobD.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      jobE.lastBuildStatusProperty().set( BuildResultStatus.UNKNOWN );
      jobF.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.reconstructTree( Arrays.asList( 
               jobA, jobB, jobC, jobD, jobE, jobF 
      ) );
      
      List< TreeItem< JobProgressTreeItem > > children = systemUnderTest.getBranch( BuildResultStatus.ABORTED ).getChildren();
      assertThat( children, hasSize( 1 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobA ) );
      
      children = systemUnderTest.getBranch( BuildResultStatus.FAILURE ).getChildren();
      assertThat( children, hasSize( 1 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobB ) );
      
      children = systemUnderTest.getBranch( BuildResultStatus.NOT_BUILT ).getChildren();
      assertThat( children, hasSize( 1 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobC ) );
      
      children = systemUnderTest.getBranch( BuildResultStatus.SUCCESS ).getChildren();
      assertThat( children, hasSize( 1 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobD ) );
      
      children = systemUnderTest.getBranch( BuildResultStatus.UNKNOWN ).getChildren();
      assertThat( children, hasSize( 1 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobE ) );
      
      children = systemUnderTest.getBranch( BuildResultStatus.UNSTABLE ).getChildren();
      assertThat( children, hasSize( 1 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobF ) );
   }//End Method
   
   @Test public void shouldGroupdJobsOfSameStatus() {
      jobA.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      jobB.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      jobC.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      
      systemUnderTest.reconstructTree( Arrays.asList( jobA, jobB, jobC ) );
      
      List< TreeItem< JobProgressTreeItem > > children = systemUnderTest.getBranch( BuildResultStatus.SUCCESS ).getChildren();
      assertThat( children, hasSize( 3 ) );
      assertThat( children.get( 0 ).getValue().getJenkinsJob(), is( jobA ) );
      assertThat( children.get( 1 ).getValue().getJenkinsJob(), is( jobB ) );
      assertThat( children.get( 2 ).getValue().getJenkinsJob(), is( jobC ) );
   }//End Method
   
   @SuppressWarnings("unchecked")//contains with generic 
   @Test public void shouldLayoutStatusesAlphabetically() {
      systemUnderTest.reconstructTree( new ArrayList<>() );
      
      assertThat( tree.getRoot().getChildren(), contains( 
               systemUnderTest.getBranch( BuildResultStatus.ABORTED ),
               systemUnderTest.getBranch( BuildResultStatus.FAILURE ),
               systemUnderTest.getBranch( BuildResultStatus.NOT_BUILT ),
               systemUnderTest.getBranch( BuildResultStatus.SUCCESS ),
               systemUnderTest.getBranch( BuildResultStatus.UNKNOWN ),
               systemUnderTest.getBranch( BuildResultStatus.UNSTABLE )
      ) );
   }//End Method
   
   @Test public void shouldProvideBranchesWithItems(){
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         TreeItem< JobProgressTreeItem > branch = systemUnderTest.getBranch( status );
         assertThat( branch.getValue(), is( instanceOf( JobProgressTreeItemBranch.class ) ) );
         JobProgressTreeItemBranch branchItem = ( JobProgressTreeItemBranch ) branch.getValue();
         assertThat( branchItem.getName(), is( status.name() ) );
      }
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAddJobIfNotConstructed() {
      systemUnderTest.add( jobA );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRemoveJobIfNotConstructed() {
      systemUnderTest.remove( jobA );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotUpdateJobIfNotConstructed() {
      systemUnderTest.update( jobA );
   }//End Method
   
   @Test public void shouldAddJobNotInTree() {
      systemUnderTest.reconstructTree( new ArrayList<>() );
      systemUnderTest.add( jobA );
      assertJobPresent( jobA, 0 );
   }//End Method
   
   @Test public void shouldIgnoreAddJobAlreadyInTree() {
      systemUnderTest.reconstructTree( Arrays.asList( jobA ) );
      systemUnderTest.add( jobA );
      assertJobPresent( jobA, 0 );
      assertBranchSize( BuildResultStatus.NOT_BUILT, 1 );
   }//End Method
   
   @Test public void shouldRemoveJobFromTree() {
      systemUnderTest.reconstructTree( Arrays.asList( jobA, jobB, jobC ) );
      systemUnderTest.remove( jobB );
      assertJobPresent( jobA, 0 );
      assertJobPresent( jobC, 1 );
      assertBranchSize( BuildResultStatus.NOT_BUILT, 2 );
   }//End Method
   
   @Test public void shouldIgnoreRemoveJobIfNotPresent() {
      systemUnderTest.reconstructTree( Arrays.asList( jobA, jobB, jobC ) );
      systemUnderTest.remove( jobD );
      assertJobPresent( jobA, 0 );
      assertJobPresent( jobB, 1 );
      assertJobPresent( jobC, 2 );
      assertBranchSize( BuildResultStatus.NOT_BUILT, 3 );
   }//End Method
   
   @Test public void shouldUpdateJobPositionWhenStatusChanges() {
      systemUnderTest.reconstructTree( Arrays.asList( jobA, jobB, jobC ) );
      
      jobB.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      systemUnderTest.update( jobB );
      assertJobPresent( jobA, 0 );
      assertJobPresent( jobC, 1 );
      assertJobPresent( jobB, 0 );
      assertBranchSize( BuildResultStatus.NOT_BUILT, 2 );
      assertBranchSize( BuildResultStatus.SUCCESS, 1 );
   }//End Method
   
   @Test public void shouldIgnoreUpdateJobWhenStatusChangesIfNotPresent() {
      systemUnderTest.reconstructTree( Arrays.asList( jobA, jobB, jobC ) );
      jobD.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      systemUnderTest.update( jobD );
      assertJobPresent( jobA, 0 );
      assertJobPresent( jobB, 1 );
      assertJobPresent( jobC, 2 );
      assertBranchSize( BuildResultStatus.NOT_BUILT, 3 );
      assertBranchSize( BuildResultStatus.SUCCESS, 0 );
   }//End Method
   
   /**
    * Method to assert that the {@link JenkinsJob} is present in the correct branch and location.
    * @param job the {@link JenkinsJob} in question.
    * @param index the index in the children.
    */
   private void assertJobPresent( JenkinsJob job, int index ) {
      TreeItem< JobProgressTreeItem > branch = systemUnderTest.getBranch( job.lastBuildStatusProperty().get() );
      assertThat( branch.getChildren().get( index ).getValue().getJenkinsJob(), is( job ) );
   }//End Method
   
   /**
    * Method to assert that the {@link BuildResultStatus} branch has the correct size.
    * @param status the {@link BuildResultStatus} for the branch.
    * @param size the size the branch should be.
    */
   private void assertBranchSize( BuildResultStatus status, int size ) {
      TreeItem< JobProgressTreeItem > branch = systemUnderTest.getBranch( status );
      assertThat( branch.getChildren(), hasSize( size ) );
   }//End Method

}//End Class
