/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.utility.TestCommon;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;

/**
 * {@link BuildWallConfigurationPersistence} end to end test to prove writing and reading from file.
 */
public class BuildWallConfigurationPersistenceE2ETest {

   private BuildWallConfiguration configuration;
   private JenkinsDatabase database;
   private BuildWallConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new BuildWallConfigurationImpl();
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new BuildWallConfigurationPersistence( configuration, database );
   }//End Method
   
   @Test public void readExistingFileInWriteItOutThenReadItBackInAndCompareWithOriginal() {
      String input = TestCommon.readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      systemUnderTest.readHandles().parse( object );
      
      ModelMarshaller marshaller = new ModelMarshaller( 
               systemUnderTest.structure(), 
               systemUnderTest.readHandles(),
               systemUnderTest.writeHandles(),
               new JarJsonPersistingProtocol( "bwcp.json", getClass() )
      );
      
      marshaller.write();
      configuration.numberOfColumns().set( 20 );
      configuration.jobPolicies().put( new JenkinsJobImpl( "something added" ), BuildWallJobPolicy.OnlyShowFailures );
      marshaller.read();
      
      assertThat( configuration.numberOfColumns().get(), is( 6 ) );
   }//End Method

}//End Class
