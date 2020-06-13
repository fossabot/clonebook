package nl.rubend.clonebook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.rubend.clonebook.exceptions.ClonebookException;
import nl.rubend.clonebook.utils.SqlInterface;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Media {
	private String id;
	private String location;
	private String ownerId;
	private String mime;
	private static File uploads = new File("/home/pi/clonebook/uploads");

	public static Media getMedia(String id) {
		try {
			PreparedStatement statement = SqlInterface.prepareStatement("SELECT * FROM media WHERE ID=?");
			statement.setString(1, id);
			ResultSet set = statement.executeQuery();
			set.next();
			return new Media(set.getString("ID"), set.getString("owner"), set.getString("location"), set.getString("mime"));
		} catch (SQLException e) {
			return null;
		}
	}

	public Media(String id, String ownerId, String location, String mime) {
		this.id = id;
		this.ownerId = ownerId;
		this.location = location;
		this.mime = mime;
	}

	public Media(InputStream file, String ownerId, String location) {
		this(UUID.randomUUID().toString(), ownerId, location, "");
		File destination = new File(uploads, id);
		try {
			Files.copy(file, destination.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mime = new Tika().detect(destination.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement statement = SqlInterface.prepareStatement("INSERT INTO media(ID,owner,location,mime) VALUES (?,?,?,?)");
			statement.setString(1, this.id);
			statement.setString(2, this.ownerId);
			statement.setString(3, this.location);
			statement.setString(4, this.mime);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ClonebookException(e.getMessage());
		}
	}

	@JsonIgnore
	public File getFile() {
		File file= new File(new File(uploads, location), id);
		if(!file.exists()) return null;
		return file;
	}
	@JsonIgnore
	public String getMime() {
		return this.mime;
	}

	public void setMime(String mime) {
		this.mime=mime;
		try {
			PreparedStatement statement = SqlInterface.prepareStatement("UPDATE media SET mime = ? WHERE ID = ?");
			statement.setString(1, this.mime);
			statement.setString(2, this.id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ClonebookException(e.getMessage());
		}
	}
	public String getId() {
		return this.id;
	}

	public String getOwnerId() {
		return this.ownerId;
	}
	@JsonIgnore
	public User getOwner() {
		return User.getUserById(this.ownerId);
	}

	public void delete() {
		try {
			PreparedStatement statement = SqlInterface.prepareStatement("DELETE FROM media WHERE ID=?");
			statement.setString(1, this.id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ClonebookException(e.getMessage());
		}
		try {
			Files.delete(getFile().toPath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClonebookException(e.getMessage());
		}
	}
	public void cropSquare() throws IOException {
		BufferedImage file=ImageIO.read(getFile());
		int height=file.getHeight();
		int width=file.getWidth();
		int size=Math.min(height, width);
		int x=(width-size)/2;
		int y=(height-size)/2;
		BufferedImage cropped=Scalr.crop(file,x,y,size,size);
		ImageIO.write(cropped,"jpg",getFile());
		this.setMime("image/jpeg");
	}
}