package pu.porna.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.Data;

@Configuration
@ConfigurationProperties (prefix = "mail")
@Data
public class MailConfig
{
private String host;
private String from;
private String to;
private String subject;
	
@Bean
public MailSender mailSender()
{
	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	mailSender.setHost( getHost() );
	return mailSender;
}
@Bean
public MailMessage templateMessage()
{
	MailMessage mailMessage = new SimpleMailMessage();
	mailMessage.setFrom( getFrom() );
	mailMessage.setTo( getTo() );
	mailMessage.setSubject( getSubject() );
	return mailMessage;
}
	
}
