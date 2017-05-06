/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.dangrew.jtt.model.jobs.BuildResultStatus.ABORTED;
import static uk.dangrew.jtt.model.jobs.BuildResultStatus.FAILURE;
import static uk.dangrew.jtt.model.jobs.BuildResultStatus.NOT_BUILT;
import static uk.dangrew.jtt.model.jobs.BuildResultStatus.SUCCESS;
import static uk.dangrew.jtt.model.jobs.BuildResultStatus.UNKNOWN;
import static uk.dangrew.jtt.model.jobs.BuildResultStatus.UNSTABLE;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusHighLevelChange;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.ChangeIdentifier;

/**
 * {@link ChangeIdentifier} test.
 */
public class ChangeIdentifierTest {

   private ChangeIdentifier systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new ChangeIdentifier();
   }//End Method
   
   @Test public void shouldIdentifyChangeType(){
      assertThat( systemUnderTest.identifyChangeType( ABORTED, ABORTED ), is( BuildResultStatusHighLevelChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, FAILURE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, NOT_BUILT ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, SUCCESS ), is( BuildResultStatusHighLevelChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, UNKNOWN ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, UNSTABLE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( FAILURE, ABORTED ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, FAILURE ), is( BuildResultStatusHighLevelChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, NOT_BUILT ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, SUCCESS ), is( BuildResultStatusHighLevelChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, UNKNOWN ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, UNSTABLE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, ABORTED ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, FAILURE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, NOT_BUILT ), is( BuildResultStatusHighLevelChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, SUCCESS ), is( BuildResultStatusHighLevelChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, UNKNOWN ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, UNSTABLE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, ABORTED ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, FAILURE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, NOT_BUILT ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, SUCCESS ), is( BuildResultStatusHighLevelChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, UNKNOWN ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, UNSTABLE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, ABORTED ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, FAILURE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, NOT_BUILT ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, SUCCESS ), is( BuildResultStatusHighLevelChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, UNKNOWN ), is( BuildResultStatusHighLevelChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, UNSTABLE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, ABORTED ), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, FAILURE ), is( BuildResultStatusHighLevelChange.ActionRequired ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, NOT_BUILT ), is( BuildResultStatusHighLevelChange.ActionRequired ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, SUCCESS ), is( BuildResultStatusHighLevelChange.Passed ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, UNKNOWN ), is( BuildResultStatusHighLevelChange.ActionRequired ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, UNSTABLE ), is( BuildResultStatusHighLevelChange.Unchanged ) );      
   }//End Method

}//End Class
