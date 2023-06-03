package pu.porna.dal;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pu.porna.bo.Directory;

public interface DirectoryQueryRepository extends CrudRepository<Directory, Integer>
{
@Query( value = """
SELECT
  directory.id AS DIRECTORY_ID
, directory.name AS DIRECTORY_NAME
, directory.date_time_last_modified AS DIRECTORY_DATE_TIME_LAST_MODIFIED
, directory.parent_id AS DIRECTORY_PARENT_ID
, subdirectory.id AS SUB_DIRECTORY_ID
, subdirectory.name AS SUB_DIRECTORY_NAME
, subdirectory.date_time_last_modified AS SUB_DIRECTORY_DATE_TIME_LAST_MODIFIED
, subdirectory.parent_id AS SUB_DIRECTORY_PARENT_ID
, file.id AS FILE_ID
, file.name as FILE_NAME
, file.size as FILE_SIZE
, file.date_time_last_modified AS FILE_DATE_TIME_LAST_MODIFIED
, file.review as FILE_REVIEW
, file.kwaliteit as FILE_KWALITEIT
, property.id AS PROPERTY_ID
, property.name as PROPERTY_NAME
from directory
left outer join directory subdirectory
	on directory.id = subdirectory.parent_id
left outer join file
	on directory.id = file.directory_id
left outer join file_property_map
	on file.id = file_property_map.file_id
left outer join property
	on property.id = file_property_map.property_id
order by directory.id, subdirectory.id, file.id
""", nativeQuery = true )
List<Tuple> getAllDirectoryTuples();

@Query( value = """
SELECT
  directory.id AS DIRECTORY_ID
, directory.name AS DIRECTORY_NAME
, directory.date_time_last_modified AS DIRECTORY_DATE_TIME_LAST_MODIFIED
, directory.parent_id AS DIRECTORY_PARENT_ID
, subdirectory.id AS SUB_DIRECTORY_ID
, subdirectory.name AS SUB_DIRECTORY_NAME
, subdirectory.date_time_last_modified AS SUB_DIRECTORY_DATE_TIME_LAST_MODIFIED
, subdirectory.parent_id AS SUB_DIRECTORY_PARENT_ID
, file.id AS FILE_ID
, file.name as FILE_NAME
, file.size as FILE_SIZE
, file.date_time_last_modified AS FILE_DATE_TIME_LAST_MODIFIED
, file.review as FILE_REVIEW
, file.kwaliteit as FILE_KWALITEIT
, property.id AS PROPERTY_ID
, property.name as PROPERTY_NAME
from directory
left outer join directory subdirectory
	on directory.id = subdirectory.parent_id
left outer join file
	on directory.id = file.directory_id
left outer join file_property_map
	on file.id = file_property_map.file_id
left outer join property
	on property.id = file_property_map.property_id
where directory.id = (:id)
order by directory.id, subdirectory.id, file.id
""", nativeQuery = true )
List<Tuple> getDirectoryTuplesById( @Param( "id" ) int aId );
}
