/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.conversion;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.data.json.conversion.ApiResponseToJsonConverterDigest;

/**
 * {@link ApiResponseToJsonConverter} test.
 */
public class ApiResponseToJsonConverterTest {
   
   @Mock private ApiResponseToJsonConverterDigest digest;
   private ApiResponseToJsonConverter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new ApiResponseToJsonConverter( digest );
   }//End Method

   @Test public void shouldAttachToDigest(){
      verify( digest ).attachSource( systemUnderTest );
   }//End Method
   
   @Test public void shouldAcceptValidJson() {
      String input = "{ anyProperty: anyValue }";
      
      JSONObject object = systemUnderTest.convert( input );
      assertThat( object, is( notNullValue() ) );
      
      assertThat( object.has( "anyProperty" ), is( true ) );
      assertThat( object.getString( "anyProperty" ), is( "anyValue" ) );
      
      verify( digest, times( 0 ) ).reportInvalidApiResponse();
      verify( digest, times( 0 ) ).reportNullApiResponse();
   }//End Method
   
   @Test public void shouldAcceptNullResponse() {
      assertThat( systemUnderTest.convert( null ), is( nullValue() ) );
      verify( digest ).reportNullApiResponse();
   }//End Method
   
   @Test public void shouldAcceptInvalidJson() {
      assertThat( systemUnderTest.convert( "any rubbish" ), is( nullValue() ) );
      verify( digest ).reportInvalidApiResponse();
   }//End Method
   
   @Test public void shouldNotReportAnIssueWithAnOptionalResponseThatIsNull(){
      assertThat( systemUnderTest.optionalConvert( null ), is( nullValue() ) );
      verify( digest, times( 0 ) ).reportNullApiResponse();
   }//End Method
   
   @Test public void shouldReportAnIssueWithAnOptionalResponseThatIsInvalid(){
      assertThat( systemUnderTest.optionalConvert( "any rubbish" ), is( nullValue() ) );
      verify( digest, times( 0 ) ).reportInvalidApiResponse();  
   }//End Method

}//End Class
