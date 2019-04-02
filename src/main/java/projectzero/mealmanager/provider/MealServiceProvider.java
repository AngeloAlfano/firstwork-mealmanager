/**
 * 
 */
package projectzero.mealmanager.provider;

import static projectzero.mealmanager.utils.MealConstants.EMPTY_STRING;
import static projectzero.mealmanager.utils.MealConstants.RECORD_FORMAT;
import static projectzero.mealmanager.utils.MealConstants.STRING_MODEL;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import projectzero.mealmanager.model.PastiGiornalieriModel;
import projectzero.mealmanager.model.TipoAlimento;
import projectzero.mealmanager.model.alimenti.AlimentoModel;
import projectzero.mealmanager.model.alimenti.Carbs;
import projectzero.mealmanager.model.alimenti.Fat;
import projectzero.mealmanager.model.alimenti.Protein;
import projectzero.mealmanager.model.pasti.CenaModel;
import projectzero.mealmanager.model.pasti.ColazioneModel;
import projectzero.mealmanager.model.pasti.PastoModel;
import projectzero.mealmanager.model.pasti.PranzoModel;
import projectzero.mealmanager.service.MealServices;

/**
 * @author angelo.alfano
 *
 */
@Service
public class MealServiceProvider implements MealServices {

	/**
	 * The method creates a generic meal for one whole day, retrieving the food in random mode.
	 * 
	 * @param index
	 * @param pasti : {@linkplain List}<{@linkplain PastoModel}>
	 * @return the meal list {@link List}<{@link PastoModel}>
	 */
	@Override
	public List<PastoModel> generaPasto(int index, List<PastoModel> pasti) {
		if(index == 0 || index == 3 || index == 6 || index == 9 || index == 12 || index == 15 || index == 18) {
			ColazioneModel colazione = new ColazioneModel();
			colazione.setAlimenti(selezionaAlimenti());
			pasti.add(colazione);
		} else if(index == 1 || index == 4 || index == 7 || index == 10 || index == 13 || index == 16 || index == 19) {
			PranzoModel pranzo = new PranzoModel();
			pranzo.setAlimenti(selezionaAlimenti());
			pasti.add(pranzo);
		}else {
			CenaModel cena = new CenaModel();
			cena.setAlimenti(selezionaAlimenti());
			pasti.add(cena);
		}
		return pasti;
	}

	/**
	 * The method initializes the daily meal list for one week.
	 * 
	 * @param index
	 * @param pastiSettimanali : {@linkplain List}<{@linkplain PastiGiornalieriModel}>
	 */
	@Override
	public void initPasti(int index, List<PastiGiornalieriModel> pastiSettimanali) {
		BigDecimal newIndex = new BigDecimal(index).divide(new BigDecimal(3));
		
		for (int i = 0; i < newIndex.intValue() ; i++) {
			ColazioneModel colazione = new ColazioneModel();
			PranzoModel pranzo = new PranzoModel();
			CenaModel cena = new CenaModel();
			PastiGiornalieriModel pastoEnneismo =  new PastiGiornalieriModel();
			pastoEnneismo.setColazione(colazione);
			pastoEnneismo.setPranzo(pranzo);
			pastoEnneismo.setCena(cena);
			pastiSettimanali.add(pastoEnneismo);
		}
	}

