/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.CURRENT_STATE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.EXCLUSION_JOB_NAME;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.FILENAME;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.JOB_EXCLUSIONS;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.PREVIOUS_STATE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.SOUND;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationPersistence.STATUS_CHANGES;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.utility.io.IoCommon;

public class SoundConfigurationPersistenceTest {

   private static final int SAMPLE_CHANGE_COUNT = 36;
   private static final int SAMPLE_EXCLUSIONS_COUNT = 3;
   private static final int CHANGE_COUNT = 3;
   private static final int EXCLUSION_COUNT = 4;
   
   @Mock private SoundConfigurationParseModel parseModel;
   @Mock private SoundConfigurationWriteModel writeModel;
   private SoundConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      when( writeModel.getNumberOfStatusChanges( Mockito.anyString() ) ).thenReturn( CHANGE_COUNT );
      when( writeModel.getNumberOfJobExclusions( Mockito.anyString() ) ).thenReturn( EXCLUSION_COUNT );
      systemUnderTest = new SoundConfigurationPersistence( parseModel, writeModel );
   }//End Method
   
   @Test public void shouldBuildCorrectStructure() {
      final Object placeholder = systemUnderTest.structure().placeholder();
      
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      assertThat( object.keySet(), containsInAnyOrder( SOUND ) );
      
      JSONObject sound = object.getJSONObject( SOUND );
      assertThat( sound.keySet(), containsInAnyOrder( STATUS_CHANGES, JOB_EXCLUSIONS ) );
      
      JSONArray changes = sound.getJSONArray( STATUS_CHANGES );
      for( int i = 0; i < CHANGE_COUNT; i++ ) {
         JSONObject change = changes.getJSONObject( i );
         assertThat( change.keySet(), containsInAnyOrder( PREVIOUS_STATE, CURRENT_STATE, FILENAME ) );
         assertThat( change.get( PREVIOUS_STATE ), is( placeholder ) );
         assertThat( change.get( CURRENT_STATE ), is( placeholder ) );
         assertThat( change.get( FILENAME ), is( placeholder ) );
      }
      
      JSONArray exclusions = sound.getJSONArray( JOB_EXCLUSIONS );
      for( int i = 0; i < EXCLUSION_COUNT; i++ ) {
         JSONObject exclusion = exclusions.getJSONObject( i );
         assertThat( exclusion.keySet(), containsInAnyOrder( EXCLUSION_JOB_NAME ) );
         assertThat( exclusion.get( EXCLUSION_JOB_NAME ), is( placeholder ) );
      }
   }//End Method
   
   @Test public void readShouldInvokeHandles(){
      String input = new IoCommon().readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      
      systemUnderTest.readHandles().parse( object );
      
      verify( parseModel, times( SAMPLE_CHANGE_COUNT ) ).startParsingChange( Mockito.anyString() );
      verify( parseModel, times( SAMPLE_CHANGE_COUNT ) ).setPreviousState( Mockito.anyString(), Mockito.any() );
      verify( parseModel, times( SAMPLE_CHANGE_COUNT ) ).setCurrentState( Mockito.anyString(), Mockito.any() );
      verify( parseModel, times( SAMPLE_CHANGE_COUNT ) ).setFilename( Mockito.anyString(), Mockito.anyString() );
      
      verify( parseModel ).startParsingExclusions( Mockito.anyString() );
      verify( parseModel, times( SAMPLE_EXCLUSIONS_COUNT ) ).setExclusion( Mockito.anyString(), Mockito.anyString() );
      
      verifyNoMoreInteractions( parseModel );
   }//End Method
   
   @Test public void writeShouldInvokeHandles(){
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      systemUnderTest.writeHandles().parse( object );
      
      verify( writeModel ).getNumberOfStatusChanges( Mockito.anyString() );
      verify( writeModel ).startWritingChanges( Mockito.anyString() );
      verify( writeModel, times( CHANGE_COUNT ) ).getPreviousState( Mockito.anyString() );
      verify( writeModel, times( CHANGE_COUNT ) ).getCurrentState( Mockito.anyString() );
      verify( writeModel, times( CHANGE_COUNT ) ).getFilename( Mockito.anyString() );
      
      verify( writeModel ).getNumberOfJobExclusions( Mockito.anyString() );
      verify( writeModel ).startWritingExclusions( Mockito.anyString() );
      verify( writeModel, times( EXCLUSION_COUNT ) ).getExclusion( Mockito.anyString() );
      
      verifyNoMoreInteractions( writeModel );
   }//End Method
   
   @Test public void regularConstructorShouldCreateModelFromParameters(){
      systemUnderTest = new SoundConfigurationPersistence( new SoundConfiguration(), new TestJenkinsDatabaseImpl() );
      assertThat( systemUnderTest.structure(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.readHandles(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.writeHandles(), is( not( nullValue() ) ) );
   }//End Method

}//End Class
