package com.andriynev.driver_helper_bot.dto;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CarRepairTree {
    @NotNull
    @Id
    private String id;
    @NotNull
    private String step;
    @NotNull
    private Map<String, String> question;
    @NotNull
    private Map<String, Map<String, String>> answers;

    private Map<String, CarRepairTree> outcomes;
    private Map<String, String> result;

    public CarRepairTree() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Map<String, String> getQuestion() {
        return question;
    }

    public void setQuestion(Map<String, String> question) {
        this.question = question;
    }

    public Map<String, Map<String, String>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Map<String, String>> answers) {
        this.answers = answers;
    }

    public Map<String, CarRepairTree> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(Map<String, CarRepairTree> outcomes) {
        this.outcomes = outcomes;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CarRepairTree{" +
                "id='" + id + '\'' +
                ", step='" + step + '\'' +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                ", outcomes=" + outcomes +
                ", result='" + result + '\'' +
                '}';
    }
}