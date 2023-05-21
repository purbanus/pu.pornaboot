package pu.porna;

import java.util.List;

import pu.porna.util.StringHelper;

public class TestClassPath
{
public static void main( String [] args ) 
{
	List<String> classPath = StringHelper.classPathAsList();
	for ( String classPathEntry : classPath )
	{
		System.out.println( classPathEntry );
	}
}
}
