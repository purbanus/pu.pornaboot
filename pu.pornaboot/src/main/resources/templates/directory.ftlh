<#import "/spring.ftl" as spring />
<#setting numberFormat="#,###.##" />
<#setting urlEscapingCharset="UTF-8">
<#-- Dit heeft alleen effect op java.Util.Date objecten, niet op LocalDate/Times! -->
<#setting datetimeFormat="dd-MM-yyyy HH:mm:ss" />

<html>
<head>
	<title>Files</title>
	<link rel=StyleSheet href="<@spring.url '/css/main.css' />" type="text/css">
	<script type="text/javascript" src="#springUrl( '/js/jquery-1.11.1.js' )"></script> 
	<script type="text/javascript" src="#springUrl( '/js/jquery.tablesorter-2.0.js' )"></script> 
	<script type="text/javascript">
$(document).ready(function() 
{ 
    $("#detabel").tablesorter({widgets: ['zebra']}); 
});
	</script> 
</head>
<body>
	<h1>Directory ${directory.displayName}</h1>
	
	<form action='<@spring.url "/directory.html" />'>
		<label for="zoekenVanaf">Zoeken vanaf</label>
		<input id="zoekenVanaf" type="text" name="zoekenVanaf">${zoekenVanaf}</input>
		<input id="directory" type="hidden" name="directory" value="${directory.name}" />
		<button type="submit" name="submit">Submit</button>
	</form>
	
	<#include "paginator.ftlh" />
	<br />
	<br />

	<#if directory.subDirectories?size gt 0>
		<table id="subdirectories" > <!-- class="sortable" -->
			<thead>
				<tr>
					<th class="header string">Subdirectory</th>
				</tr>
			</thead>
			<#list directory.subDirectories as subDirectory>
				<tr class=${subDirectory?itemParity}>
				<#-- Check of directory.subDirectory de quote bevat - ja dus -->
					<#-- <td class="string">subDirName=${subDirectory.name}</td>  -->
					
					<#-- Tijdelijk @spring.url weggehaald. Het breekt url's af na een quote -->
					<#-- <td class="string"><a href='<@spring.url "/directory.html?directory=${subDirectory.name}" />'>${subDirectory.name}</a></td> -->
					<td class="string"><a href="/directory.html?directory=${subDirectory.displayName}">${subDirectory.displayName}</a></td>
				</tr>
			</#list>
		</table>
	</#if>

	<#if directory.files?size gt 0>
		<table id="detabel" class="sortable">
			<thead>
				<tr>
					<th class="header string">File</th> <!-- Client ID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
					<th class="header number">File size</th>
					<th class="header string">Modified</th>
					<th class="header string">Remark</th>
				</tr>
			</thead>
			<#list directory.files as file>
				<tr class=${file?itemParity}>
					<td class="string"><a href='<@spring.url "/file.html?name=${file.name}" />'>${file.name}</a></td>
					<td class="number">${file.size}</td>
					<td class="string">${file.dateTimeLastModified.format( 'dd-MM-yyyy HH:mm:ss' ) }</td>
					<#-- <td class="string">${file.dateTimeLastModified }</td> -->
					<td class="string">Hier komen de properties</td>
				</tr>
			</#list>
		</table>
	</#if>
	<br />
	
	<#include "paginator.ftlh" />
	
</body>
</html>
