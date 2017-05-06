/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.utility.TestCommon;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;

/**
 * {@link SoundConfigurationPersistence} end to end test to prove writing and reading from file.
 */
public class SoundConfigurationPersistenceE2ETest {

   private JenkinsDatabase database;
   private SoundConfiguration configuration;
   private SoundConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new SoundConfiguration();
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new SoundConfigurationPersistence( configuration, database );
   }//End Method
   
   @Test public void readExistingFileInWriteItOutThenReadItBackInAndCompareWithOriginal() {
      String input = TestCommon.readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      systemUnderTest.readHandles().parse( object );
      
      ModelMarshaller marshaller = new ModelMarshaller( 
               systemUnderTest.structure(), 
               systemUnderTest.readHandles(),
               systemUnderTest.writeHandles(),
               new JarJsonPersistingProtocol( "sound-test.json", getClass() )
      );
      
      marshaller.write();
      
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
      configuration.statusChangeSounds().put( 
               change, "anything" 
      );
      marshaller.read();
      
      assertThat( configuration.statusChangeSounds().get( change ), is( not( "anything" ) ) );
   }//End Method

}//End Class
