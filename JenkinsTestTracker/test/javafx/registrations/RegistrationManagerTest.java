/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.registrations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * {@link RegistrationManager} test.
 */
public class RegistrationManagerTest {

   @Mock private RegistrationImpl registrationA;
   @Mock private RegistrationImpl registrationB;
   @Mock private RegistrationImpl registrationC;
   private RegistrationManager systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new RegistrationManager();
   }//End Method
   
   @Test public void shouldRegisterRegistrationsOnAdd() {
      systemUnderTest.apply( registrationA );
      verify( registrationA ).register();
   }//End Method
   
   @Test public void shouldUnregisterWhenShutdown() {
      systemUnderTest.apply( registrationA );
      systemUnderTest.shutdown();
      verify( registrationA ).unregister();
   }//End Method
   
   @Test public void shouldRegisterAllRegistrationsOnAdd() {
      systemUnderTest.apply( registrationA );
      systemUnderTest.apply( registrationB );
      systemUnderTest.apply( registrationC );
      verify( registrationA ).register();
      verify( registrationB ).register();
      verify( registrationC ).register();
   }//End Method
   
   @Test public void shouldUnregisterAllWhenShutdown() {
      systemUnderTest.apply( registrationA );
      systemUnderTest.apply( registrationB );
      systemUnderTest.apply( registrationC );
      systemUnderTest.shutdown();
      verify( registrationA ).unregister();
      verify( registrationB ).unregister();
      verify( registrationC ).unregister();
   }//End Method
   
   @Test public void shouldRemoveReferencesWhenShutdown() {
      systemUnderTest.apply( registrationA );
      verify( registrationA ).register();
      systemUnderTest.apply( registrationB );
      verify( registrationB ).register();
      systemUnderTest.apply( registrationC );
      verify( registrationC ).register();
      systemUnderTest.shutdown();
      verify( registrationA ).unregister();
      verify( registrationB ).unregister();
      verify( registrationC ).unregister();
      
      systemUnderTest.shutdown();
      verifyNoMoreInteractions( registrationA, registrationB, registrationC );
   }//End Method

}//End Class
