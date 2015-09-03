package codeamatic.gam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The entry point for the Gam application.
 */
@EnableAutoConfiguration
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(SiteConfig.class, args);
  }
}