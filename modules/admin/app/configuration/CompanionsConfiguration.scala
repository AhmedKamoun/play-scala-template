package configuration

import dto.TestWrites
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * This solution is used to be able to use autowired bean into Scala Object
 */
@Configuration
class CompanionsConfiguration {

  @Bean
  def getTestWrites() = TestWrites.getInstance()

}
