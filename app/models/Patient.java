package models;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patients")
@NamedQuery(name="Patient.findAll", query="SELECT c FROM Patient c")
public class Patient {
    @Id
    @Column(name = "iPatientId")
    public String id;

    @Column(name = "sPatSex")
    public String sex;

    @Column(name = "dPatBirthDate")
    public String dateOfBirthRaw;

    @ElementCollection
    @CollectionTable(
            name = "patient_prescriptions",
            joinColumns = @JoinColumn(name = "iPatientId")
    )
    public List<Prescription> prescriptions;

    @ElementCollection
    @CollectionTable(
            name = "measurement_values",
            joinColumns = @JoinColumn(name = "iPatientId")
    )
    public List<Measurement> measurements;

    @OneToMany(mappedBy = "patient", targetEntity = Episode.class)
    public List<Episode> episodes;

    public Date getDateOfBirth() {
        if (dateOfBirthRaw == null) {
            return null;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                return dateFormat.parse(dateOfBirthRaw);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
