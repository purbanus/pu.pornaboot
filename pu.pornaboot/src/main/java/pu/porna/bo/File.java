package pu.porna.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.transaction.annotation.Transactional;

import pu.porna.config.PornaConfig;
import pu.porna.dal.FlatDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Transactional
public class File implements FileSystemObject
{
public static File fromFlatDocument( FlatDocument aFlatDocument )
{
	return builder()
		.id( aFlatDocument.getFileId() )
		.name( aFlatDocument.getFileName() )
		.size( aFlatDocument.getFileSize() )
		.dateTimeLastModified( aFlatDocument.getFileDateTimeLastModified() )
		.review( aFlatDocument.getFileReview() )
		.kwaliteit( Kwaliteit.fromString( aFlatDocument.getFileKwaliteit() ) )
		.build();
}
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;

@Column( nullable = false )
private String name;

@Column( nullable = false )
private long size;

@Column( nullable = false )
private LocalDateTime dateTimeLastModified;

@ManyToOne(
	cascade = CascadeType.ALL,
	fetch = FetchType.LAZY
)
@JoinColumn(
	name = "directory_id",
	foreignKey = @ForeignKey( name = "FK_File_Directory" )
)
@EqualsAndHashCode.Exclude
private Directory directory;

@Column( nullable = true, length = 4096 )
private String review;

@Enumerated( EnumType.STRING )
private Kwaliteit kwaliteit;

@ManyToMany(
	cascade = CascadeType.MERGE // Anders gaat hij de properties, die al gesavet zijn, alsnog opslaan
)
@JoinTable(
	name = "file_property_map",
	joinColumns = @JoinColumn(
		// Both from the File class/table, I think
		name = "file_id",
		referencedColumnName = "id"
	),
	inverseJoinColumns = @JoinColumn(
		// Both from the Property class/table, I think
		name = "property_id",
		referencedColumnName = "id"
	)
)
@OnDelete( action = OnDeleteAction.CASCADE )
@Builder.Default
@EqualsAndHashCode.Exclude
@ToString.Exclude
private List<Property> properties = new ArrayList<>();

@Transient
@ToString.Exclude
@EqualsAndHashCode.Exclude
private PornaConfig pornaConfig;

public String getDirectoryDisplayName()
{
	return getDirectory().getDisplayName();
}
public List<String> getPropertiesAsStrings()
{
	return getProperties().stream()
		.map( p -> p.getName() )
		.collect( Collectors.toList() );
}
public String getPropertiesString()
{
	return String.join( ",", getPropertiesAsStrings() );
}
public String getVideoLocation()
{
	return "/videos/" + getDirectoryDisplayName() + "/" + getName();
}
}
