/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import graphics.JavaFxInitializer;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import viewer.basic.DigestViewer;

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
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemDigest = new DigestViewer();
      display = new BorderPane();
   }//End Method

   @Test public void shouldIdentifySystemDigest() {
      parent = new BorderPane( display );
      parent.setTop( systemDigest );
      
      systemUnderTest = new WrappedSystemDigest( display );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( true ) );
   }//End Method
   
   @Test public void shouldIdentifySystemDigestWithTitledPane() {
      parent = new BorderPane( display );
      systemDigestPane = new TitledPane( "anything", systemDigest );
      parent.setTop( systemDigestPane );
      
      systemUnderTest = new WrappedSystemDigest( display );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( true ) );
   }//End Method
   
   @Test public void shouldNotIdentifySystemDigestWithNoParent() {
      systemUnderTest = new WrappedSystemDigest( display );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( false ) );
   }//End Method
   
   @Test public void shouldNotIdentifySystemDigestWithNoTitledPaneAndNoDigest() {
      parent = new BorderPane( display );
      
      systemUnderTest = new WrappedSystemDigest( display );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( false ) );
   }//End Method
   
   @Test public void shouldNotIdentifySystemDigestWithTitledPaneButNoDigest() {
      parent = new BorderPane( display );
      parent.setTop( systemDigestPane );
      
      systemUnderTest = new WrappedSystemDigest( display );
      assertThat( systemUnderTest.isSystemDigestAvailable(), is( false ) );
   }//End Method
   
   @Test public void shouldInsertAndRemoveSystemDigest() {
      parent = new BorderPane( display );
      systemDigestPane = new TitledPane( "anything", systemDigest );
      parent.setTop( systemDigestPane );
      assertThat( parent.getTop(), is( systemDigestPane ) );
      
      systemUnderTest = new WrappedSystemDigest( display );
      
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
      
      systemUnderTest = new WrappedSystemDigest( display );
      
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
      
      systemUnderTest = new WrappedSystemDigest( display );
      
      systemUnderTest.removeDigest();
      systemUnderTest.removeDigest();
      systemUnderTest.removeDigest();
      assertThat( parent.getTop(), nullValue() );
   }//End Method

}//End Class