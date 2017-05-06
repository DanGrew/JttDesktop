/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.dualwall;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javafx.geometry.Orientation;
import uk.dangrew.jtt.desktop.buildwall.configuration.persistence.dualwall.DualWallConfigurationPersistence;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfigurationImpl;
import uk.dangrew.jtt.model.utility.TestCommon;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;

/**
 * {@link DualWallConfigurationPersistence} end to end test to prove writing and reading from file.
 */
public class DualWallConfigurationPersistenceE2ETest {

   private DualWallConfiguration configuration;
   private DualWallConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new DualWallConfigurationImpl();
      systemUnderTest = new DualWallConfigurationPersistence( configuration );
   }//End Method
   
   @Test public void readExistingFileInWriteItOutThenReadItBackInAndCompareWithOriginal() {
      String input = TestCommon.readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      systemUnderTest.readHandles().parse( object );
      
      ModelMarshaller marshaller = new ModelMarshaller( 
               systemUnderTest.structure(), 
               systemUnderTest.readHandles(),
               systemUnderTest.writeHandles(),
               new JarJsonPersistingProtocol( "dwcp.json", getClass() )
      );
      
      marshaller.write();
      configuration.dividerOrientationProperty().set( Orientation.VERTICAL );
      marshaller.read();
      
      assertThat( configuration.dividerOrientationProperty().get(), is( Orientation.HORIZONTAL ) );
   }//End Method

}//End Class
