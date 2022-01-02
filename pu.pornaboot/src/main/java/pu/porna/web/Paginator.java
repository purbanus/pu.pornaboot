package pu.porna.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pu.porna.bo.RowBounds;

public class Paginator
{
public class Page
{
	private final int id;
	private final int offset;
	private final int limit;
	private final boolean active; 
	public Page( int aId, int aOffset, int aLimit, boolean aActive )
	{
		super();
		id = aId;
		offset = aOffset;
		limit = aLimit;
		active = aActive;
	}
	public int getId()
	{
		return id;
	}
	public int getOffset()
	{
		return offset;
	}
	public int getLimit()
	{
		return limit;
	}
	public boolean isActive()
	{
		return active;
	}
	@Override
	public String toString()
	{
		return "Page [id=" + id + ", offset=" + offset + ", limit=" + limit + ", active=" + active + "]";
	}
}

public static final int PAGES_IN_PAGINATOR = 20;
public static final int RIJEN_PER_PAGINA = 10; 
private final int aantalRijen;
private final int activePage;
private List<Page> pages;
private Page firstPage;
private Page previousPage;
private Page huidigePage;
private Page nextPage;
private Page lastPage;

public static RowBounds getStartingRowBounds()
{
	return new RowBounds( 0, RIJEN_PER_PAGINA );
}
public Paginator( int aAantalRijen, int aActivePage )
{
	super();
	aantalRijen = aAantalRijen;
	activePage = aActivePage;
	maakPages();
}
public int getAantalRijen()
{
	return aantalRijen;
}
public int getActivePage()
{
	return activePage;
}
public Page getNextPage()
{
	return nextPage;
}
public List<Page> getPages()
{
	return pages;
}
public Page getHuidigePage()
{
	return huidigePage;
}
public Page getPreviousPage()
{
	return previousPage;
}
public Page getFirstPage()
{
	return firstPage;
}
public Page getLastPage()
{
	return lastPage;
}
private void maakPages()
{
	List<Page> allPages = new ArrayList<>();
	if ( aantalRijen <= 0 )
	{
		Page page = new Page( 1, 0, 1, true );
		firstPage = page;
		lastPage = page;
		previousPage = page;
		nextPage = page;
		huidigePage = page;
		pages = Arrays.asList( page );
		return;
	}
	for ( int x = 0; x < aantalRijen; x += RIJEN_PER_PAGINA )
	{
		int pageId = x / RIJEN_PER_PAGINA + 1;
		allPages.add( new Page( pageId, x, RIJEN_PER_PAGINA, pageId == activePage ) );
	}
	firstPage = allPages.get( 0 );
	lastPage = allPages.get( allPages.size() - 1 );

	// We gaan nu 20 pagina's uitkiezen
	pages = trimPageListDownTo( allPages, PAGES_IN_PAGINATOR );
	
	previousPage = pages.get( 0 );
	boolean thisPageIsActive = false;
	for ( Page page : pages )
	{
		if ( page.isActive() )
		{
			thisPageIsActive = true;
			huidigePage = page;
		}
		else if ( thisPageIsActive )
		{
			nextPage = page;
			break;
		}
		else
		{
			previousPage = page;
		}
	}
	// Als we niet in die else-if-tak hierboven terechtgekomen zijn, is nextPage nog null
	if ( nextPage == null )
	{
		nextPage = lastPage;
	}
}
private List<Page> trimPageListDownTo( List<Page> aPages, int aPagesInPaginator )
{
	if ( aPages.size() <= PAGES_IN_PAGINATOR )
	{
		return aPages;
	}
	List<Page> newPages = new ArrayList<>();
	int activePageIndex = activePage - 1;
	int startIndex = Math.max(  0,  activePageIndex - PAGES_IN_PAGINATOR / 2 );
	startIndex = Math.min(  startIndex,  aPages.size() - PAGES_IN_PAGINATOR );
	for ( int x = startIndex; x < startIndex + PAGES_IN_PAGINATOR; x++ )
	{
		newPages.add( aPages.get( x ) );
	}
	return newPages;
}
public RowBounds getHuidigeRowBounds()
{
	return new RowBounds( getHuidigePage().getOffset(), getHuidigePage().getLimit() );
}
}
