package pu.porna.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pu.porna.bo.FilesContainer;

/**
 * This type was created in VisualAge.
 */
public class Assert implements Serializable
{
private static final Logger LOG = LoggerFactory.getLogger( FilesContainer.class );
private static boolean ASSERT = true;

/**
 * Of we elk assert-statement moeten loggen, ook als de assert true is
 */
public static boolean TRACE_ALL_ASSERTS = false;

public Assert()
{
	super();
}
// code for data state assertions
public static void assertTrue(boolean condition, String message)
{
	if ( TRACE_ALL_ASSERTS )
	{
		LOG.info( "ASSERT " + condition );
	}
	if ( ASSERT && !condition)
	{
		throw new RuntimeException( "Assert " + message );
	}
}
}
