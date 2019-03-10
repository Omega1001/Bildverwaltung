package bildverwaltung.dao.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;

@Entity
@NamedQueries(value = {
		/**/
		@NamedQuery(name = "album.all_id_name", query = "SELECT e.id, e.name FROM Album e")
		/**/
})
public class Album extends UUIDBase {

	private String name;
	private List<Picture> pictures = new LinkedList<>();
	private Date creationDate = new Date();
	private String comment;

	public Album(String name, List<Picture> pictures, Date creationDate, String comment) {
		super();
		this.name = name;
		this.pictures = pictures;
		this.creationDate = creationDate;
		this.comment = comment;
	}

	public Album() {
		super();
	}

	/**
	 * @return the name
	 */
	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pictures
	 */
	@ManyToMany(cascade= CascadeType.ALL)
	@JoinTable
	@OrderBy("name ASC")
	public List<Picture> getPictures() {
		return pictures;
	}

	/**
	 * @param pictures
	 *            the pictures to set
	 */
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	/**
	 * @return the creationDate
	 */
	@Column(nullable = false)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
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
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Album [getId()=").append(getId()).append(", name=").append(name).append("]");
		return builder.toString();
	}

}
