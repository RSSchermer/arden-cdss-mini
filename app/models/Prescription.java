package models;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class Prescription {
    @Column(name = "sLabel")
    public String label;

    @Column(name = "sATCCode")
    public String atcCode;

    @Column(name = "sDosingCode")
    public String dosingCode;

    @Column(name = "dPrescriptionDate")
    private String startDateRaw;

    @Column(name = "dPrescriptionEndDate")
    private String endDateRaw;

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
