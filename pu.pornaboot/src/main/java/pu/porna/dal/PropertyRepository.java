package pu.porna.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import pu.porna.bo.Property;

public interface PropertyRepository extends JpaRepository<Property, Integer>
{
Property findByName( String aNaam );
}
