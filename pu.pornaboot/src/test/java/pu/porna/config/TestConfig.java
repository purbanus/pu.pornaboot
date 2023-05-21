package pu.porna.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestConfig
{
@Autowired private MailConfig mailConfig;
@Autowired private PornaConfig pornaConfig;

@Test
public void testMailConfig()
{
	assertEquals( "gmail.com", mailConfig.getHost() );
	assertEquals( "porna.tst@pu.nl", mailConfig.getFrom() );
	assertEquals( "purbanus@gmail.com", mailConfig.getTo() );
	assertEquals( "Fout bij porna", mailConfig.getSubject() );
}
@Test
public void testPornaConfig()
{
	assertEquals( "/media/purbanus/5TB Seagate/Videos/vrouwen", pornaConfig.getStartingDirectory() );
	assertEquals( ".porna", pornaConfig.getPornaFileName() );
}
}
