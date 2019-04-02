/**
 * 
 */
package projectzero.mealmanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import projectzero.mealmanager.logic.provider.MealServiceProvider;
import projectzero.mealmanager.logic.service.MealServices;

/**
 * @author angelo.alfano
 *
 */
@Configuration
public class BootAutoConfiguration {

	@Bean
	public MealServices mealService() {
		return new MealServiceProvider();
	}

}
