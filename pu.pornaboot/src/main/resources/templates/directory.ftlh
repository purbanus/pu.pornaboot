<#import "/spring.ftl" as spring />
<#setting numberFormat="0.##" />
<html>
<head>
	<title>Files</title>
	<link rel=StyleSheet href="<@spring.url '/css/main.css' />" type="text/css">
</head>
<body>
	<h1>Files</h1>
	
	<form action='<@spring.url "/files.html" />'>
		<label for="zoekenVanaf">Zoeken vanaf</label>
		<input id="zoekenVanaf" type="text" name="zoekenVanaf">${zoekenVanaf}</input>
		<button type="submit" name="submit">Submit</button>
	</form>
	
	<#include "paginator.ftlh" />
	<br />
	<br />

	<table id="subdirectories" > <!-- class="sortable" -->
		<thead>
			<tr>
				<th class="header string">Subdirectory</th>
			</tr>
		</thead>
		<#list directory.subDirectories as subDirectory>
			<tr class=${subDirectory?itemParity}>
				<td class="string"><a href='<@spring.url "/directory.html?directory=${subDirectory.name}" />'>${subDirectory.name}</a></td>
			</tr>
		</#list>
	</table>

	<table id="detabel" > <!-- class="sortable" -->
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
				<td class="string">${file.dateTimeLastModified}</td>
				<td class="string">Hier komen de ptoperties</td>
			</tr>
		</#list>
	</table>

	<br />
	
	<#include "paginator.ftlh" />
	
</body>
</html>
