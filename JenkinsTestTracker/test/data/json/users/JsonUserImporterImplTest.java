/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.users;

import static data.json.users.JsonUserImporterImpl.FULL_NAME_KEY;
import static data.json.users.JsonUserImporterImpl.USERS_KEY;
import static data.json.users.JsonUserImporterImpl.USER_KEY;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;

/**
 * {@link JsonUserImporterImpl} test.
 */
public class JsonUserImporterImplTest {
   
   private static final String FIRST_USER = "Dan Grew";
   private static final String SECOND_USER = "jenkins";
   private static final String THIRD_USER = "He who shall not be named";
   private static final String FOURTH_USER = "The Stig";
   private static final String FIFTH_USER = "Jeffrey";
   
   @Mock private JSONObject response;
   @Mock private JSONArray usersArray;
   
   @Mock private JSONObject firstUserWrapper;
   @Mock private JSONObject secondUserWrapper;
   @Mock private JSONObject thirdUserWrapper;
   @Mock private JSONObject fourthUserWrapper;
   @Mock private JSONObject fifthUserWrapper;
   
   @Mock private JSONObject firstUser;
   @Mock private JSONObject secondUser;
   @Mock private JSONObject thirdUser;
   @Mock private JSONObject fourthUser;
   @Mock private JSONObject fifthUser;
   
   @Mock private JSONException exception;
   
   @Mock private JsonUserImportHandler handler;
   private JsonUserImporter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JsonUserImporterImpl( handler );
      
