package pu.porna.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig
{
	@Value( "${porna.mail.host}" )
	private String mailHost;
	@Value( "${porna.mail.from}" )
	private String mailFrom;
	@Value( "${porna.mail.to}" )
	private String mailTo;
	@Value( "${porna.mail.subject}" )
	private String mailSubject;
	
	@Bean
	public MailSender mailSender()
	{
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost( mailHost );
		return mailSender;
	}
	@Bean
	public MailMessage templateMessage()
	{
		MailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom( mailFrom );
		mailMessage.setTo( mailTo );
		mailMessage.setSubject( mailSubject );
		return mailMessage;
	}
	
}
