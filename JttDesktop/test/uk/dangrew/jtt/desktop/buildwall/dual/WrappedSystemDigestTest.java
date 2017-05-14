/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.dual;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.sd.graphics.launch.TestApplication;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * WrappedSystemDigest test.
 */
public class WrappedSystemDigestTest {
   
   private BorderPane parent;
   private TitledPane systemDigestPane;
   private DigestViewer systemDigest;
   private BorderPane display;
   private WrappedSystemDigest systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      TestApplication.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemDigest = new DigestViewer();
      display = new BorderPane();
   }//End Method

   @Test public void shouldIdentifySystemDigest() {
      parent = new BorderPane( display );
      parent.setTop( systemDigest );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( true ) );
   }//End Method
   
   @Test public void shouldIdentifySystemDigestWithTitledPane() {
      parent = new BorderPane( display );
      systemDigestPane = new TitledPane( "anything", systemDigest );
      parent.setTop( systemDigestPane );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( true ) );
   }//End Method
   
   @Test public void shouldNotIdentifySystemDigestWithNoParent() {
      systemUnderTest = new WrappedSystemDigest( parent );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( false ) );
   }//End Method
   
   @Test public void shouldNotIdentifySystemDigestWithNoTitledPaneAndNoDigest() {
      parent = new BorderPane( display );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( false ) );
   }//End Method
   
   @Test public void shouldNotIdentifySystemDigestWithTitledPaneButNoDigest() {
      parent = new BorderPane( display );
      parent.setTop( systemDigestPane );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( false ) );
   }//End Method
   
   @Test public void shouldInsertAndRemoveSystemDigest() {
      parent = new BorderPane( display );
      systemDigestPane = new TitledPane( "anything", systemDigest );
      parent.setTop( systemDigestPane );
      assertThat( parent.getTop(), is( systemDigestPane ) );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      
      systemUnderTest.removeDigest();
      assertThat( parent.getTop(), nullValue() );
      
      systemUnderTest.insertDigest();
      assertThat( parent.getTop(), is( systemDigestPane ) );
      
      systemUnderTest.removeDigest();
      assertThat( parent.getTop(), nullValue() );
      
      systemUnderTest.insertDigest();
      assertThat( parent.getTop(), is( systemDigestPane ) );
   }//End Method
   
   @Test public void shouldNotInsertWhenAlreadyPresent() {
      parent = new BorderPane( display );
      systemDigestPane = new TitledPane( "anything", systemDigest );
      parent.setTop( systemDigestPane );
      assertThat( parent.getTop(), is( systemDigestPane ) );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      
      systemUnderTest.insertDigest();
      systemUnderTest.insertDigest();
      systemUnderTest.insertDigest();
      assertThat( parent.getTop(), is( systemDigestPane ) );
   }//End Method
   
   @Test public void shouldNotRemoveWhenNotPresent() {
      parent = new BorderPane( display );
      systemDigestPane = new TitledPane( "anything", systemDigest );
      parent.setTop( systemDigestPane );
      assertThat( parent.getTop(), is( systemDigestPane ) );
      
      systemUnderTest = new WrappedSystemDigest( parent );
      
      systemUnderTest.removeDigest();
      systemUnderTest.removeDigest();
      systemUnderTest.removeDigest();
      assertThat( parent.getTop(), nullValue() );
   }//End Method

}//End Class
