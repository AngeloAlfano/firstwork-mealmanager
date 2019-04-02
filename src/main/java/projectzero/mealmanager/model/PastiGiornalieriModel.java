/**
 * 
 */
package projectzero.mealmanager.model;

import projectzero.mealmanager.model.pasti.CenaModel;
import projectzero.mealmanager.model.pasti.ColazioneModel;
import projectzero.mealmanager.model.pasti.PranzoModel;

/**
 * @author angelo.alfano
 *
 */
public class PastiGiornalieriModel {
	
	private ColazioneModel colazione;
	private PranzoModel pranzo;
	private CenaModel cena;
	
	public ColazioneModel getColazione() {
		return colazione;
	}
	public void setColazione(ColazioneModel colazione) {
		this.colazione = colazione;
	}
	public PranzoModel getPranzo() {
		return pranzo;
	}
	public void setPranzo(PranzoModel pranzo) {
		this.pranzo = pranzo;
	}
	public CenaModel getCena() {
		return cena;
	}
	public void setCena(CenaModel cena) {
		this.cena = cena;
	}
}
