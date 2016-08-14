/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

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

/**
 * {@link ChangeIdentifier} test.
 */
public class ChangeIdentifierTest {

   private ChangeIdentifier systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new ChangeIdentifier();
   }//End Method
   
   @Test public void shouldIdentifyChangeType(){
      assertThat( systemUnderTest.identifyChangeType( ABORTED, ABORTED ), is( BuildResultStatusChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, FAILURE ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, NOT_BUILT ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, SUCCESS ), is( BuildResultStatusChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, UNKNOWN ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( ABORTED, UNSTABLE ), is( BuildResultStatusChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( FAILURE, ABORTED ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, FAILURE ), is( BuildResultStatusChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, NOT_BUILT ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, SUCCESS ), is( BuildResultStatusChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, UNKNOWN ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( FAILURE, UNSTABLE ), is( BuildResultStatusChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, ABORTED ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, FAILURE ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, NOT_BUILT ), is( BuildResultStatusChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, SUCCESS ), is( BuildResultStatusChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, UNKNOWN ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( NOT_BUILT, UNSTABLE ), is( BuildResultStatusChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, ABORTED ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, FAILURE ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, NOT_BUILT ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, SUCCESS ), is( BuildResultStatusChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, UNKNOWN ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( SUCCESS, UNSTABLE ), is( BuildResultStatusChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, ABORTED ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, FAILURE ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, NOT_BUILT ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, SUCCESS ), is( BuildResultStatusChange.Passed ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, UNKNOWN ), is( BuildResultStatusChange.Unchanged ) );
      assertThat( systemUnderTest.identifyChangeType( UNKNOWN, UNSTABLE ), is( BuildResultStatusChange.ActionRequired ) );
      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, ABORTED ), is( BuildResultStatusChange.ActionRequired ) );
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, FAILURE ), is( BuildResultStatusChange.ActionRequired ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, NOT_BUILT ), is( BuildResultStatusChange.ActionRequired ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, SUCCESS ), is( BuildResultStatusChange.Passed ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, UNKNOWN ), is( BuildResultStatusChange.ActionRequired ) );      
      assertThat( systemUnderTest.identifyChangeType( UNSTABLE, UNSTABLE ), is( BuildResultStatusChange.Unchanged ) );      
   }//End Method

}//End Class
