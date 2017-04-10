package controllers;

import models.Patient;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.ArdenCdss;
import services.ArdenCdssResults;
import views.html.patient_details;
import views.html.patient_list;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class PatientsController extends Controller {
    private JPAApi jpaApi;

    private ArdenCdss ardenCdss;

    @Inject
    public PatientsController(JPAApi api, ArdenCdss ardenCdss) {
        this.jpaApi = api;
        this.ardenCdss = ardenCdss;
    }

    @Transactional(readOnly=true)
    public Result list() {
        List<Patient> patients = jpaApi.em().createNamedQuery("Patient.findAll").getResultList();

        return ok(patient_list.render(patients));
    }

    @Transactional(readOnly=true)
    public Result details(String id) {
        Patient patient = jpaApi.em().find(Patient.class, id);
        ArdenCdssResults cdssResults = ardenCdss.getResults(id);

        return ok(patient_details.render(patient, cdssResults));
    }
}
