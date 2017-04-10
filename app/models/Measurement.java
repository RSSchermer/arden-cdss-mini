package models;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class Measurement {
    @Column(name = "sMemoCode")
    public String memoCode;

    @Column(name = "iLabCode")
    public String labCode;

    @Column(name = "sDescription")
    public String description;

    @Column(name = "sDescriptionShort")
    public String descriptionShort;

    @Column(name = "sMVResult")
    public String measuredValue;

    @Column(name = "iLowLimit")
    public String lowerLimitOfNormal;

    @Column(name = "iHighLimit")
    public String higherLimitOfNormal;

    @Column(name = "sMaterial")
    public String material;

    @Column(name = "sReqDate")
    private String dateRaw;

    @Column(name = "bNotNormal")
    public boolean abnormal;

    public Date getDate() {
        if (dateRaw == null) {
             return null;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                return dateFormat.parse(dateRaw);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
