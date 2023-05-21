package pu.porna.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties (prefix = "porna")
@Data
public class PornaConfig
{
private String startingDirectory;
private String pornaFileName;

}
