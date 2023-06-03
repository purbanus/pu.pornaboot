package pu.porna.dal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import pu.porna.bo.Property;

@Repository
public class PropertyRepositoryHelper
{
@Autowired private PropertyRepository propertyRepository;

public Property createProperty( String aNaam )
{
	Property property = Property.builder()
		.name( aNaam )
		.build();
	Optional<Property> result = propertyRepository.findOne( Example.of( property ) );
	return result.isEmpty() ? propertyRepository.save( property ) : result.get();
}

}
