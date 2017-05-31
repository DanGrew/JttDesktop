/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.utility.TestCommon;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;

public class StatisticsConfigurationPersistenceE2ETest {

   private StatisticsConfiguration configuration;
   private StatisticsConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new StatisticsConfiguration();
      systemUnderTest = new StatisticsConfigurationPersistence( configuration );
   }//End Method
   
   @Test public void readExistingFileInWriteItOutThenReadItBackInAndCompareWithOriginal() {
      String input = TestCommon.readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      systemUnderTest.readHandles().parse( object );
      
      ModelMarshaller marshaller = new ModelMarshaller( 
               systemUnderTest.structure(), 
               systemUnderTest.readHandles(),
               systemUnderTest.writeHandles(),
               new JarJsonPersistingProtocol( "scp.json", getClass() )
      );
      
      marshaller.write();
      configuration.excludedJobs().add( new JenkinsJobImpl( "Fourth" ) );
      marshaller.read();
      
      assertThat( configuration.excludedJobs().size(), is( 3 ) );
   }//End Method

}//End Class
