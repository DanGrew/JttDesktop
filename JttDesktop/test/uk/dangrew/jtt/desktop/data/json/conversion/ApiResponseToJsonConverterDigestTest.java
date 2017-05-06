/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.data.json.conversion;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.desktop.data.json.conversion.ApiResponseToJsonConverterDigest;
import uk.dangrew.sd.core.category.Categories;
import uk.dangrew.sd.core.lockdown.DigestMessageReceiver;
import uk.dangrew.sd.core.lockdown.DigestMessageReceiverImpl;
import uk.dangrew.sd.core.message.Messages;
import uk.dangrew.sd.core.source.Source;
import uk.dangrew.sd.core.source.SourceImpl;

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
               Mockito.any(), 
               Mockito.eq( expectedSource ),
               Mockito.eq( Categories.error() ),
               Mockito.eq( Messages.simpleMessage( ApiResponseToJsonConverterDigest.NULL_RESPONSE ) )
       );
   }//End Method
   
   @Test public void shouldReportInvalidResponse() {
      systemUnderTest.reportInvalidApiResponse();
      verify( messageReceiver ).log(
               Mockito.any(), 
               Mockito.eq( expectedSource ),
               Mockito.eq( Categories.error() ),
               Mockito.eq( Messages.simpleMessage( ApiResponseToJsonConverterDigest.INVALID_RESPONSE ) )
       );
   }//End Method

}//End Class
