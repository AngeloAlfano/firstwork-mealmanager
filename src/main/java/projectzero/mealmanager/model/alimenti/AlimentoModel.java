/**
 * 
 */
package projectzero.mealmanager.model.alimenti;

import java.util.ArrayList;
import java.util.List;

import projectzero.mealmanager.model.TipoAlimento;


/**
 * @author angelo.alfano
 *
 */
public class AlimentoModel {

	private static final String PASTO_LIBERO = "Pasto Libero";
	
	private Integer cho;
	private boolean influenzaAltriAlimenti;
    private String nome;
    private Double percInfluenza;
    private Integer peso;
    private TipoAlimento tipo;
    
	public AlimentoModel() {
	}

	public AlimentoModel(Integer cho, boolean influenzaAltriAlimenti, String nome, Double percInfluenza,
			Integer peso, TipoAlimento tipo) {
		this.cho = cho;
		this.influenzaAltriAlimenti = influenzaAltriAlimenti;
		this.nome = nome;
		this.percInfluenza = percInfluenza;
		this.peso = peso;
		this.tipo = tipo;
	}
	
public List<Carbs> initCarbs() {
		
		Carbs galletteEMiele = new Carbs(12, false, "Gallette", 1.0, 50, TipoAlimento.CARBS);
		Carbs miele = new Carbs(34, false, "Miele", 1.0, 34, TipoAlimento.CARBS);
		Carbs banana = new Carbs(50, false, "Banana", 1.0, 100, TipoAlimento.CARBS);
		Carbs fichiSecchi = new Carbs(50, false, "Fichi Secchi", 1.0, 100, TipoAlimento.CARBS);
		Carbs uva = new Carbs(50, false, "Uva", 1.0, 100, TipoAlimento.CARBS);
		Carbs prugne = new Carbs(50, false, "Prugne", 1.0, 100, TipoAlimento.CARBS);
		Carbs succaArancia = new Carbs(50, false, "Succo d'arancia (circa 250 ml)", 1.0, 250, TipoAlimento.CARBS);
		
		List<Carbs> carbs = new ArrayList<>();
		carbs.add(galletteEMiele);
		carbs.add(miele);
		carbs.add(banana);
		carbs.add(fichiSecchi);
		carbs.add(uva);
		carbs.add(prugne);
		carbs.add(succaArancia);
		carbs.add(prugne);
		return carbs;
	}
	
	public List<Protein> initProtein() {
		
		Protein uova = new Protein(20, false, "Uova (circa 3 pz.)", 1.0, 60, TipoAlimento.PROTEIN);
		Protein yogurt = new Protein(20, false, "Yogurt Greco Fage 0%", 1.0, 220, TipoAlimento.PROTEIN);
		Protein prosciutto = new Protein(20, false, "Prosciutto Crudo", 1.0, 80, TipoAlimento.PROTEIN);
		
		List<Protein> proteins = new ArrayList<>();
		proteins.add(uova);
		proteins.add(yogurt);
		proteins.add(prosciutto);
		return proteins;
	}
	
	public List<Fat> initFat() {
		
		Fat noci = new Fat(null, false, "Noci (circa 2 pz.)", 1.0, 10, TipoAlimento.FAT);
		Fat mandorle = new Fat(null, false, "Mandorle (circa 20 pz.)", 1.0, 20, TipoAlimento.FAT);
		Fat avocado = new Fat(null, false, "Avocado (circa 1/2 pz.)", 1.0, 120, TipoAlimento.FAT);
		
		List<Fat> fats = new ArrayList<>();
		fats.add(noci);
		fats.add(mandorle);
		fats.add(avocado);
		return fats;
	}
	
	public Integer getCho() {
		return cho;
	}

	public void setCho(Integer cho) {
		this.cho = cho;
	}

	public boolean isInfluenzaAltriAlimenti() {
		return influenzaAltriAlimenti;
	}
	
	public void setInfluenzaAltriAlimenti(boolean influenzaAltriAlimenti) {
		this.influenzaAltriAlimenti = influenzaAltriAlimenti;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Double getPercInfluenza() {
		return percInfluenza;
	}
	
	public void setPercInfluenza(Double percInfluenza) {
		this.percInfluenza = percInfluenza;
	}
	
	public Integer getPeso() {
		return peso;
	}
	
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public TipoAlimento getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoAlimento tipo) {
		this.tipo = tipo;
	}
	
}
