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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.Label;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.mc.sides.jobs.JobProgressTreeItemBranch;

/**
 * {@link JobProgressTreeItemBranch} test.
 */
public class JobProgressTreeItemBranchTest {

   private static final String BRANCH_NAME = "my branch";
   
   @Spy private JavaFxStyle styling;
   private JobProgressTreeItemBranch systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JobProgressTreeItemBranch( BRANCH_NAME, styling );
   }//End Method
   
   @Test public void shouldUseStyleWithName() {
      assertThat( systemUnderTest.contentProperty().get(), is( instanceOf( Label.class ) ) );
      Label label = ( Label ) systemUnderTest.contentProperty().get();
      assertThat( label.getText(), is( BRANCH_NAME ) );
      verify( styling ).createBoldLabel( BRANCH_NAME );
   }//End Method

   @Test public void shouldNotProvideJob(){
      assertThat( systemUnderTest.getJenkinsJob(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldProvideName(){
      assertThat( systemUnderTest.getName(), is( BRANCH_NAME ) );
   }//End Method
   
}//End Class
