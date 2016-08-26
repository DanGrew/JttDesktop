/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link UserAssignmentsTreeController} test.
 */
public class UserAssignmentsTreeControllerTest {

   private JenkinsDatabase database;
   @Mock private UserAssignmentsTreeLayout layout;
   private UserAssignmentsTreeController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      database.store( new JenkinsUserImpl( "First User" ) );
      systemUnderTest = new UserAssignmentsTreeController( layout, database );
   }//End Method
   
   @Test public void shouldAddUserWhenUserAddedToDatabase() {
      JenkinsUser user = new JenkinsUserImpl( "Another User" );
      database.store( user );
      verify( layout ).addBranch( user );
   }//End Method
   
   @Test public void shouldRemoveUserWhenUserRemoveFromDatabase() {
      JenkinsUser existing = database.jenkinsUsers().get( 0 );
      database.removeJenkinsUser( existing );
      verify( layout ).removeBranch( existing );
   }//End Method
   
   @Test public void shouldUpdateUserWhenUserNameChangesInDatabase() {
      JenkinsUser existing = database.jenkinsUsers().get( 0 );
      existing.nameProperty().set( "something else" );
      verify( layout ).updateBranch( existing );
   }//End Method

}//End Class
