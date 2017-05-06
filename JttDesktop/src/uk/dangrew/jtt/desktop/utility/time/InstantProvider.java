/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.utility.time;

import java.util.function.Supplier;

/**
 * Bespoke interface for a {@link Supplier} of long milliseconds of the {@link java.time.Instant}.
 */
public interface InstantProvider extends Supplier< Long >{}
