/**
 * 
 */
package projectzero.mealmanager.model.alimenti;

import projectzero.mealmanager.model.TipoAlimento;

/**
 * @author angelo.alfano
 *
 */
public class Fat extends AlimentoModel {

	public Fat(Integer cho, boolean influenzaAltriAlimenti, String nome, Double percInfluenza, Integer peso, TipoAlimento tipo) {
		super(cho, influenzaAltriAlimenti, nome, percInfluenza, peso, tipo);
	}
	
}
