/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.utility.observable.PrivatelyModifiableObservableListImpl;

/**
 * {@link MappedObservableStoreManagerImpl} test.
 */
public class MappedObservableStoreManagerImplTest {

   private Object object;
   private String key;
   private ObjectStoreManager< String, Object > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      object = new Object();
      key = "anything";
      systemUnderTest = new MappedObservableStoreManagerImpl<>();
   }//End Method
   
   @Test public void shouldStoreObject() {
      Assert.assertTrue( systemUnderTest.isEmpty() ); 
      systemUnderTest.store( key, object );
      Assert.assertFalse( systemUnderTest.isEmpty() );
      Assert.assertTrue( systemUnderTest.has( key ) );
   }//End Method
   
   @Test public void shouldRetrieveObjectWithKey() {
      shouldStoreObject();
      Assert.assertEquals( object, systemUnderTest.get( key ) );
   }//End Method
   
   @Test public void shouldRetrieveNothingWithUnrecognisedKey() {
      Assert.assertNull( systemUnderTest.get( "somethingElse" ) );
   }//End Method
   
   @Test public void shouldRetrieveNothingWithNullKey() {
      Assert.assertNull( systemUnderTest.get( null ) );
   }//End Method
   
   @Test public void shouldRemoveObjectWithKey() {
      shouldStoreObject();
      Assert.assertEquals( object, systemUnderTest.remove( key ) );
      Assert.assertTrue( systemUnderTest.isEmpty() );
      Assert.assertFalse( systemUnderTest.has( key ) );
   }//End Method
   
   @Test public void shouldProvideObservableList() {
      Assert.assertNotNull( systemUnderTest.objectList() );
      Assert.assertTrue( systemUnderTest.objectList().isEmpty() );
   }//End Method
   
   @Test public void shouldKeepStoreAddAndListInSync() {
      shouldStoreObject();
      Assert.assertTrue( systemUnderTest.objectList().contains( object ) );
   }//End Method
   
   @Test public void shouldKeepStoreRemoveAndListInSync() {
      shouldStoreObject();
      Assert.assertTrue( systemUnderTest.objectList().contains( object ) );
      systemUnderTest.remove( key );
      Assert.assertFalse( systemUnderTest.objectList().contains( object ) );
   }//End Method
   
   @Test public void shouldNotHaveNullKey(){
      Assert.assertFalse( systemUnderTest.has( null ) );
   }//End Method
   
   @Test public void shouldNotStoreNull(){
      systemUnderTest.store( null, object );
      Assert.assertTrue( systemUnderTest.isEmpty() );
   }//End Method

   @Test public void shouldNotGetNull(){
      Assert.assertNull( systemUnderTest.get( null ) );
   }//End Method
   
   @Test public void shouldNotRemoveNull(){
      Assert.assertNull( systemUnderTest.remove( null ) );
   }//End Method
   
   @Test public void shouldOverwriteKeyMapping(){
      shouldStoreObject();

      Assert.assertEquals( object, systemUnderTest.get( key ) );
      final Object object2 = new Object();
      systemUnderTest.store( key, object2 );
      Assert.assertEquals( object2, systemUnderTest.get( key ) );
   }//End Method
   
   @Test public void shouldAddToListOnceOnly(){
      systemUnderTest.store( key, object );
      Assert.assertTrue( systemUnderTest.has( key ) );
      systemUnderTest.store( key, object );
      Assert.assertEquals( 1, systemUnderTest.objectList().size() );
   }//End Method
   
   @Test public void shouldUseAppropriateClassToRestrictModifications(){
      Assert.assertTrue( systemUnderTest.objectList() instanceof PrivatelyModifiableObservableListImpl );
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void shouldNotAllowModificationsToList(){
      systemUnderTest.objectList().add( object );
   }//End Method

   @Test public void addAndForeachShouldBeSynchronized() {
      TestCommon.assertConcurrencyIsNotAnIssue( 
               count -> systemUnderTest.store( "" + count, count ), 
               count -> systemUnderTest.objectList().forEach( item -> item.toString() ), 
               1000 
      );
   }//End Method
   
}//End Class
