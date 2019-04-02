/**
 * 
 */
package projectzero.mealmanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import projectzero.mealmanager.provider.MealServiceProvider;
import projectzero.mealmanager.service.MealServices;


/**
 * @author angelo.alfano
 *
 */
@Configuration
public class MealConfiguration {
	
	@Bean
    public MealServices mealService() {
        return new MealServiceProvider();
    } 

}
