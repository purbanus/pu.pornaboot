<div class="paginator">
	<a class="left" href='<@spring.url "/directory?pageId=${paginator.firstPage.id}&rijen=${paginator.aantalRijen}&directory=${directory.name}" />'><span class="prevnext">&lt;&lt;</span></a>
	<a href='<@spring.url "/directory?pageId=${paginator.previousPage.id}&rijen=${paginator.aantalRijen}&directory=${directory.name}" />'><span class="prevnext">&lt;</span></a>
	<#list paginator.pages as page>
		<a
		<#if page.active>
			class="active"
		</#if>
		href='<@spring.url "/directory?pageId=${page.id}&rijen=${paginator.aantalRijen}&directory=${directory.name}" />'>${page.id}
		</a>
	</#list>
	<a href='<@spring.url "/directory?pageId=${paginator.nextPage.id}&rijen=${paginator.aantalRijen}&directory=${directory.name}" />'><span class="prevnext">&gt;</span></a>
	<a href='<@spring.url "/directory?pageId=${paginator.lastPage.id}&rijen=${paginator.aantalRijen}&directory=${directory.name}" />'><span class="prevnext">&gt;&gt;</span></a>
</div>
