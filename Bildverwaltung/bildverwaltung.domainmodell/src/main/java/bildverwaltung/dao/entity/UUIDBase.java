package bildverwaltung.dao.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class UUIDBase {
	
	private UUID id;
	private Date lastUpdate;
	
	public UUIDBase() {
		super();
		this.id = UUID.randomUUID();
		this.lastUpdate = new Date();
	}
	
	public UUIDBase(UUID id, Date lastUpdate) {
		super();
		this.id = id;
		this.lastUpdate = lastUpdate;
	}
	/**
	 * @return the id
	 */
	@Id
	@Column(length = 36)
	public UUID getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}
	/**
	 * @return the lastUpdate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getLastUpdate() {
		return lastUpdate;
	}
	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}	
}
