package services;

import arden.compiler.Compiler;
import arden.compiler.CompilerException;
import arden.runtime.*;
import arden.runtime.evoke.CallTrigger;
import arden.runtime.jdbc.JDBCQuery;
import play.Environment;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ArdenCdss {
    private Environment environment;

    private Database database;

    @Inject
    public ArdenCdss(Environment environment, Database database) {
        this.environment = environment;
        this.database = database;
    }

    public ArdenCdssResults getResults(String patientId) {
        final File mlmDirectory = environment.getFile("mlm");
        final List<File> mlmFiles;
        final List<String> messages = new ArrayList<>();
        final List<String> errors = new ArrayList<>();

        URL[] searchUrls = new URL[1];

        try {
            searchUrls[0] = mlmDirectory.toURL();
        } catch (MalformedURLException e) {
            errors.add("No `mlm` directory could be found.");

            return new ArdenCdssResults(new ArrayList<String>(), errors);
        }

        try {
            mlmFiles = Files.walk(Paths.get(mlmDirectory.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            errors.add("No `mlm` directory could be found.");

            return new ArdenCdssResults(new ArrayList<String>(), errors);
        }

        final List<MedicalLogicModule> compiledMlms = new ArrayList<>();

        for (File file : mlmFiles) {
            Compiler compiler = new Compiler();
            compiler.enableDebugging(file.getPath());

            try {
                compiledMlms.add(compiler.compileMlm(new FileReader(file.getPath())));
            } catch (CompilerException e) {
                errors.add("Could not compile " + file.getPath() + ": " + e.getMessage());
            } catch (FileNotFoundException e) {
                errors.add("File not found: " + file.getPath());
            } catch (IOException e) {
                errors.add("Could not read " + file.getPath());
            }
        }

        final Connection connection = database.getConnection();
        final ExecutionContext context = new ExecutionContext(patientId, searchUrls, connection);

        for (MedicalLogicModule mlm : compiledMlms) {
            try {
                ArdenValue[] results = mlm.run(context, new ArdenValue[0], new CallTrigger());

                if (results != null) {
                    for (ArdenValue result : results) {
                        messages.add(ArdenString.getStringFromValue(result));
                    }
                }
            } catch (InvocationTargetException e) {
                errors.add("Could not run MLM:" + e.getMessage());
            }
        }

        messages.addAll(context.getMessages());

        try {
            connection.close();
        } catch (SQLException ignored) {}

        return new ArdenCdssResults(messages, errors);
    }

    class ExecutionContext extends BaseExecutionContext
    {
        private String patientId;

        private Connection connection;

        private List<String> messages = new ArrayList<>();

        ExecutionContext(String patientId, URL[] mlmSearchPath, Connection connection) {
            super(mlmSearchPath);

            this.patientId = patientId;
            this.connection = connection;
        }

        public List<String> getMessages() {
            return messages;
        }

        public DatabaseQuery createQuery(MedicalLogicModule mlm, String mapping) {
            return new JDBCQuery(mapping.replaceAll("PATIENT_ID", patientId), connection);
        }

        public void write(ArdenValue message, ArdenValue destination, double urgency) {
            messages.add(message.toString());
        }
    }
}
