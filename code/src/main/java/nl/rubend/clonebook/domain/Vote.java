package nl.rubend.clonebook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.rubend.clonebook.UUIDGenerator;
import nl.rubend.clonebook.exceptions.ClonebookException;
import nl.rubend.clonebook.utils.SqlInterface;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Vote {
	@Id
	@GeneratedValue(generator= UUIDGenerator.generatorName)
	@GenericGenerator(name = UUIDGenerator.generatorName, strategy = "nl.rubend.clonebook.UUIDGenerator")
	private String id;
	@JsonIgnore
	@ManyToOne
	private Post post;
	@ManyToOne
	@JsonIgnore
	private User user;
	private int vote;
}
