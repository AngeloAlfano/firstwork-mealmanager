/**
 * 
 */
package projectzero.mealmanager.model.alimenti;

import projectzero.mealmanager.model.TipoAlimento;

/**
 * @author angelo.alfano
 *
 */
public class Protein extends AlimentoModel {

	public Protein(Integer cho, boolean influenzaAltriAlimenti, String nome, Double percInfluenza, Integer peso, TipoAlimento tipo) {
		super(cho, influenzaAltriAlimenti, nome, percInfluenza, peso, tipo);
	}
	
}
