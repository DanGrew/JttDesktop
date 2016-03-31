/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import api.sources.ExternalApi;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;

/**
 * {@link JsonUserImporterImpl} test.
 */
public class JsonUserImporterImplTest {
   
   private JenkinsDatabase database;
   private JsonUserImporter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new JsonUserImporterImpl( database );
   }//End Method
   
   @Test public void shouldParseUsersList(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list.json" );
      
      assertUsersImported( response, new ArrayList<>() );
   }//End Method
   
   @Test public void shouldIgnoreEmptyUsersList(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list-empty-users.json" );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest.importUsers( response );
      Assert.assertTrue( database.hasNoJenkinsUsers() );
      Assert.assertTrue( database.jenkinsUsers().isEmpty() );
   }//End Method
   
   @Test public void shouldIgnoreInvalidUserNameInUserList(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list-invalid-full-name-value.json" );
      
      assertUsersImported( response, Arrays.asList( 0 ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingUsersInUserList(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list-missing-users.json" );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest.importUsers( response );
      Assert.assertTrue( database.hasNoJenkinsUsers() );
      Assert.assertTrue( database.jenkinsUsers().isEmpty() );
   }//End Method
   
   @Test public void shouldIgnoreMissingFullNameKeyInUserList(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list-missing-full-name-key.json" );
      
      assertUsersImported( response, Arrays.asList( 1 ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingFullNameValueInUserList(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list-missing-full-name-value.json" );
      
      assertUsersImported( response, Arrays.asList( 3 ) );
   }//End Method
   
   @Test public void shouldOnlyImportNewUsers(){
      database.store( new JenkinsUserImpl( "Dan Grew" ) );
      database.store( new JenkinsUserImpl( "jenkins" ) );
      
      String response = TestCommon.readFileIntoString( getClass(), "users-list.json" );
      
      assertUsersImported( response, new ArrayList<>() );
   }//End Method
   
   /**
    * Method to assert that the expected {@link JenkinsUser}s have been imported.
    * @param response the response from the {@link ExternalApi}.
    * @param missingUsers the user number to exclude.
    */
   private void assertUsersImported( String response, List< Integer > missingUsers ) {
      List< String > expected = new ArrayList<>();
      if ( !missingUsers.contains( 0 ) ) {
         expected.add( "Dan Grew" );
      }
      if ( !missingUsers.contains( 1 ) ) {
         expected.add( "jenkins" );
      }
      if ( !missingUsers.contains( 2 ) ) {
         expected.add( "He who shall not be named" );
      } 
      if ( !missingUsers.contains( 3 ) ) {
         expected.add( "The Stig" );
      }
      if ( !missingUsers.contains( 4 ) ) {
         expected.add( "Jeffrey" );
      }
      
      systemUnderTest.importUsers( response );
      Assert.assertEquals( 5 - missingUsers.size(), database.jenkinsUsers().size() );
      for ( int i = 0; i < 5 - missingUsers.size(); i ++ ) {
         Assert.assertEquals( expected.get( i ), database.jenkinsUsers().get( i ).nameProperty().get() );
      }
   }//End Method
   
   @Test public void shouldIgnoreNullDatabaseInJobList(){
      systemUnderTest = new JsonUserImporterImpl( null );
      systemUnderTest.importUsers( "anything" );
   }//End Method
   
   @Test public void shouldIgnoreNullResponseInJobList(){
      systemUnderTest.importUsers( null );
   }//End Method
   
   @Test public void shouldIgnoreMissingButValidData(){
      systemUnderTest.importUsers( "{ }" );
   }//End Method
}//End Class
