/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import core.category.Categories;
import core.category.Category;
import core.lockdown.DigestManager;
import core.lockdown.DigestMessageReceiver;
import core.lockdown.DigestMessageReceiverImpl;
import core.lockdown.DigestProgressReceiver;
import core.lockdown.DigestProgressReceiverImpl;
import core.message.Message;
import core.progress.Progress;
import core.progress.Progresses;
import core.source.Source;

/**
 * {@link JenkinsLoginDigest} test.
 */
public class JenkinsLoginDigestTest {

   @Mock private DigestMessageReceiver messageReceiver;
   @Mock private DigestProgressReceiver progressReceiver;
   @Captor private ArgumentCaptor< Source > sourceCaptor;
   @Captor private ArgumentCaptor< Category > categoryCaptor;
   @Captor private ArgumentCaptor< Progress > progressCaptor;
   @Captor private ArgumentCaptor< Message > messageCaptor;
   private JenkinsLoginDigest systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      DigestManager.reset();
      MockitoAnnotations.initMocks( this );
      new DigestMessageReceiverImpl( messageReceiver );
      new DigestProgressReceiverImpl( progressReceiver );
      systemUnderTest = new JenkinsLoginDigest();
      systemUnderTest.attachSource( mock( JenkinsLogin.class ) );
   }//End Method
   
   @Test public void shouldProvideConstantName() {
      systemUnderTest.log( mock( Category.class ), mock( Message.class ) );
      verify( messageReceiver ).log( sourceCaptor.capture(), Mockito.any(), Mockito.any() );
      assertThat( sourceCaptor.getValue().getIdentifier(), is( JenkinsLoginDigest.JENKINS_LOGIN_NAME ) );
      
      systemUnderTest.progress( mock( Progress.class ), mock( Message.class ) );
      verify( progressReceiver ).progress( sourceCaptor.capture(), Mockito.any(), Mockito.any() );
      assertThat( sourceCaptor.getValue().getIdentifier(), is( JenkinsLoginDigest.JENKINS_LOGIN_NAME ) );
   }//End Method
   
   @Test public void shouldResetToAwaitingLoginProgress(){
      systemUnderTest.resetLoginProgress();
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      
      assertThat( progressCaptor.getValue().getPercentage(), is( 0.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.WAITING_FOR_LOG_IN ) );
   }//End Method
   
   @Test public void shouldResetProgressWhenValidationErrorLogged(){
      final String error = "some error found";
      systemUnderTest.validationError( error );
      
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 0.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.WAITING_FOR_LOG_IN ) );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.error() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( error ) );
      
      InOrder order = inOrder( messageReceiver, progressReceiver );
      order.verify( progressReceiver ).progress( Mockito.any(), Mockito.any(), Mockito.any() );
      order.verify( messageReceiver ).log( Mockito.any(),  Mockito.any(),  Mockito.any() );
   }//End Method
   
   @Test public void shouldAcceptCredentialInput(){
      systemUnderTest.acceptCredentials();
      
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( JenkinsLoginDigest.CREDENTIALS_FORMAT_ACCEPTED_PROGRESS ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.CONNECTING_TO_JENKINS ) );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.CREDENTIALS_FORMAT_ACCEPTED ) );
      
      InOrder order = inOrder( messageReceiver, progressReceiver );
      order.verify( messageReceiver ).log( Mockito.any(),  Mockito.any(),  Mockito.any() );
      order.verify( progressReceiver ).progress( Mockito.any(), Mockito.any(), Mockito.any() );
   }//End Method

   @Test public void shouldFailLogin(){
      systemUnderTest.loginFailed();
      
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 0.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.WAITING_FOR_LOG_IN ) );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.LOG_IN_FAILED ) );
      
      InOrder order = inOrder( messageReceiver, progressReceiver );
      order.verify( progressReceiver ).progress( Mockito.any(), Mockito.any(), Mockito.any() );
      order.verify( messageReceiver ).log( Mockito.any(),  Mockito.any(),  Mockito.any() );
   }//End Method
   
   @Test public void shouldLoginSuccessfully(){
      systemUnderTest.loginSuccessful();
      
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue(), is( Progresses.complete() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.LOGIN_PROCESS_HAS_COMPLETED ) );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.SUCCESSFULLY_LOGGED_IN ) );
      
      InOrder order = inOrder( messageReceiver, progressReceiver );
      order.verify( progressReceiver ).progress( Mockito.any(), Mockito.any(), Mockito.any() );
      order.verify( messageReceiver ).log( Mockito.any(),  Mockito.any(),  Mockito.any() );
   }//End Method
   
   @Test public void shouldTimeoutLogin(){
      systemUnderTest.loginTimedOut();
      
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 0.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.WAITING_FOR_LOG_IN ) );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLoginDigest.LOGIN_PROCESS_TIMED_OUT ) );
      
      InOrder order = inOrder( messageReceiver, progressReceiver );
      order.verify( progressReceiver ).progress( Mockito.any(), Mockito.any(), Mockito.any() );
      order.verify( messageReceiver ).log( Mockito.any(),  Mockito.any(),  Mockito.any() );
   }//End Method
}//End Class
