package pu.porna.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pu.porna.bo.Directory;

public interface DirectoryRepository extends JpaRepository<Directory, Integer>
{
@Query( value = "SELECT count(*) from Directory d" )
int getDirectoryCount();
}