      constructJsonResponseThatCanBeDynamicallyChangedDuringTest();
   }//End Method
   
   /**
    * Method to construct the {@link JSONObject} response for testing. The theory here is to mock out an entire
    * user import structure, then have individual cases break parts of it to test the import.
    */
   private void constructJsonResponseThatCanBeDynamicallyChangedDuringTest(){
      when( response.has( USERS_KEY ) ).thenReturn( true );
      when( response.getJSONArray( USERS_KEY ) ).thenReturn( usersArray );
      when( response.optJSONArray( USERS_KEY ) ).thenReturn( usersArray );
      
      when( usersArray.length() ).thenReturn( 5 );
      when( usersArray.getJSONObject( 0 ) ).thenReturn( firstUserWrapper );
      when( usersArray.getJSONObject( 1 ) ).thenReturn( secondUserWrapper );
      when( usersArray.getJSONObject( 2 ) ).thenReturn( thirdUserWrapper );
      when( usersArray.getJSONObject( 3 ) ).thenReturn( fourthUserWrapper );
      when( usersArray.getJSONObject( 4 ) ).thenReturn( fifthUserWrapper );
      
      whenHasGetOptObject( USER_KEY, firstUser, firstUserWrapper );
      whenHasGetOptObject( USER_KEY, secondUser, secondUserWrapper );
      whenHasGetOptObject( USER_KEY, thirdUser, thirdUserWrapper );
      whenHasGetOptObject( USER_KEY, fourthUser, fourthUserWrapper );
      whenHasGetOptObject( USER_KEY, fifthUser, fifthUserWrapper );
      
      whenHasGetOptString( FULL_NAME_KEY, FIRST_USER, firstUser );
      whenHasGetOptString( FULL_NAME_KEY, SECOND_USER, secondUser );
      whenHasGetOptString( FULL_NAME_KEY, THIRD_USER, thirdUser );
      whenHasGetOptString( FULL_NAME_KEY, FOURTH_USER, fourthUser );
      whenHasGetOptString( FULL_NAME_KEY, FIFTH_USER, fifthUser );
   }//End Method
   
   /**
    * Method to mock out the return result for a particular {@link String} value.
    * @param key the key in the data.
    * @param value the value to provide.
    * @param object the {@link JSONObject} to when on.
    */
   private void whenHasGetOptString( String key, String value, JSONObject object ) {
      when( object.has( key ) ).thenReturn( true );
      when( object.getString( key ) ).thenReturn( value );
      when( object.optString( key ) ).thenReturn( value );
   }//End Method
   
   /**
    * Method to mock out the return result for a particular {@link JSONObject} value.
    * @param key the key in the data.
    * @param value the value to provide.
    * @param object the {@link JSONObject} to when on.
    */
   private void whenHasGetOptObject( String key, JSONObject value, JSONObject object ) {
      when( object.has( key ) ).thenReturn( true );
      when( object.getJSONObject( key ) ).thenReturn( value );
      when( object.optJSONObject( key ) ).thenReturn( value );
   }//End Method
   
   @Test public void systemConstructorShouldConstruct(){
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest = new JsonUserImporterImpl( database );
      systemUnderTest.importUsers( new JSONObject() );
   }//End Method
   
   @Test public void shouldParseUsersList(){
      assertUsersImported( new ArrayList<>() );
   }//End Method
   
   @Test public void shouldParseUsersListWithSystemConstructor(){
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest = new JsonUserImporterImpl( database );
      systemUnderTest.importUsers( response );
      assertThat( database.jenkinsUsers(), hasSize( 5 ) );
   }//End Method
   
   @Test public void shouldNotImportWhenUsersAreNotPresent(){
      when( response.has( USERS_KEY ) ).thenReturn( false );
      when( response.optJSONArray( USERS_KEY ) ).thenReturn( null );
      when( response.getJSONArray( USERS_KEY ) ).thenThrow( new JSONException( "" ) );
      
      assertUsersImported( Arrays.asList( 0, 1, 2, 3, 4 ) );
   }//End Method
   
   @Test public void shouldNotImportWhenUsersAreEmpty(){
      when( response.has( USERS_KEY ) ).thenReturn( false );
      when( response.optJSONArray( USERS_KEY ) ).thenReturn( new JSONArray() );
      when( response.getJSONArray( USERS_KEY ) ).thenThrow( new JSONException( "" ) );
      
      assertUsersImported( Arrays.asList( 0, 1, 2, 3, 4 ) );
   }//End Method
   
   @Test public void shouldNotImportWhenUsersAreThoughtButNotPresent(){
      when( response.has( USERS_KEY ) ).thenReturn( true );
      when( response.optJSONArray( USERS_KEY ) ).thenReturn( null );
      when( response.getJSONArray( USERS_KEY ) ).thenThrow( exception );
      
      assertUsersImported( Arrays.asList( 0, 1, 2, 3, 4 ) );
   }//End Method
   
   @Test public void shouldNotImportWhenUsersArePresentButNoElements(){
      when( usersArray.length() ).thenReturn( 0 );
      for ( int i = 0; i < 5; i++ ) {
         when( usersArray.get( i ) ).thenReturn( null );
      }
      
      assertUsersImported( Arrays.asList( 0, 1, 2, 3, 4 ) );
   }//End Method
   
   @Test public void shouldNotImportSecondWhenUserKeyNotPresent(){
      when( secondUserWrapper.has( USER_KEY ) ).thenReturn( false );
      when( secondUserWrapper.optJSONObject( USER_KEY ) ).thenReturn( null );
      when( secondUserWrapper.getJSONObject( USER_KEY ) ).thenThrow( exception );
      
      assertUsersImported( Arrays.asList( 1 ) );
   }//End Method
   
   @Test public void shouldNotImportThirdWhenUserFullNameNotPresent(){
      when( thirdUser.has( FULL_NAME_KEY ) ).thenReturn( false );
      when( thirdUser.optString( FULL_NAME_KEY ) ).thenReturn( null );
      when( thirdUser.getString( FULL_NAME_KEY ) ).thenThrow( exception );
      
      assertUsersImported( Arrays.asList( 2 ) );
   }//End Method
   
   @Test public void shouldNotImportThirdWhenUserFullNameNotValid(){
      when( thirdUser.has( FULL_NAME_KEY ) ).thenReturn( true );
      when( thirdUser.optString( FULL_NAME_KEY ) ).thenReturn( null );
      when( thirdUser.getString( FULL_NAME_KEY ) ).thenThrow( exception );
      
      assertUsersImported( Arrays.asList( 2 ) );
   }//End Method
   
   /**
    * Method to assert that the expected {@link JenkinsUser}s have been imported.
    * @param response the response from the {@link ExternalApi}.
    * @param missingUsers the user number to exclude.
    */
   private void assertUsersImported( List< Integer > missingUsers ) {
      List< String > expected = new ArrayList<>();
      if ( !missingUsers.contains( 0 ) ) {
         expected.add( FIRST_USER );
      }
      if ( !missingUsers.contains( 1 ) ) {
         expected.add( SECOND_USER );
      }
      if ( !missingUsers.contains( 2 ) ) {
         expected.add( THIRD_USER );
      } 
      if ( !missingUsers.contains( 3 ) ) {
         expected.add( FOURTH_USER );
      }
      if ( !missingUsers.contains( 4 ) ) {
         expected.add( FIFTH_USER );
      }
      
      systemUnderTest.importUsers( response );
      for ( String expectedUser : expected ) {
         verify( handler ).userFound( expectedUser );
      }
   }//End Method
   
}//End Class
