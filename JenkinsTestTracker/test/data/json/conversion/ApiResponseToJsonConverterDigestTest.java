/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.conversion;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import core.category.Categories;
import core.lockdown.DigestMessageReceiver;
import core.lockdown.DigestMessageReceiverImpl;
import core.message.Messages;
import core.source.Source;
import core.source.SourceImpl;

/**
 * {@link ApiResponseToJsonConverterDigest} test.
 */
public class ApiResponseToJsonConverterDigestTest {

   @Mock private ApiResponseToJsonConverter converter;
   private Source expectedSource;
   @Mock private DigestMessageReceiver messageReceiver;
   private ApiResponseToJsonConverterDigest systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );

      expectedSource = new SourceImpl( converter, ApiResponseToJsonConverterDigest.NAME );
      new DigestMessageReceiverImpl( messageReceiver );
      
      systemUnderTest = new ApiResponseToJsonConverterDigest();
      systemUnderTest.attachSource( converter );
   }//End Method
   
   @Test public void shouldReportNullResponse() {
      systemUnderTest.reportNullApiResponse();
      verify( messageReceiver ).log( 
               expectedSource,
               Categories.error(),
               Messages.simpleMessage( ApiResponseToJsonConverterDigest.NULL_RESPONSE )
       );
   }//End Method
   
   @Test public void shouldReportInvalidResponse() {
      systemUnderTest.reportInvalidApiResponse();
      verify( messageReceiver ).log( 
               expectedSource,
               Categories.error(),
               Messages.simpleMessage( ApiResponseToJsonConverterDigest.INVALID_RESPONSE )
       );
   }//End Method

}//End Class
