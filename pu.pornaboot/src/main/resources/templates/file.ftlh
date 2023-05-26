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
		<tr class="even"> <#-- Hier de nieuwe notatie gebruiken voor null values -->
			<th class="label">Kwaliteit</th><td>${(file.kwaliteit)!}</td>
		</tr>
		<tr class="odd">
			<th class="label">Type</th><td>${(file.type)!}</td>
		</tr>
		<tr class="even">
			<th class="label">Recensie</th><td>${(file.recensie)!}</td>
		</tr>
	</table>

	<#--  	
	<form action='<@spring.url "/directory.html" />'>
		<input id="directory" type="hidden" name="directory" value="${directory.displayName}" />
		
		<label for="zoekenVanaf">Zoeken vanaf</label>
		<input id="zoekenVanaf" type="text" name="zoekenVanaf">${zoekenVanaf}</input>
		
		<input type="radio" id="opNaam" name="orderBy" value="opNaam" <#if orderBy == "opNaam">checked</#if> />
		<label for="opNaam">Op filenaam</label>
		<input type="radio" id="opSize" name="orderBy" value="opSize" <#if orderBy == "opSize">checked</#if> />
		<label for="opSize">Op file grootte</label>
		<input type="radio" id="opDatum" name="orderBy" value="opDatum" <#if orderBy == "opDatum">checked</#if> />
		<label for="opDatum">Op datum</label>

		<button type="submit" name="submit">Submit</button>
	</form>
	-->
	<br />

	<video control>
		<source	src="${file.videoLocation}" type="video/mp4" />
	</video>
	
</body>
</html>
