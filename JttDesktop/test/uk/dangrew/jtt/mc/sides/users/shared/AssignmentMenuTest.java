/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users.shared;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link AssignmentMenu} test.
 */
public class AssignmentMenuTest {
   
   private JenkinsDatabase database;
   @Mock private Consumer< JenkinsUser > notifier;
   private AssignmentMenu systemUnderTest;

   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      database = new TestJenkinsDatabaseImpl();
      database.store( new JenkinsUserImpl( "lizzybuff" ) );
      database.store( new JenkinsUserImpl( "jeffrey" ) );
      database.store( new JenkinsUserImpl( "Dan" ) );
      database.store( new JenkinsUserImpl( "Liz" ) );
      database.store( new JenkinsUserImpl( "Jeffrey" ) );
      systemUnderTest = new AssignmentMenu( database, notifier );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new AssignmentMenu( null, notifier );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullNotifier(){
      new AssignmentMenu( database, null );
   }//End Method

   @Test public void shouldProvideNameOnMenu(){
      assertThat( systemUnderTest.getText(), is( AssignmentMenu.ASSIGN ) );
   }//End Method
   
   @Test public void shouldHaveMenusForAllUsersInDatabase() {
      assertAllUsersPresentInMenu();
   }//End Method
   
   private void assertAllUsersPresentInMenu() {
      assertThat( systemUnderTest.getItems(), hasSize( database.jenkinsUsers().size() ) );
      
      List< JenkinsUser > orderedUsers = new ArrayList<>();
      orderedUsers.addAll( database.jenkinsUsers() );
      orderedUsers.sort( ( a, b ) -> a.nameProperty().get().compareToIgnoreCase( b.nameProperty().get() ) );
      
      for ( int i = 0; i < database.jenkinsUsers().size(); i++ ) {
         assertThat( systemUnderTest.getItems().get( i ).getText(), is( orderedUsers.get( i ).nameProperty().get() ) );
      }
   }//End Method
   
   @Test public void shouldAddUserMenuWhenAddedToDatabase() {
      database.store( new JenkinsUserImpl( "Anything" ) );
      assertAllUsersPresentInMenu();
   }//End Method
   
   @Test public void shouldRemoveUserMenuWhenRemovedFromDatabase() {
      database.removeJenkinsUser( database.jenkinsUsers().get( 1 ) );
      assertAllUsersPresentInMenu();
   }//End Method
   
   @Test public void shouldUpdateUserMenuWhenUpdatedInDatabase() {
      database.jenkinsUsers().get( 0 ).nameProperty().set( "something else" );
      assertAllUsersPresentInMenu();
   }//End Method
   
   @Test public void shouldRaiseSingleEventForSingleSelection() {
      systemUnderTest.getItems().get( 0 ).fire();
      verify( notifier ).accept( database.jenkinsUsers().get( 2 ) );
   }//End Method
   
}//End Class
