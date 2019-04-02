/**
 * 
 */
package projectzero.mealmanager.logic.service;

import java.io.BufferedWriter;
import java.util.List;

import projectzero.mealmanager.model.PastiGiornalieriModel;
import projectzero.mealmanager.model.pasti.PastoModel;


/**
 * @author angelo.alfano
 *
 */
public interface MealServices {
	
	public List<PastoModel> generaPasto (int index, List<PastoModel> pasti);
	
	public void initPasti(int index, List<PastiGiornalieriModel> pastiSettimanali);
	
	public void scritturaFile(PastiGiornalieriModel dailyMeal, BufferedWriter buffer, int dayNumber);

}
