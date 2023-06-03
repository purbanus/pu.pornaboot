package pu.porna.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import pu.porna.config.PornaConfig;
import pu.porna.dal.FlatDocument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UN_Directory_Name", columnNames = { "name" }) })

public class Directory implements FileSystemObject
{
public static Directory fromFlatDocument( FlatDocument aFlatDocument )
{
	return builder()
		.id( aFlatDocument.getDirectoryId() )
		.name( aFlatDocument.getDirectoryName() )
		.dateTimeLastModified( aFlatDocument.getDirectoryDateTimeLastModified() )
		.subDirectories( new ArrayList<>() )
		.files( new ArrayList<>() )
		.build();
}
public static Directory subDirectoryFromFlatDocument( FlatDocument aFlatDocument )
{
	return builder()
		.id( aFlatDocument.getSubDirectoryId() )
		.name( aFlatDocument.getSubDirectoryName() )
		.dateTimeLastModified( aFlatDocument.getSubDirectoryDateTimeLastModified() )
		.subDirectories( new ArrayList<>() )
		.files( new ArrayList<>() )
		.build();
}
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;

@Column( nullable = false )
private String name;

@Column( nullable = false )
private LocalDateTime dateTimeLastModified;

@Transient
@ToString.Exclude
@EqualsAndHashCode.Exclude
private PornaConfig pornaConfig;

@Transient
private int totalNumberOfFiles;

@ManyToOne(optional = true, fetch = FetchType.LAZY)
@JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
private Directory parent;

/* Zie het eerste antwoord in https://stackoverflow.com/questions/15216321/jpa-self-join-using-jointable */
@OneToMany(
    cascade = {CascadeType.ALL},
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
@JoinColumn(name = "PARENT_ID")
@OnDelete( action = OnDeleteAction.CASCADE )
@Builder.Default
@ToString.Exclude
@EqualsAndHashCode.Exclude
private List<Directory> subDirectories = new ArrayList<>();

@OneToMany(
	mappedBy = "directory",
	cascade = CascadeType.ALL, // Zodat de files ook gesavet worden
	fetch = FetchType.LAZY 
)
@OnDelete( action = OnDeleteAction.CASCADE )
@Builder.Default
@ToString.Exclude
@EqualsAndHashCode.Exclude
private List<File> files = new ArrayList<>();

@Transient
@Builder.Default
@ToString.Exclude
@Getter( AccessLevel.NONE )
@Setter( AccessLevel.NONE )
private Map<String, File> filesMap = null;


@Override
public long getSize()
{
	return 0;
}

public String getDisplayName()
{
	return getName().substring( getPornaConfig().getStartingPrefix().length() );
}
private Map<String, File> getFilesMap()
{
	if ( filesMap == null )
	{
		Map<String, File> newMap = new HashMap<>();
		for ( File file : getFiles() )
		{
			newMap.put( file.getName(), file );
		}
		filesMap = newMap;
	}
	return filesMap;
}
public File getFile( String aFileName )
{
	return getFilesMap().get( aFileName );
}

}
