package start;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;



@Entity
@Table(name="Shared Flies")
public class ShareFiles {
	@Column
	private int aid;
	@Column
	@Id
	
	private int shared_aid;
    @Column
	private String url;
	
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ShareFiles(int aid, int shared_aid, String url) {
		super();
		this.aid = aid;
		this.shared_aid = shared_aid;
		this.url = url;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getShared_aid() {
		return shared_aid;
	}
	public void setShared_aid(int shared_aid) {
		this.shared_aid = shared_aid;
	}


}
