package projectzero.mealmanager.model.pasti;

import java.util.List;

import projectzero.mealmanager.model.alimenti.AlimentoModel;

/**
 * @author angelo.alfano
 *
 */
public class PastoModel {

    private List<AlimentoModel> alimenti;
    private boolean pastoLibero;
    
    public List<AlimentoModel> getAlimenti() {
        return this.alimenti;
    }

    public void setAlimenti(  List<AlimentoModel> inAlimenti) {
        this.alimenti = inAlimenti;
    }
    
    public boolean getPastoLibero() {
        return this.pastoLibero;
    }

    public void setPastoLibero( boolean inPastoLibero) {
        this.pastoLibero = inPastoLibero;
    }
}
