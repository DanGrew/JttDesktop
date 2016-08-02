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
 * The {@link EventNotification} provides an interface for a notification action of an event.
 * @param <SourceT> the source of the event.
 * @param <ValueT> the type of value changed.
 */
public interface EventNotification< SourceT, ValueT > {
   
   /**
    * Method to fire the given {@link Event} to {@link EventSubscription}s managed.
    * @param event the {@link Event} to fire.
    */
   public void fire( Event< SourceT, ValueT > event );

}//End Interface
