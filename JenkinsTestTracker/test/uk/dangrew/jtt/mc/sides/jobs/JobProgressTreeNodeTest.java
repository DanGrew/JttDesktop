/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.panel.JobProgressImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.styling.SystemStyling;

/**
 * {@link JobProgressTreeNode} test.
 */
public class JobProgressTreeNodeTest {

   @Spy private JavaFxStyle styling;
   private JenkinsJob job;
   private JobProgressTreeNode systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      SystemStyling.initialise();
      MockitoAnnotations.initMocks( this );
      job = new JenkinsJobImpl( "my job" );
      systemUnderTest = new JobProgressTreeNode( job, styling );
   }//End Method
   
   @Test public void shouldBeAssociatedWith() {
      assertThat( systemUnderTest.isAssociatedWith( job ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new JenkinsJobImpl( "anything" ) ), is( false ) );
   }//End Method
   
   @Test public void shouldHaveJobName(){
      verify( styling ).createBoldLabel( job.nameProperty().get() );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.jobName() ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveJobProgress(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.jobProgress() ), is( true ) );
      assertThat( systemUnderTest.jobProgress().getCenter(), is( instanceOf( JobProgressImpl.class ) ) );
      JobProgressImpl progress = ( JobProgressImpl ) systemUnderTest.jobProgress().getCenter();
      assertThat( progress.isAssociatedWith( job ), is( true ) );
   }//End Method
   
   @Test public void shouldUseColumnConstraints(){
      assertThat( systemUnderTest.getColumnConstraints().get( 0 ).getPercentWidth(), is( JobProgressTreeNode.NAME_PROPORTION ) );
      assertThat( systemUnderTest.getColumnConstraints().get( 0 ).getMinWidth(), is( JobProgressTreeNode.MINIMUM_JOB_NAME_WIDTH ) );
      assertThat( systemUnderTest.getColumnConstraints().get( 1 ).getPercentWidth(), is( JobProgressTreeNode.PROGRESS_PROPORTION ) );
   }//End Method

}//End Class
