/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package utility.observable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Implementation of {@link ObservableList} with a backing {@link ObservableList} that restricts modification of the 
 * {@link ObservableList} directly. This allows a large part of the interface to function, and correctly, and have
 * this interacted with as if it were a full {@link ObservableList}.
 * @param <ObjectTypeT> the type in the list.
 */
public class PrivatelyModifiableObservableListImpl< ObjectTypeT > implements ObservableList< ObjectTypeT >{

   private static final String UNSAFE = "Modifications are not publicly allowed.";
   private final ObservableList< ObjectTypeT > backingList;
   
   /**
    * Constructs a new {@link PrivatelyModifiableObservableListImpl} with the gien backing {@link ObservableList}.
    * @param backingList the {@link ObservableList} that contains the actual data.
    */
   public PrivatelyModifiableObservableListImpl( ObservableList< ObjectTypeT > backingList ) {
      this.backingList = backingList;
   }//End Constructor
   
   /*
    * Not publicly safe methods.
    */
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean add( ObjectTypeT e ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean remove( Object o ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean addAll( Collection< ? extends ObjectTypeT > c ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean addAll( int index, Collection< ? extends ObjectTypeT > c ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean removeAll( Collection< ? > c ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean retainAll( Collection< ? > c ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void clear() {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectTypeT set( int index, ObjectTypeT element ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void add( int index, ObjectTypeT element ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectTypeT remove( int index ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked") //Overriding only to throw.
   @Override public boolean addAll( ObjectTypeT... elements ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked") //Overriding only to throw.
   @Override public boolean setAll( ObjectTypeT... elements ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean setAll( Collection< ? extends ObjectTypeT > col ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked") //Overriding only to throw.
   @Override public boolean removeAll( ObjectTypeT... elements ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked") //Overriding only to throw.
   @Override public boolean retainAll( ObjectTypeT... elements ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void remove( int from, int to ) {
      throw new UnsupportedOperationException( UNSAFE );
   }//End Method
   
   /*
    * Safe methods that use composite.
    */
   
   /**
    * {@inheritDoc}
    */
   @Override public int size() {
      return backingList.size();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean isEmpty() {
      return backingList.isEmpty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean contains( Object o ) {
      return backingList.contains( o );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public Iterator< ObjectTypeT > iterator() {
      return backingList.iterator();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public Object[] toArray() {
      return backingList.toArray();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public < T > T[] toArray( T[] a ) {
      return backingList.toArray( a );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean containsAll( Collection< ? > c ) {
      return backingList.containsAll( c );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectTypeT get( int index ) {
      return backingList.get( index );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public int indexOf( Object o ) {
      return backingList.indexOf( o );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public int lastIndexOf( Object o ) {
      return backingList.lastIndexOf( o );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ListIterator< ObjectTypeT > listIterator() {
      return backingList.listIterator();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ListIterator< ObjectTypeT > listIterator( int index ) {
      return backingList.listIterator( index );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public List< ObjectTypeT > subList( int fromIndex, int toIndex ) {
      return backingList.subList( fromIndex, toIndex );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void addListener( InvalidationListener listener ) {
      backingList.addListener( listener );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void removeListener( InvalidationListener listener ) {
      backingList.removeListener( listener );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void addListener( ListChangeListener< ? super ObjectTypeT > listener ) {
      backingList.addListener( listener );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void removeListener( ListChangeListener< ? super ObjectTypeT > listener ) {
      backingList.removeListener( listener );
   }//End Method

}//End Class
