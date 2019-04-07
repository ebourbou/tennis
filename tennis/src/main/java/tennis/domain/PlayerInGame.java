package tennis.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerInGame {
    
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	public @Getter Integer id;
	
	@OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public @NonNull @Getter Player player;
	
	@Enumerated(EnumType.STRING)
    public @NonNull @Setter @Getter TennisPlayerScore score = TennisPlayerScore.LOVE;

}