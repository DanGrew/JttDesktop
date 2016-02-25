/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import model.tests.TestClass;

/**
 * Basic implementation of the {@link TestClassKey}.
 */
public class TestClassKeyImpl implements TestClassKey {

   private final String name;
   private final String location;
   
   /**
    * Constructs a new {@link TestClassKeyImpl}.
    * @param name the {@link String} name of the {@link TestClass}.
    * @param location the {@link String} location of the {@link TestClass}.
    */
   public TestClassKeyImpl( String name, String location ) {
      if ( name == null ) throw new IllegalArgumentException( "Null name provided." );
      if ( location == null ) throw new IllegalArgumentException( "Null location provided." );
      
      this.name = name;
      this.location = location;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public String getName() {
      return name;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getLocation() {
      return location;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + location.hashCode();
      result = prime * result + name.hashCode();
      return result;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean equals( Object obj ) {
      if ( this == obj ) {
         return true;
      }
      if ( obj == null ) {
         return false;
      }
      if ( !( obj instanceof TestClassKeyImpl ) ) {
         return false;
      }
      TestClassKeyImpl other = ( TestClassKeyImpl ) obj;
      if ( !location.equals( other.location ) ) {
         return false;
      }
      if ( !name.equals( other.name ) ) {
         return false;
      }
      return true;
   }//End Method

}//End Class
