/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;

public class BuildResultStatusChangeTest {

   private static final BuildResultStatus FROM = BuildResultStatus.FAILURE;
   private static final BuildResultStatus TO = BuildResultStatus.SUCCESS;
   
   private BuildResultStatusChange systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new BuildResultStatusChange( FROM, TO );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullPreviousState(){
      new BuildResultStatusChange( null, TO );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullCurrentState(){
      new BuildResultStatusChange( FROM, null );
   }//End Method

   @Test public void shouldProvideOriginalStatus() {
      assertThat( systemUnderTest.getPreviousStatus(), is( FROM ) );
   }//End Method
   
   @Test public void shouldProvideNewStatus() {
      assertThat( systemUnderTest.getCurrentStatus(), is( TO ) );
   }//End Method

   @Test public void shouldBeEqual(){
      assertThat( systemUnderTest, is( systemUnderTest ) );
      assertThat( new BuildResultStatusChange( FROM, TO ), is( systemUnderTest ) );
      assertThat( systemUnderTest, is( new BuildResultStatusChange( FROM, TO ) ) );
      assertThat( new BuildResultStatusChange( FROM, TO ), is( new BuildResultStatusChange( FROM, TO ) ) );
   }//End Method
   
   @Test public void shouldNotBeEqual(){
      assertThat( systemUnderTest.equals( null ), is( false ) );
      assertThat( systemUnderTest, is( not( new BuildResultStatusChange( FROM, BuildResultStatus.ABORTED ) ) ) );
      assertThat( new BuildResultStatusChange( FROM, BuildResultStatus.ABORTED ), is( not( systemUnderTest ) ) );
      assertThat( systemUnderTest, is( not( new BuildResultStatusChange( BuildResultStatus.ABORTED, TO ) ) ) );
      assertThat( new BuildResultStatusChange( BuildResultStatus.ABORTED, TO ), is( not( systemUnderTest ) ) );
      assertThat( systemUnderTest, is( not( "anything" ) ) );
   }//End Method
   
   @Test public void shouldHaveEqualHashcode(){
      assertThat( systemUnderTest.hashCode(), is( systemUnderTest.hashCode() ) );
      assertThat( new BuildResultStatusChange( FROM, TO ).hashCode(), is( systemUnderTest.hashCode() ) );
      assertThat( systemUnderTest.hashCode(), is( new BuildResultStatusChange( FROM, TO ).hashCode() ) );
      assertThat( new BuildResultStatusChange( FROM, TO ).hashCode(), is( new BuildResultStatusChange( FROM, TO ).hashCode() ) );
   }//End Method
   
   @Test public void shouldNotHaveEqualHashcode(){
      assertThat( systemUnderTest.hashCode(), is( not( new BuildResultStatusChange( FROM, BuildResultStatus.ABORTED ).hashCode() ) ) );
      assertThat( new BuildResultStatusChange( FROM, BuildResultStatus.ABORTED ).hashCode(), is( not( systemUnderTest.hashCode() ) ) );
      assertThat( systemUnderTest.hashCode(), is( not( new BuildResultStatusChange( BuildResultStatus.ABORTED, TO ).hashCode() ) ) );
      assertThat( new BuildResultStatusChange( BuildResultStatus.ABORTED, TO ).hashCode(), is( not( systemUnderTest.hashCode() ) ) );
   }//End Method
}//End Class
