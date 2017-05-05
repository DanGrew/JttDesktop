/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

/**
 * The {@link TestClassKey} provides an interface for an item that can
 * match {@link TestClass}es based on unique information describing the {@link TestClass}.
 */
public interface TestClassKey {

   /**
    * Getter for {@link TestClass#nameProperty()}.
    * @return the {@link String} name.
    */
   public String getName();

   /**
    * Getter for {@link TestClass#locationProperty()}.
    * @return the {@link String} location.
    */
   public String getLocation();

}//End Interface
