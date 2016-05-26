/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

import uk.dangrew.jtt.storage.database.TestClassKey;
import uk.dangrew.jtt.storage.database.TestClassKeyImpl;

/**
 * {@link TestClassKeyImpl} test.
 */
public class TestClassKeyImplTest {

   /**
    * Prove that a null name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullName() {
      new TestClassKeyImpl( null, "anywhere" );
   }//End Method
   
   /**
    * Prove that a null location is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectLocationName() {
      new TestClassKeyImpl( "anything", null );
   }//End Method
   
   /**
    * Prove that two {@link TestClassKey}s calculate the same hashcode when they have the same data.
    */
   @Test public void keysWithSameValuesShouldMatchHashCode(){
      final String name = "anything";
      final String location = "anywhere";
      TestClassKey key1 = new TestClassKeyImpl( name, location );
      TestClassKey key2 = new TestClassKeyImpl( name, location );
      TestClassKey key3 = new TestClassKeyImpl( "something", "else" );
      Assert.assertEquals( key1.hashCode(), key2.hashCode() );
      Assert.assertNotEquals( key1.hashCode(), key3.hashCode() );
      Assert.assertNotEquals( key2.hashCode(), key3.hashCode() );
   }//End Method
   
   /**
    * Prove that two {@link TestClassKey}s are equal when they have the same data.
    */
   @Test public void keysWithSameValuesShouldBeEquals(){
      final String name = "anything";
      final String location = "anywhere";
      TestClassKey key1 = new TestClassKeyImpl( name, location );
      TestClassKey key2 = new TestClassKeyImpl( name, location );
      TestClassKey key3 = new TestClassKeyImpl( "something", "else" );
      Assert.assertTrue( key1.equals( key2 ) );
      Assert.assertFalse( key1.equals( key3 ) );
      Assert.assertFalse( key2.equals( key3 ) );
   }//End Method
   
   /**
    * Prove that the key correctly holds the parameters given.
    */
   @Test public void keyShouldHoldGivenParameters(){
      final String name = "anything";
      final String location = "anywhere";
      TestClassKey key = new TestClassKeyImpl( name, location );
      
      Assert.assertEquals( name, key.getName() );
      Assert.assertEquals( location, key.getLocation() );
   }//End Method
   
   @Test public void shouldBeEqual(){
      final String name = "anything";
      final String location = "anywhere";
      TestClassKey key1 = new TestClassKeyImpl( name, location );
      
      Assert.assertTrue( key1.equals( key1 ) );
      assertThat( key1.equals( key1 ), is( true ) );
   }//End Method
   
   @Test public void shouldNotBeEqual(){
      final String name = "anything";
      final String location = "anywhere";
      TestClassKey key1 = new TestClassKeyImpl( name, location );
      
      Assert.assertFalse( key1.equals( null ) );
      Assert.assertFalse( key1.equals( "anything" ) );
      Assert.assertFalse( key1.equals( new TestClassKeyImpl( "another", location ) ) );
      Assert.assertFalse( key1.equals( new TestClassKeyImpl( name, "somewhere else" ) ) );
   }//End Method

}//End Class
