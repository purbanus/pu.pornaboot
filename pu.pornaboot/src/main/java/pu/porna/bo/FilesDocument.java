package pu.porna.bo;

import java.util.List;

import pu.porna.dal.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FilesDocument
{
private final List<File> files;

//public int getNumberOfFiles()
//{
//	return files.size();
//}
}
