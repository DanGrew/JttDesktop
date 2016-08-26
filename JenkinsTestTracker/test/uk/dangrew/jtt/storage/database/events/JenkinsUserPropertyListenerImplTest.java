/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.util.Pair;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link JenkinsUserPropertyListener} test.
 */
public class JenkinsUserPropertyListenerImplTest {

   private JenkinsDatabase databse;
   private JenkinsUser user1;
   private JenkinsUser user2;
   private JenkinsUser user3;
   
   private JenkinsUserPropertyListener systemUnderTest;
   private List< Pair< JenkinsUser, String > > nameNotifications;
   private JttChangeListener< JenkinsUser, String > nameListener;
   
   @Before public void initialiseSystemUnderTest(){
      databse = new JenkinsDatabaseImpl();
      user1 = new JenkinsUserImpl( "first  user" );
      user2 = new JenkinsUserImpl( "second user" );
      user3 = new JenkinsUserImpl( "third  user" );
      databse.store( user1 );
      databse.store( user2 );
      databse.store( user3 );
      
      systemUnderTest = new JenkinsUserPropertyListener( databse );
      nameNotifications = new ArrayList<>();
      nameListener = ( user, old, updated ) -> nameNotifications.add( new Pair< JenkinsUser, String >( user, updated ) );
      systemUnderTest.addNamePropertyListener( nameListener );
   }//End Method
   
   @Test public void shouldNotifyNameWhenChanged() {
      assertThat( nameNotifications.isEmpty(), is( true ) );
      user1.nameProperty().set( "anything" );
      
      assertThat( nameNotifications.isEmpty(), is( false ) );
      Pair< JenkinsUser, String > result = nameNotifications.remove( 0 );
      assertThat( result.getKey(), is( user1 ) );
      assertThat( result.getValue(), is( "anything" ) );
      assertThat( nameNotifications.isEmpty(), is( true ) );
   }//End Method
   
}//End Class
