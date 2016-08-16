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

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.styling.SystemStyling;

/**
 * {@link JobProgressTreeItemImpl} test.
 */
public class JobProgressTreeItemImplTest {
   
   private JenkinsJob job;
   private JobProgressTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      SystemStyling.initialise();
      job = new JenkinsJobImpl( "NewJob" );
      systemUnderTest = new JobProgressTreeItemImpl( job );
   }//End Method

   @Test public void shouldProvideJob() {
      assertThat( systemUnderTest.getJenkinsJob(), is( job ) );
   }//End Method
   
   @Test public void shouldProvideJobProgressNodeInProperty(){
      assertThat( systemUnderTest.contentProperty().get(), is( instanceOf( JobProgressTreeNode.class ) ) );
      JobProgressTreeNode node = ( JobProgressTreeNode ) systemUnderTest.contentProperty().get();
      assertThat( node.isAssociatedWith( job ), is( true ) );
   }//End Method

}//End Class
