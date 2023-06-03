package pu.porna.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pu.porna.bo.File;

public interface FileRepository extends JpaRepository<File, Integer>
{
// @@NOG Dit moet toch automatisch kunnen, zonder @Query?
@Query( value = "select f from File f order by name" )
List<File> getAllFilesOrderedByName();

@Query( value = "select f from File f order by id" )
List<File> getAllFilesOrderedById();

@Modifying
@Query("update File f set f.review = :review where f.id = :id")
void updateFileReview( @Param( value = "id" ) int id, @Param( value = "review" ) String review );
}
