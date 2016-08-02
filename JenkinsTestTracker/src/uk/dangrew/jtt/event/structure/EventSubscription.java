/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.event.structure;

/**
 * The {@link EventSubscription} interface provides the definition of an object that can
 * subscribe for events.
 * @param <SourceT> the source of the event.
 * @param <ValueT> the type of value changed.
 */
public interface EventSubscription< SourceT, ValueT > {
   
   /**
    * Method to notify that the given {@link Event} has occurred.
    * @param event the {@link Event} fired.
    */
   public void notify( Event< SourceT, ValueT > event );
}//End Interface
