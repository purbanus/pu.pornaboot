package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import pu.porna.bo.Property;

@SpringBootTest
@Transactional
class TestPropertyRepository
{
@Autowired private PropertyRepository repository;

@Test
public void testGetAll()
{
	List<Property> properties = repository.findAll( Sort.by( "name" ) );
	assertEquals( 3, properties.size() );
	assertEquals( "anal", properties.get( 0 ).getName() );
	assertEquals( "asian", properties.get( 1 ).getName() );
	assertEquals( "bdsm", properties.get( 2 ).getName() );
}
}
