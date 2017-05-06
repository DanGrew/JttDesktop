/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.dualwall;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationPersistence.DIVIDER;
import static uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationPersistence.DIVIDER_ORIENTATION;
import static uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationPersistence.DIVIDER_POSITION;
import static uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationPersistence.DUAL_BUILD_WALL;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.geometry.Orientation;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfigurationImpl;
import uk.dangrew.jtt.model.utility.TestCommon;

/**
 * {@link DualWallConfigurationPersistence} test.
 */
public class DualWallConfigurationPersistenceTest {

   private static final double DIVIDER_POSITION_VALUE = 0.8;
   private static final Orientation DIVIDER_ORIENTATION_VALUE = Orientation.HORIZONTAL;
   
   @Mock private DualWallConfigurationModel parseModel;
   @Mock private DualWallConfigurationModel writeModel;
   private DualWallConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new DualWallConfigurationPersistence( parseModel, writeModel );
   }//End Method
   
   @Test public void shouldBuildCorrectStructure() {
      final Object placeholder = systemUnderTest.structure().placeholder();
      
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      assertThat( object.keySet(), containsInAnyOrder( DUAL_BUILD_WALL ) );
      
      JSONObject dualWall = object.getJSONObject( DUAL_BUILD_WALL );
      assertThat( dualWall.keySet(), containsInAnyOrder( DIVIDER ) );
      
      JSONObject divider = dualWall.getJSONObject( DIVIDER );
      assertThat( divider.keySet(), containsInAnyOrder( DIVIDER_POSITION, DIVIDER_ORIENTATION ) );
      assertThat( divider.get( DIVIDER_POSITION ), is( placeholder ) );
      assertThat( divider.get( DIVIDER_ORIENTATION ), is( placeholder ) );
   }//End Method
   
   @Test public void readShouldInvokeHandles(){
      String input = TestCommon.readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      
      systemUnderTest.readHandles().parse( object );
      
      verify( parseModel ).setDividerPosition( DIVIDER_POSITION, DIVIDER_POSITION_VALUE );
      verify( parseModel ).setDividerOrientation( DIVIDER_ORIENTATION, DIVIDER_ORIENTATION_VALUE );
      
      verifyNoMoreInteractions( parseModel );
   }//End Method
   
   @Test public void writeShouldInvokeHandles(){
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      systemUnderTest.writeHandles().parse( object );
      
      verify( writeModel ).getDividerPosition( DIVIDER_POSITION );
      verify( writeModel ).getDividerOrientation( DIVIDER_ORIENTATION );
      
      verifyNoMoreInteractions( writeModel );
   }//End Method
   
   @Test public void regularConstructorShouldCreateModelFromParameters(){
      systemUnderTest = new DualWallConfigurationPersistence( new DualWallConfigurationImpl() );
      assertThat( systemUnderTest.structure(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.readHandles(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.writeHandles(), is( not( nullValue() ) ) );
   }//End Method

}//End Class
