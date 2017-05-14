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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

@RunWith( JUnitParamsRunner.class )
public class IndividualChangeApplierTest {
   
   private static final String FILENAME = "anything";
   private SoundConfiguration configuration;
   private IndividualChangeApplier systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new SoundConfiguration();
   }//End Method

   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldConfigureChange( BuildResultStatus status ) {
      for ( BuildResultStatus other : BuildResultStatus.values() ) {
         BuildResultStatusChange change = new BuildResultStatusChange( status, other );
         systemUnderTest = new IndividualChangeApplier( configuration, change );
         systemUnderTest.configure( FILENAME );
         assertThat( configuration.statusChangeSounds().get( change ), is( FILENAME ) );
      }
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldConfigureChangeWithStates( BuildResultStatus status ) {
      for ( BuildResultStatus other : BuildResultStatus.values() ) {
         BuildResultStatusChange change = new BuildResultStatusChange( status, other );
         systemUnderTest = new IndividualChangeApplier( configuration, status, other );
         systemUnderTest.configure( FILENAME );
         assertThat( configuration.statusChangeSounds().get( change ), is( FILENAME ) );
      }
   }//End Method

}//End Class
