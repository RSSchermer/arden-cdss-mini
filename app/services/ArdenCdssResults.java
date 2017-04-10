package services;

import java.util.List;

public class ArdenCdssResults {
    private List<String> messages;

    private List<String> errors;

    ArdenCdssResults(List<String> messages, List<String> errors) {
        this.messages = messages;
        this.errors = errors;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getErrors() {
        return errors;
    }
}
