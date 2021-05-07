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
    private String question;
    @NotNull
    private List<String> answers;

    private Map<String, CarRepairTree> outcomes;
    private String result;

    public CarRepairTree() {

    }

    public String getId() {
        return id;
    }

    public String getStep() {
        return step;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public Map<String, CarRepairTree> getOutcomes() {
        return outcomes;
    }

    public String getResult() {
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setOutcomes(Map<String, CarRepairTree> outcomes) {
        this.outcomes = outcomes;
    }

    public void setResult(String result) {
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