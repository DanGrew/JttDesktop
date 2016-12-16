/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class JobBuiltResultTest {

   private JenkinsJob job;
   
   private static final BuildResultStatus FROM = BuildResultStatus.FAILURE;
   private static final BuildResultStatus TO = BuildResultStatus.SUCCESS;
   
   private JobBuiltResult systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      job = new JenkinsJobImpl( "Job" );
      systemUnderTest = new JobBuiltResult( job, FROM, TO );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullPreviousState(){
      new JobBuiltResult( job, null, TO );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullCurrentState(){
      new JobBuiltResult( job, FROM, null );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullJob(){
      new JobBuiltResult( job, null, TO );
   }//End Method

   @Test public void shouldProvideOriginalStatus() {
      assertThat( systemUnderTest.getPreviousStatus(), is( FROM ) );
   }//End Method
   
   @Test public void shouldProvideNewStatus() {
      assertThat( systemUnderTest.getCurrentStatus(), is( TO ) );
   }//End Method
   
   @Test public void shouldProvideJob() {
      assertThat( systemUnderTest.getJenkinsJob(), is( job ) );
   }//End Method

   @Test public void shouldBeEqual(){
      assertThat( systemUnderTest, is( systemUnderTest ) );
      assertThat( new JobBuiltResult( job, FROM, TO ), is( systemUnderTest ) );
      assertThat( systemUnderTest, is( new JobBuiltResult( job, FROM, TO ) ) );
      assertThat( new JobBuiltResult( job, FROM, TO ), is( new JobBuiltResult( job, FROM, TO ) ) );
   }//End Method
   
   @Test public void shouldNotBeEqual(){
      assertThat( systemUnderTest.equals( null ), is( false ) );
      assertThat( systemUnderTest, is( not( new JobBuiltResult( job, FROM, BuildResultStatus.ABORTED ) ) ) );
      assertThat( new JobBuiltResult( job, FROM, BuildResultStatus.ABORTED ), is( not( systemUnderTest ) ) );
      assertThat( systemUnderTest, is( not( new JobBuiltResult( job, BuildResultStatus.ABORTED, TO ) ) ) );
      assertThat( new JobBuiltResult( job, BuildResultStatus.ABORTED, TO ), is( not( systemUnderTest ) ) );
      assertThat( systemUnderTest, is( not( new JobBuiltResult( new JenkinsJobImpl( "Anything" ), FROM, TO ) ) ) );
      assertThat( new JobBuiltResult( new JenkinsJobImpl( "Anything" ), FROM, TO ), is( not( systemUnderTest ) ) );
      assertThat( systemUnderTest, is( not( "anything" ) ) );
   }//End Method
   
   @Test public void shouldHaveEqualHashcode(){
      assertThat( systemUnderTest.hashCode(), is( systemUnderTest.hashCode() ) );
      assertThat( new JobBuiltResult( job, FROM, TO ).hashCode(), is( systemUnderTest.hashCode() ) );
      assertThat( systemUnderTest.hashCode(), is( new JobBuiltResult( job, FROM, TO ).hashCode() ) );
      assertThat( new JobBuiltResult( job, FROM, TO ).hashCode(), is( new JobBuiltResult( job, FROM, TO ).hashCode() ) );
   }//End Method
   
   @Test public void shouldNotHaveEqualHashcode(){
      assertThat( systemUnderTest.hashCode(), is( not( new JobBuiltResult( job, FROM, BuildResultStatus.ABORTED ).hashCode() ) ) );
      assertThat( new JobBuiltResult( job, FROM, BuildResultStatus.ABORTED ).hashCode(), is( not( systemUnderTest.hashCode() ) ) );
      assertThat( systemUnderTest.hashCode(), is( not( new JobBuiltResult( job, BuildResultStatus.ABORTED, TO ).hashCode() ) ) );
      assertThat( new JobBuiltResult( job, BuildResultStatus.ABORTED, TO ).hashCode(), is( not( systemUnderTest.hashCode() ) ) );
      assertThat( systemUnderTest.hashCode(), is( not( new JobBuiltResult( new JenkinsJobImpl( "Anything" ), FROM, TO ).hashCode() ) ) );
      assertThat( new JobBuiltResult( new JenkinsJobImpl( "Anything" ), FROM, TO ).hashCode(), is( not( systemUnderTest.hashCode() ) ) );
   }//End Method
}//End Class
