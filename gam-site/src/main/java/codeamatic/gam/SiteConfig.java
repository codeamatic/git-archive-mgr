package codeamatic.gam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * Main configuration resource for the GAM application.
 */
@Configuration
public class SiteConfig {

  @Autowired
  private MessageSource messageSource;

  @Bean
  public MessageSourceAccessor messageSourceAccessor() {
    return new MessageSourceAccessor(messageSource);
  }
}
