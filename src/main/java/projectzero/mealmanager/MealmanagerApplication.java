package projectzero.mealmanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import projectzero.mealmanager.model.PastiGiornalieriModel;
import projectzero.mealmanager.model.pasti.CenaModel;
import projectzero.mealmanager.model.pasti.ColazioneModel;
import projectzero.mealmanager.model.pasti.PastoModel;
import projectzero.mealmanager.model.pasti.PranzoModel;
import projectzero.mealmanager.provider.MealServiceProvider;
import projectzero.mealmanager.service.MealServices;

@SpringBootApplication
@EnableAutoConfiguration
public class MealmanagerApplication {

	private static final String FILE_PATH = "C:\\Users\\angelo.alfano\\Desktop\\firstwork\\Pasti Settimanali.doc";
	
//	@Autowired
//	private static MealServices services;
	
	/**
	 * Main method provides the application start
	 */
	public static void main(String[] args) {

		MealServiceProvider services = new MealServiceProvider();
		
		List<PastiGiornalieriModel> pastiSettimanali = new ArrayList<>();
		List<PastoModel> pasti = new ArrayList<>();
		services.initPasti(21, pastiSettimanali);
		for (int i = 0; i < 21; i++) {
			pasti = services.generaPasto(i, pasti);
		}
		int j = 0;
		for (int i = 0; i < pasti.size(); i+=3) {
			pastiSettimanali.get(j).setColazione((ColazioneModel) pasti.get(i));
			pastiSettimanali.get(j).setPranzo((PranzoModel) pasti.get(i+1));
			pastiSettimanali.get(j).setCena((CenaModel) pasti.get(i+2));
			j++;
		}
		
		String pathname = FILE_PATH;
		File file = new File(pathname);
		
		try (
			FileWriter writer = new FileWriter(file);
			BufferedWriter buffer = new BufferedWriter(writer);
			)
		{
		if(file.createNewFile()){
			System.out.println("File non creato, poiche gia esistente");
		}
		file = Files.copy(Paths.get(pathname), Paths.get(pathname)).toFile();
		int dayNumber = 0;
		for (PastiGiornalieriModel pasto : pastiSettimanali) {
			++dayNumber;
			services.scritturaFile(pasto, buffer, dayNumber);
		}
		buffer.close();
		writer.close();
		System.out.println(String.format("Scrittura del File %s completato", file.getName()));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

}
