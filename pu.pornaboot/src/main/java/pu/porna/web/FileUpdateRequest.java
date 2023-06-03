package pu.porna.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateRequest
{
private String directory;
private String fileName;
private String kwaliteit;
// @@NOG Moet dat niet een List<String> zijn?
private String property;
private String review;
}
