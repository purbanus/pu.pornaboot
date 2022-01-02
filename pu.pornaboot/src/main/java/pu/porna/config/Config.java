package pu.porna.config;

/*
 * Reden om de properties in een ouderwetse XML laden:
 * - Er zit een bug in Spring waardoor je minstens ��n propertyfile in XML moet laden, anders laadt hij niets
 */

//@Configuration
//@ComponentScan(basePackages = { "pu.porna.bo", "pu.porna.dal", "pu.porna.service" } )
////@Import( MailConfig.class ) // @@NOG Is dit nu nodig of vindt die component-scan hem vanzelf ook wel? // ==> Lijkt mij niet
//@EnableScheduling
public class Config
{
//    @Configuration
//    @Profile(value = "local")
//    @ImportResource( "classpath:/local/IntegrationTestContext.xml" )
//    //@PropertySource( "classpath:/local/scheduling.properties" )
//    public static class LocalConfiguration
//    {
//    	// Dit is een heuse @Configuration class dus je kunt er beans inzetten etc
//    	// Bijvoorbeeld een simpele DataSource
//    }
//    @Configuration
//    @Profile(value = "!local")
//    @ImportResource( "WEB-INF/ApplicationContext.xml" )
//    //@PropertySource( "WEB-INF/scheduling.properties" )
//    public static class NonLocalConfiguration
//    {
//    	// Dit is een heuse @Configuration class dus je kunt er beans inzetten etc
//    	// Bijvoorbeeld een complexe DataSource
//    }
}
