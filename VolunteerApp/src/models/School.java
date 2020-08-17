/**
 * 
 */
package models;

/**
 * @author Eva Rubio
 *
 */
public class School {
	
	
	private int id_school;
	private int name_school;
	
	
	public School(int id_school, int name_school) {
		this.id_school = id_school;
		this.name_school = name_school;
	}

	
	
	public int getId_school() {
		return id_school;
	}
	public void setId_school(int id_school) {
		this.id_school = id_school;
	}
	public int getName_school() {
		return name_school;
	}
	public void setName_school(int name_school) {
		this.name_school = name_school;
	}
	
	@Override
	public String toString() {
		return "School [id_school=" + id_school + ", name_school=" + name_school + "]";
	}

}
