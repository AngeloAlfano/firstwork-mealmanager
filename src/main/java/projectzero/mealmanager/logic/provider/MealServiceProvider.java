/**
 * 
 */
package projectzero.mealmanager.logic.provider;

import static projectzero.mealmanager.utils.MealConstants.EMPTY_STRING;
import static projectzero.mealmanager.utils.MealConstants.LOGO;
import static projectzero.mealmanager.utils.MealConstants.RECORD_FORMAT;
import static projectzero.mealmanager.utils.MealConstants.STRING_MODEL;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import projectzero.mealmanager.logic.service.MealServices;
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

/**
 * @author angelo.alfano
 *
 */
@Service
public class MealServiceProvider implements MealServices {

	/**
	 * The method creates a generic meal for one whole day, retrieving the food in
	 * random mode.
	 * 
	 * @param index
	 * @param pasti
	 *            : {@linkplain List}<{@linkplain PastoModel}>
	 * @return the meal list {@link List}<{@link PastoModel}>
	 */
	@Override
	public List<PastoModel> generaPasto(int index, List<PastoModel> pasti) {
		if (index == 0 || index == 3 || index == 6 || index == 9 || index == 12 || index == 15 || index == 18) {
			ColazioneModel colazione = new ColazioneModel();
			colazione.setAlimenti(selezionaAlimenti());
			pasti.add(colazione);
		} else if (index == 1 || index == 4 || index == 7 || index == 10 || index == 13 || index == 16 || index == 19) {
			PranzoModel pranzo = new PranzoModel();
			pranzo.setAlimenti(selezionaAlimenti());
			pasti.add(pranzo);
		} else {
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
	 * @param pastiSettimanali
	 *            : {@linkplain List}<{@linkplain PastiGiornalieriModel}>
	 */
	@Override
	public void initPasti(int index, List<PastiGiornalieriModel> pastiSettimanali) {
		BigDecimal newIndex = new BigDecimal(index).divide(new BigDecimal(3));

		for (int i = 0; i < newIndex.intValue(); i++) {
			ColazioneModel colazione = new ColazioneModel();
			PranzoModel pranzo = new PranzoModel();
			CenaModel cena = new CenaModel();
			PastiGiornalieriModel pastoEnneismo = new PastiGiornalieriModel();
			pastoEnneismo.setColazione(colazione);
			pastoEnneismo.setPranzo(pranzo);
			pastoEnneismo.setCena(cena);
			pastiSettimanali.add(pastoEnneismo);
		}
	}

	/**
	 * The method creates the records of one daily meal.
	 * 
	 * @param dailyMeal
	 *            : {@linkplain PastiGiornalieriModel}
	 * @param buffer
	 *            : {@linkplain BufferedWriter}
	 */
	@Override
	public void scritturaFile(PastiGiornalieriModel dailyMeal, BufferedWriter buffer, int dayNumber) {
		try {
			if (dailyMeal != null) {
				buffer.write(String.format("GIORNO %d", Integer.valueOf(dayNumber)));
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
	 * @param pasto
	 *            : {@linkplain PastoModel}
	 * @param buffer
	 *            : {@linkplain BufferedWriter}
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
		} else if (pasto instanceof PranzoModel) {
			pranzo = (PranzoModel) pasto;
			alimenti = pranzo.getAlimenti();
		} else if (pasto instanceof CenaModel) {
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
	 * @param alimenti
	 *            : {@linkplain List}<{@linkplain AlimentoModel}>
	 * @return the random index.
	 */
	private static int generateRandomIndex(List<? extends AlimentoModel> alimenti) {
		return new BigDecimal(alimenti.size()).multiply(BigDecimal.valueOf(Math.random()))
				.round(new MathContext(0, RoundingMode.HALF_EVEN)).intValue();
	}

	/**
	 * The method retrieves the food from the initialized list, using a random
	 * index.
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

	public void generateBodyDoc() {
		try (
				// create word.doc
				XWPFDocument document = new XWPFDocument();) {
			// Insert image
			Path imagePath = Paths.get(LOGO);
			configImage(document, ParagraphAlignment.RIGHT, imagePath, XWPFDocument.PICTURE_TYPE_PNG, 75, 75);
			// create a document's paragraph and format paragraph
			// Title
			String titleText = "Titolo di prova";
			XWPFParagraph title = prepareParagraph(document, ParagraphAlignment.CENTER, null, null);
			XWPFRun titleRun = configText(title, titleText, null, null, 24);
			titleRun.setBold(true);

			// Sub-Title
			String subtitleText = "Sottotitolo di prova";
			XWPFParagraph subtitle = prepareParagraph(document, ParagraphAlignment.CENTER, null, 150);
			XWPFRun subtitleRun = configText(subtitle, subtitleText, null, null, 14);
			subtitleRun.setBold(true);
			subtitleRun.setItalic(true);

			// Header
			String headerText = "Testo di prova per un possibile inserimento di un \"header\" di default.";
			XWPFParagraph header = prepareParagraph(document, ParagraphAlignment.LEFT, null, 250);
			configText(header, headerText, null, null, 12);

			// Main text
			String lineText = "Testo di prova che deve comporre il corpo del file Word.";
			for (int i = 0; i < 10; i++) {
				XWPFParagraph genericLine = prepareParagraph(document, ParagraphAlignment.LEFT, null, null);
				configText(genericLine, lineText, null, null, 12);
			}

			// Footer
			String footerText = "Testo di prova per un possibile inserimento di un \"footer\" di default.";
			XWPFParagraph footer = prepareParagraph(document, ParagraphAlignment.LEFT, 250, null);
			configText(footer, footerText, null, null, 12);

			// Signature
			String signatureText = "Nome Cognome";
			XWPFParagraph signature = prepareParagraph(document, ParagraphAlignment.RIGHT, 1000, null);
			configText(signature, signatureText, null, null, 12);
			// write the file
			FileOutputStream out = new FileOutputStream("Prova Font.doc");
			document.write(out);
			document.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Helper configuration method to insert image.
	 * 
	 * @param document
	 *            : {@linkplain XWPFDocument}
	 * @param alignment
	 *            : {@linkplain ParagraphAlignment}
	 * @param imagePath
	 *            : {@linkplain Path}
	 * @param imgExtension
	 *            : int
	 * @param dimX
	 *            : int
	 * @param dimY
	 *            : int
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private static void configImage(XWPFDocument document, ParagraphAlignment alignment, Path imagePath,
			int imgExtension, int dimX, int dimY) throws IOException, InvalidFormatException {
		XWPFParagraph image = document.createParagraph();
		image.setAlignment(alignment);
		XWPFRun imageRun = image.createRun();
		InputStream inStream = Files.newInputStream(imagePath);
		imageRun.addPicture(inStream, imgExtension, imagePath.getFileName().toString(), Units.toEMU(dimX),
				Units.toEMU(dimY));
	}

	/**
	 * Helper configuration method to text's format.
	 * 
	 * @param paragraph
	 *            : {@linkplain XWPFParagraph}
	 * @param text
	 *            : {@linkplain String}
	 * @param colorInExaDec
	 *            : {@linkplain String}
	 * @param font
	 *            : {@linkplain String}
	 * @param size
	 *            : int
	 * @return the configured Object: {@link XWPFRun}
	 */
	private static XWPFRun configText(XWPFParagraph paragraph, String text, String colorInExaDec, String font,
			int size) {
		XWPFRun run = paragraph.createRun();
		run.setText(text);
		run.setColor(colorInExaDec == null ? "000000" : colorInExaDec);
		run.setFontFamily(font == null ? "TimesNewRoman" : font);
		run.setFontSize(size);
		return run;
	}
	// , boolean bold, boolean italic, boolean underline

	/**
	 * Helper paragraph configuration method.
	 * 
	 * @param document
	 *            : {@linkplain XWPFDocument}
	 * @param alignment
	 *            : {@linkplain ParagraphAlignment}
	 * @param spaceBefore
	 *            : {@linkplain Integer}
	 * @param spaceAfter
	 *            : {@linkplain Integer}
	 * 
	 * @return the paragraph.
	 */
	private static XWPFParagraph prepareParagraph(XWPFDocument document, ParagraphAlignment alignment,
			Integer spaceBefore, Integer spaceAfter) {
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(alignment);
		paragraph.setSpacingBefore(spaceBefore == null ? 0 : spaceBefore);
		paragraph.setSpacingAfter(spaceAfter == null ? 0 : spaceAfter);
		return paragraph;
	}
}
