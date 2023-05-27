<#import "/spring.ftl" as spring />
<#setting numberFormat="#,###.##" />
<#setting urlEscapingCharset="UTF-8">
<#-- Dit heeft alleen effect op java.Util.Date objecten, niet op LocalDate/Times! -->
<#setting datetimeFormat="dd-MM-yyyy HH:mm:ss" />

<html>
<head>
	<title>File</title>
	<link rel=StyleSheet href="<@spring.url '/css/main.css' />" type="text/css">
</head>
<body>
	<h1>File ${file.name}</h1>

	<form action='<@spring.url "/file-update.html" />'>
		<input type="hidden" id="directory" name="directory" value="${file.directoryDisplayName}" />
		<input type="hidden" id="fileName" name="fileName" value="${file.name}" />
		<table border="0" cellspacing="0" cellpadding="5" class="issueDetails">
			<tr class="even">
				<th class="label">Naam</th><td>${file.name}</td>
			</tr>
			<tr class="odd">
				<th class="label">Directory</th><td>${file.directory.displayName}</td>
			</tr>
			<tr class="even">
				<th class="label">Datum/Tijd</th><td>${file.dateTimeLastModified.format( 'dd-MM-yyyy HH:mm:ss' ) }</td>
			</tr>
			<tr class="odd">
				<th class="label">Grootte</th><td>${file.size}</td>
			</tr>
			<tr class="even">
				<th class="label">Kwaliteit</th>
				<td>
					<#list kwaliteiten as kwaliteit>
						<input type="radio" id="${kwaliteit}" name="kwaliteit" value="${kwaliteit}" <#if deKwaliteit?seqContains( kwaliteit )>checked</#if> />
						<label for="${kwaliteit}">${kwaliteit}</label>
					</#list>
				</td>
			</tr>
			<tr class="odd">
				<th class="label">Type</th>
				<td>
					<#list types as type>
						<input type="checkbox" id="${type}" name="type" value="${type}" <#if deType?seqContains( type )>checked</#if> />
						<label for="${type}">${type}</label>
					</#list>
				</td>
			</tr>
			<tr class="even">
				<th class="label">Review</th><td>${(file.review)!}</td>
			</tr>
		</table>

		<button type="submit" name="submit">Submit</button>
	</form>

	<br />

<#--
	<video control>
		<source	src="${file.videoLocation}" type="video/mp4" />
	</video>
-->	
</body>
</html>
