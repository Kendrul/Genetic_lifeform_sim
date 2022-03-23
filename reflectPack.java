
import java.lang.reflect.Field;


public class reflectPack {

	private double[] fieldArrayValues;
	private String[] fieldArrayNames;
	private String[] faValues;
	Field[] fieldArray;
	
	/**
	 * @param f fields
	 * @param d field values
	 * @param s field names
	 * 
	 */
	public reflectPack(Field [] f,double [] d, String [] s)
	{
		Field[] fieldArray = f;	
		fieldArrayValues = d;
		fieldArrayNames = s;
	}
	
	public reflectPack(Field [] f,String [] d, String [] s)
	{
		Field[] fieldArray = f;	
		faValues = d;
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

	/**
	 * @return the faValues
	 */
	public String[] getFaValues() {
		return faValues;
	}

	/**
	 * @param faValues the faValues to set
	 */
	public void setFaValues(String[] faValues) {
		this.faValues = faValues;
	}
	
	
}
