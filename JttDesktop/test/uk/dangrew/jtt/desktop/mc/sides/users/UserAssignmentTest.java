/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link UserAssignment} test.
 */
public class UserAssignmentTest {

   private static final long TIMESTAMP = 1000;
   private static final String DESCRIPTION = "some special description";
   private static final String DETAIL = "really detail explanation";
   
   private JenkinsUser user;
   private UserAssignment systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      user = new JenkinsUserImpl( "Dan" );
      systemUnderTest = new UserAssignment( user, TIMESTAMP, DESCRIPTION, DETAIL );
   }//End Method
   
   @Test public void shouldProvideUserGiven() {
      assertThat( systemUnderTest.getJenkinsUser(), is( user ) );
   }//End Method
   
   @Test public void shouldConstructAssignmentItem(){
      UserAssignmentsTreeItem item = systemUnderTest.constructTreeItem();
      assertThat( item, is( instanceOf( AssignedTreeItem.class ) ) );
      assertThat( item.getJenkinsUser(), is( user ) );
   }//End Method
   
   @Test public void shouldProvideDescriptionProperty(){
      assertThat( systemUnderTest.descriptionProperty().get(), is( DESCRIPTION ) );
   }//End Method
   
   @Test public void shouldProvideDetailProperty(){
      assertThat( systemUnderTest.detailProperty().get(), is( DETAIL ) );
   }//End Method
   
   @Test public void shouldProvideTimestampProperty(){
      assertThat( systemUnderTest.timestampProperty().get(), is( TIMESTAMP ) );
   }//End Method
   
}//End Class
