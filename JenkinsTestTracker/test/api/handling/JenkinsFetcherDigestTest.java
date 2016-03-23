/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import core.category.Categories;
import core.category.Category;
import core.lockdown.DigestMessageReceiver;
import core.lockdown.DigestMessageReceiverImpl;
import core.lockdown.DigestProgressReceiver;
import core.lockdown.DigestProgressReceiverImpl;
import core.message.Message;
import core.progress.Progress;
import core.progress.Progresses;
import core.source.Source;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;

/**
 * {@link JenkinsFetcherDigest} test.
 */
public class JenkinsFetcherDigestTest {

   private static final String SAMPLE_TEST_VALUE = "something very specific";
   
   @Mock private DigestMessageReceiver messageReceiver;
   @Mock private DigestProgressReceiver progressReceiver;
   
   @Captor private ArgumentCaptor< Source > sourceCaptor;
   @Captor private ArgumentCaptor< Category > categoryCaptor;
   @Captor private ArgumentCaptor< Progress > progressCaptor;
   @Captor private ArgumentCaptor< Message > messageCaptor;
   
   private JenkinsJob job;
   
   private JenkinsFetcherDigest systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      new DigestMessageReceiverImpl( messageReceiver );
      new DigestProgressReceiverImpl( progressReceiver );
      
      job = new JenkinsJobImpl( "some jenkins job" );
      
      systemUnderTest = new JenkinsFetcherDigest();
      systemUnderTest.attachSource( mock( JenkinsFetcherImpl.class ) );
   }//End Method
   
   @Test public void shouldProvideConstantName() {
      systemUnderTest.log( mock( Category.class ), mock( Message.class ) );
      verify( messageReceiver ).log( sourceCaptor.capture(), Mockito.any(), Mockito.any() );
      assertThat( sourceCaptor.getValue().getIdentifier(), is( JenkinsFetcherDigest.JENKINS_FETCHER_NAME ) );
      
      systemUnderTest.progress( mock( Progress.class ), mock( Message.class ) );
      verify( progressReceiver ).progress( sourceCaptor.capture(), Mockito.any(), Mockito.any() );
      assertThat( sourceCaptor.getValue().getIdentifier(), is( JenkinsFetcherDigest.JENKINS_FETCHER_NAME ) );
   }//End Method
   
   @Test public void shouldFetchForJob(){
      systemUnderTest.fetching( SAMPLE_TEST_VALUE, job );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.information() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Fetching something very specific for some jenkins job" ) );
   }//End Method
   
   @Test public void shouldParseForJob(){
      systemUnderTest.parsing( SAMPLE_TEST_VALUE, job );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.processingSequence() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Parsing something very specific for some jenkins job" ) );
   }//End Method
   
   @Test public void shouldUpdateForJob(){
      systemUnderTest.updated( SAMPLE_TEST_VALUE, job );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "some jenkins job something very specific updated" ) );
   }//End Method
   
   @Test public void shouldFetch(){
      systemUnderTest.fetching( SAMPLE_TEST_VALUE );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.information() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Fetching something very specific" ) );
   }//End Method
   
   @Test public void shouldParse(){
      systemUnderTest.parsing( SAMPLE_TEST_VALUE );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.processingSequence() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Parsing something very specific" ) );
   }//End Method
   
   @Test public void shouldUpdate(){
      systemUnderTest.updated( SAMPLE_TEST_VALUE );
      
      verify( messageReceiver ).log( Mockito.any(), categoryCaptor.capture(), messageCaptor.capture() );
      assertThat( categoryCaptor.getValue(), is( Categories.status() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "something very specific updated" ) );
   }//End Method
   
   @Test public void shouldProgressJobsInOrder(){
      systemUnderTest.startUpdatingJobs( 5 );
      
      verify( progressReceiver ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 0.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsFetcherDigest.UPDATING_JOBS ) );
      
      systemUnderTest.updatedJob( job );
      
      verify( progressReceiver, times( 2 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 20.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Updated some jenkins job" ) );
      
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      
      verify( progressReceiver, times( 5 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 80.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Updated some jenkins job" ) );
      
      systemUnderTest.jobsUpdated();
      verify( progressReceiver, times( 6 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue(), is( Progresses.complete() ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsFetcherDigest.UPDATED_ALL_JOBS ) );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void progressJobWithNoCountShouldThrowIllegalState(){
      systemUnderTest.updatedJob( job );
   }//End Method
   
   @Test public void shouldShowProgressGreaterThan100WhenMoreJobsAreProvided(){
      systemUnderTest.startUpdatingJobs( 2 );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      
      verify( progressReceiver, times( 7 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 300.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Updated some jenkins job" ) );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void completionShouldResetJobCount(){
      systemUnderTest.startUpdatingJobs( 10 );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      
      verify( progressReceiver, times( 5 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 40.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Updated some jenkins job" ) );
      
      systemUnderTest.jobsUpdated();
      systemUnderTest.updatedJob( job );
   }//End Method
   
   @Test public void completionShouldResetProgressThroughJobs(){
      systemUnderTest.startUpdatingJobs( 10 );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      systemUnderTest.updatedJob( job );
      
      verify( progressReceiver, times( 5 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 40.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Updated some jenkins job" ) );
      
      systemUnderTest.jobsUpdated();
      systemUnderTest.startUpdatingJobs( 10 );
      
      systemUnderTest.updatedJob( job );
      verify( progressReceiver, times( 8 ) ).progress( Mockito.any(), progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( 10.0 ) );
      assertThat( messageCaptor.getValue().getMessage(), is( "Updated some jenkins job" ) );
   }//End Method

}//End Class
