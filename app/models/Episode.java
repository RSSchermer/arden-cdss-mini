package models;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @Column(name = "iEpisodeId")
    public String id;

    @Column(name = "sEpisodeTitle")
    public String title;

    @Column(name = "sEpisodeICPC")
    public String icpcCode;

    @Column(name = "dEpisodeStartDate")
    private String startDateRaw;

    @Column(name = "dEpisodeEndDate")
    private String endDateRaw;

    @ManyToOne(optional=false)
    @JoinColumn(name="iPatientId", referencedColumnName="iPatientId")
    public Patient patient;

    public Date getStartDate() {
        if (startDateRaw == null) {
            return null;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                return dateFormat.parse(startDateRaw);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    public Date getEndDate() {
        if (endDateRaw == null) {
            return null;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                return dateFormat.parse(endDateRaw);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
