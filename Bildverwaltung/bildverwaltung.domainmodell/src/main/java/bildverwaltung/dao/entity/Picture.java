package bildverwaltung.dao.entity;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Picture extends UUIDBase {
	
	private String name;
	private URI uri;
	private List<Album> alben;
	private String extension;
	private int heigth;
	private int width;
	private Date creationDate = new Date();
	private String comment;
	private Byte rating;

	public Picture(String name, URI uri, List<Album> alben,
			String extension, int heigth, int with, Date creationDate,
			String comment, Byte rating) {
		super();
		this.name = name;
		this.uri = uri;
		this.alben = alben;
		this.extension = extension;
		this.heigth = heigth;
		this.width = with;
		this.creationDate = creationDate;
		this.comment = comment;
		this.rating = rating;
	}



	public Picture() {
		super();
	}



	/**
	 * @return the name
	 */
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the uri
	 */
	@Column(nullable= false)
	public URI getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * @return the alben
	 */
	@ManyToMany(mappedBy = "pictures", cascade= {CascadeType.DETACH,CascadeType.REFRESH})
	public List<Album> getAlben() {
		return alben;
	}

	/**
	 * @param alben the alben to set
	 */
	public void setAlben(List<Album> alben) {
		this.alben = alben;
	}

	/**
	 * @return the extension
	 */
	@Column(nullable = false)
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the heigth
	 */
	@Column(nullable = false)
	public int getHeigth() {
		return heigth;
	}

	/**
	 * @param heigth the heigth to set
	 */
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	/**
	 * @return the with
	 */
	@Column(nullable = false)
	public int getWidth() {
		return width;
	}

	/**
	 * @param with the with to set
	 */
	public void setWidth(int with) {
		this.width = with;
	}

	/**
	 * @return the creationDate
	 */
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the comment
	 */
	@Column(nullable = true)
	@Lob
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the rating
	 */
	@Column(nullable = false)
	public Byte getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Byte rating) {
		this.rating = rating;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Picture [getId()=").append(getId()).append(", name=").append(name).append(", alben=")
				.append(alben).append(", uri=").append(uri).append("]");
		return builder.toString();
	}




}
