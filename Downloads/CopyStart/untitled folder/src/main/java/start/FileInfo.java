package start;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="Files")
public class FileInfo {
	
	@Id
	private int id;

	private String filename;

	private String url;

	@ManyToOne
	private Alien alien;

	public Alien getAlien() {
		return alien;
	}

	public FileInfo() {
		super();
	}

	public void setAlien(Alien alien) {
		this.alien = alien;
	}

	public FileInfo(String filename, String url) {
		this.filename = filename;
		this.url = url;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
