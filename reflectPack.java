
import java.lang.reflect.Field;


public class reflectPack {

	private double[] fieldArrayValues;
	private String[] fieldArrayNames;
	Field[] fieldArray;
	
	/**
	 * @param f fields
	 * @param d field values
	 * @param s field names
	 * 
	 */
	public reflectPack(Field [] f,double [] d, String [] s)
	{
		
		fieldArrayValues = d;
		fieldArrayNames = s;
	}

	public double[] getFieldArrayValues() {
		return fieldArrayValues;
	}

	public void setFieldArrayValues(double[] genoArray) {
		this.fieldArrayValues = genoArray;
	}

	public Field[] getFieldArray() {
		return fieldArray;
	}

	public void setFieldArray(Field[] fieldArray) {
		this.fieldArray = fieldArray;
	}

	public String[] getFieldArrayNames() {
		return fieldArrayNames;
	}

	public void setFieldArrayNames(String[] genoArrayNames) {
		this.fieldArrayNames = genoArrayNames;
	}
	
	
}
