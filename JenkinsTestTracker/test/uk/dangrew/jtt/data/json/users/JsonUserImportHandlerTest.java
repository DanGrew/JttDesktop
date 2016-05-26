/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.users;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.data.json.users.JsonUserImportHandler;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * {@link JsonUserImportHandler} test.
 */
public class JsonUserImportHandlerTest {

   @Mock private JenkinsDatabase database;
   @Captor private ArgumentCaptor< JenkinsUser > userCaptor;
   private JsonUserImportHandler systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JsonUserImportHandler( database );
   }//End Method
   
   @Test public void shouldStoreUserInDatabaseIfNotPresent() {
      final String userName = "some user"; 
      systemUnderTest.userFound( userName );
      
      verify( database ).store( userCaptor.capture() );
      assertThat( userCaptor.getValue().nameProperty().get(), is( userName ) );
   }//End Method
   
   @Test public void shouldNotStoreUserIfAlreadyPresent() {
      final String existingUser = "some user";
      when( database.hasJenkinsUser( existingUser ) ).thenReturn( true );
      
      systemUnderTest.userFound( existingUser );
      verify( database, times( 0 ) ).store( Mockito.< JenkinsUser >any() );
   }//End Method

   @Test public void shouldNotStoreUserIfNameInvalid() {
      final String existingUser = "   ";
      
      systemUnderTest.userFound( existingUser );
      verify( database, times( 0 ) ).store( Mockito.< JenkinsUser >any() );
   }//End Method
   
   @Test public void shuldIgnoreNullName(){
      systemUnderTest.userFound( null );
      verify( database, times( 0 ) ).store( Mockito.< JenkinsUser >any() );
   }//End Method
   
}//End Class