	/**
	 * The method creates the records of one daily meal.
	 * 
	 * @param dailyMeal : {@linkplain PastiGiornalieriModel}
	 * @param buffer : {@linkplain BufferedWriter}
	 */
	@Override
	public void scritturaFile(PastiGiornalieriModel dailyMeal, BufferedWriter buffer, int dayNumber) {
		try {
			///////////////////////////////////////////////////////////
//			// Create a new document from scratch
//			XWPFDocument doc = new XWPFDocument();
//			// create paragraph
//			XWPFParagraph para = doc.createParagraph();
//			// create a run to contain the content
//			XWPFRun rh = para.createRun();
//			// Format as desired
//			rh.setFontSize(14);
//			rh.setFontFamily("TimesNewRoman");
//			rh.setText(String.format("GIORNO %d",Integer.valueOf(dayNumber)));
//			rh.setColor("fff000");
//			para.setAlignment(ParagraphAlignment.RIGHT);
//			// write the file
//			FileOutputStream out = new FileOutputStream("Pasti Settimanali.doc");
//			doc.write(out);
//			out.close();
			///////////////////////////////////////////////////////////
			if(dailyMeal != null) {
			buffer.write(String.format("GIORNO %d",Integer.valueOf(dayNumber)));
			buffer.newLine();
			ColazioneModel colazione = Optional.ofNullable(dailyMeal.getColazione()).orElse(null);
				buffer.write(colazione.getClass().getSimpleName().replace(STRING_MODEL, EMPTY_STRING).concat(":"));
				buffer.newLine();
				if (!colazione.getPastoLibero()) {
					createFileRecord(colazione, buffer);
				} else {
					buffer.write(TipoAlimento.PASTO_LIBERO.getValue());
					buffer.newLine();
				}
			PranzoModel pranzo = Optional.ofNullable(dailyMeal.getPranzo()).orElse(null);
			buffer.write(pranzo.getClass().getSimpleName().replace(STRING_MODEL, EMPTY_STRING).concat(":"));
			buffer.newLine();
				if (!pranzo.getPastoLibero()) {
					createFileRecord(pranzo, buffer);
				} else {
					buffer.write(TipoAlimento.PASTO_LIBERO.getValue());
					buffer.newLine();
				}
			CenaModel cena = Optional.ofNullable(dailyMeal.getCena()).orElse(null);
			buffer.write(cena.getClass().getSimpleName().replace(STRING_MODEL, EMPTY_STRING).concat(":"));
			buffer.newLine();
				if (!cena.getPastoLibero()) {
					createFileRecord(cena, buffer);
				} else {
					buffer.write(TipoAlimento.PASTO_LIBERO.getValue());
					buffer.newLine();
				}
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * The method creates the records of one meal.
	 * 
	 * @param pasto : {@linkplain PastoModel}
	 * @param buffer : {@linkplain BufferedWriter}
	 * @throws IOException
	 */
	private static void createFileRecord(PastoModel pasto, BufferedWriter buffer) throws IOException {
		
		List<AlimentoModel> alimenti = new ArrayList<>();
		ColazioneModel colazione;
		PranzoModel pranzo;
		CenaModel cena;
		if (pasto instanceof ColazioneModel) {
			colazione = (ColazioneModel) pasto;
			alimenti = colazione.getAlimenti();
		}else if (pasto instanceof PranzoModel) {
			pranzo = (PranzoModel) pasto;
			alimenti = pranzo.getAlimenti();
		}else if (pasto instanceof CenaModel) {
			cena = (CenaModel) pasto;
			alimenti = cena.getAlimenti();
		}
		buffer.write(String.format(RECORD_FORMAT, alimenti.get(0).getPeso(), alimenti.get(0).getNome(),
				alimenti.get(0).getCho(), alimenti.get(0).getTipo().getValue()));
		buffer.newLine();
		
		buffer.write(String.format(RECORD_FORMAT, alimenti.get(1).getPeso(), alimenti.get(1).getNome(),
				alimenti.get(1).getCho(), alimenti.get(1).getTipo().getValue()));
		buffer.newLine();
		if (pasto instanceof CenaModel) {
			buffer.write(String.format(RECORD_FORMAT, alimenti.get(2).getPeso(), alimenti.get(2).getNome(),
					alimenti.get(2).getCho(), alimenti.get(2).getTipo().getValue()));
			buffer.newLine();
		}
		buffer.newLine();
	}
	
	/**
	 * The method generate a random index from the input list.
	 * 
	 * @param alimenti : {@linkplain List}<{@linkplain AlimentoModel}>
	 * @return the random index.
	 */
	private static int generateRandomIndex(List<? extends AlimentoModel> alimenti) {
		return new BigDecimal(alimenti.size()).multiply(BigDecimal.valueOf(Math.random()))
											  .round(new MathContext(0, RoundingMode.HALF_EVEN))
										      .intValue();
	}
	
	/**
	 * The method retrieves the food from the initialized list, using a random index.
	 * 
	 */
	private static List<AlimentoModel> selezionaAlimenti() {
		
			List<AlimentoModel> result = new ArrayList<>();
			AlimentoModel alimento = new AlimentoModel();
			List<Carbs> carbs = alimento.initCarbs();
			List<Protein> proteins = alimento.initProtein();
			List<Fat> fats = alimento.initFat();
			
			int i = generateRandomIndex(carbs);
			int j = generateRandomIndex(proteins);
			int k = generateRandomIndex(fats);
			
			Carbs carb = carbs.get(i);
			Protein protein = proteins.get(j);
			Fat fat = fats.get(k);
			result.add(carb);
			result.add(protein);
			result.add(fat);
			return result;
	}
}
