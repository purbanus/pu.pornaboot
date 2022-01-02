<div class="paginator">
	<a href='<@spring.url "/files?pageId=${paginator.firstPage.id}&rijen=${paginator.aantalRijen}&directory=${filesDocument.directory}" />'><span class="prevnext">&lt;&lt;</span></a>
	<a href='<@spring.url "/files?pageId=${paginator.previousPage.id}&rijen=${paginator.aantalRijen}&directory=${filesDocument.directory}" />'><span class="prevnext">&lt;</span></a>
	<#list paginator.pages as page>
		<#assign activeClass = "">
		<#if page.active>
			<#assign activeClass = "class='active'">
		</#if>
		<a ${activeClass} href='<@spring.url "/files?pageId=${page.id}&rijen=${paginator.aantalRijen}&directory=${filesDocument.directory}" />'>${page.id}</a>
	</#list>
	<a href='<@spring.url "/files?pageId=${paginator.nextPage.id}&rijen=${paginator.aantalRijen}&directory=${filesDocument.directory}" />'><span class="prevnext">&gt;</span></a>
	<a href='<@spring.url "/files?pageId=${paginator.lastPage.id}&rijen=${paginator.aantalRijen}&directory=${filesDocument.directory}" />'><span class="prevnext">&gt;&gt;</span></a>
</div>
