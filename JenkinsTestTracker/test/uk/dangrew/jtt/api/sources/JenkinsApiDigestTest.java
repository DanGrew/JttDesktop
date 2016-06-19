/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.sd.core.category.Categories;
import uk.dangrew.sd.core.category.Category;
import uk.dangrew.sd.core.lockdown.DigestMessageReceiver;
import uk.dangrew.sd.core.lockdown.DigestMessageReceiverImpl;
import uk.dangrew.sd.core.lockdown.DigestProgressReceiver;
import uk.dangrew.sd.core.lockdown.DigestProgressReceiverImpl;
import uk.dangrew.sd.core.message.Message;
import uk.dangrew.sd.core.progress.Progress;
import uk.dangrew.sd.core.source.Source;

/**
 * {@link JenkinsApiDigest} test.
 */
public class JenkinsApiDigestTest {

   @Mock private DigestMessageReceiver messageReceiver;
   @Mock private DigestProgressReceiver progressReceiver;
   @Captor private ArgumentCaptor< Source > sourceCaptor;
   @Captor private ArgumentCaptor< Category > categoryCaptor;
   @Captor private ArgumentCaptor< Message > messageCaptor;
   private JenkinsApiDigest systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      new DigestMessageReceiverImpl( messageReceiver );
      new DigestProgressReceiverImpl( progressReceiver );
      systemUnderTest = new JenkinsApiDigest();
      systemUnderTest.attachSource( mock( JenkinsApiImpl.class ) );
   }//End Method
   
   @Test public void shouldProvideConstantName() {
      systemUnderTest.log( mock( Category.class ), mock( Message.class ) );
      verify( messageReceiver ).log( Mockito.any(), sourceCaptor.capture(), Mockito.any(), Mockito.any() );
      assertThat( sourceCaptor.getValue().getIdentifier(), is( JenkinsApiDigest.JENKINS_API_CONNECTION ) );
      
      systemUnderTest.progress( mock( Progress.class ), mock( Message.class ) );
      verify( progressReceiver ).progress( sourceCaptor.capture(), Mockito.any(), Mockito.any() );
      assertThat( sourceCaptor.getValue().getIdentifier(), is( JenkinsApiDigest.JENKINS_API_CONNECTION ) );
   }//End Method
   
   @Test public void shouldHandleExecuteLoginRequest(){
      systemUnderTest.executingLoginRequest();
      verify( messageReceiver ).log( Mockito.any(), Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      
      assertThat( categoryCaptor.getValue(), is( Categories.processingSequence() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsApiDigest.EXECUTING_BASE_LOGIN_REQUEST ) );
   }//End Method
   
   @Test public void shouldHandleConnectionFailed(){
      systemUnderTest.connectionFailed();
      verify( messageReceiver ).log( Mockito.any(), Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      
      assertThat( categoryCaptor.getValue(), is( Categories.error() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsApiDigest.CLIENT_FAILED_TO_CONNECT ) );
   }//End Method
   
   @Test public void shouldHandleConnectionSuccess(){
      systemUnderTest.connectionSuccess();
      verify( messageReceiver ).log( Mockito.any(), Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsApiDigest.CLIENT_CONNECTED ) );
   }//End Method
   
   @Test public void shouldHandleConnectionException(){
      final Exception exception = mock( Exception.class );
      final String exceptionMessage = "any message from an exception";
      Mockito.when( exception.getMessage() ).thenReturn( exceptionMessage );
      
      systemUnderTest.connectionException( exception );
      verify( messageReceiver ).log( Mockito.any(), Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      
      assertThat( categoryCaptor.getValue(), is( Categories.error() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( exception.getMessage() ) );
   }//End Method
   
   @Test public void shouldHandleHandlingResponse(){
      systemUnderTest.handlingResponse();
      verify( messageReceiver ).log( Mockito.any(), Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      
      assertThat( categoryCaptor.getValue(), is( Categories.processingSequence() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsApiDigest.HANDLING_RESPONSE_FOR_REQUEST ) );
   }//End Method
   
   @Test public void shouldHandleResponseReady(){
      systemUnderTest.responseReady();
      verify( messageReceiver ).log( Mockito.any(), Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      
      assertThat( categoryCaptor.getValue(), is( Categories.processingSequence() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsApiDigest.RESPONSE_READY ) );
   }//End Method
}//End Class
