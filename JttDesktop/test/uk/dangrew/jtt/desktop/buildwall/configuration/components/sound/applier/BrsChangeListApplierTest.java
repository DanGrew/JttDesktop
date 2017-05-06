/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier.BrsChangeListApplier;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;

public class BrsChangeListApplierTest {
   
   private static final String FILENAME = "anything";
   
   private List< BuildResultStatusChange > changes;
   private SoundConfiguration configuration;
   private BrsChangeListApplier systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      configuration = new SoundConfiguration();
      changes = new ArrayList<>();
      systemUnderTest = new BrsChangeListApplier( configuration, changes );
   }//End Method
   
   /**
    * Method to populate the {@link List} of {@link BuildResultStatusChange}s that will be tested
    * against. These should be all changes that will be set in the {@link SoundConfiguration}.
    * @param changes the {@link List} of {@link BuildResultStatusChange}s to add to.
    */
   protected void populateChanges( List< BuildResultStatusChange > changes ){}
   
   /**
    * Method to construct the specific system under test.
    * @param configuration the {@link SoundConfiguration}.
    * @param changes the {@link List} of {@link BuildResultStatusChange}s to use.
    */
   protected BrsChangeListApplier constructSut( SoundConfiguration configuration, List< BuildResultStatusChange > changes ){
      return new BrsChangeListApplier( configuration, changes );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new BrsChangeListApplier( null, new ArrayList<>() );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullList(){
      new BrsChangeListApplier( configuration, null );
   }//End Method
   
   @Test public void shouldSetAllChangesInConfigurationFromList() {
      for( int i = 0; i < 10; i++ ) {
         changes.add( mock( BuildResultStatusChange.class ) );
      }
      
      systemUnderTest.configure( FILENAME );
      for ( BuildResultStatusChange change : changes ) {
         assertThat( configuration.statusChangeSounds().get( change ), is( FILENAME ) );
      }
   }//End Method
   
   @Test public void shouldBeAssociatedWith(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new SoundConfiguration() ), is( false ) );
   }//End Method

}//End Class
