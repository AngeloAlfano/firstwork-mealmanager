/**
 * 
 */
package projectzero.mealmanager.model;

import java.util.Arrays;

/**
 * @author angelo.alfano
 *
 */
public enum TipoAlimento {
	
	CARBS("carboidrati"),
	FAT("grassi"),
	PROTEIN("proteine"),
	PASTO_LIBERO("Pasto Libero");
	
	private String value;
	
	private TipoAlimento(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static TipoAlimento parse (String value) {
		return Arrays.asList(values()).stream()
				.filter(type -> value
						.equals(type.getValue()))
				.findFirst()
				.orElseThrow(()-> new EnumConstantNotPresentException(TipoAlimento.class, value));
	}

}
