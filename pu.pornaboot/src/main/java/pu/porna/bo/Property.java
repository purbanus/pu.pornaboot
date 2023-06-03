package pu.porna.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import pu.porna.dal.FlatDocument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor( access=AccessLevel.PRIVATE, force=true )
@Builder
@Entity
@Table( uniqueConstraints = { @UniqueConstraint( name = "UN_Property_Name", columnNames = { "name" } ) })
public class Property
{
public static Property fromFlatDocument( FlatDocument aFlatDocument )
{
	return builder()
		.id( aFlatDocument.getPropertyId() )
		.name( aFlatDocument.getPropertyName() )
		.build();
}
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;

@NotNull
@Column( nullable = false )
private String name;


}
