package com.soldierrecoveryunit.SRUmain.models;

public enum MilitaryGrades {
    E1("E-1"),
    E2("E-2"),
    E3("E-3"),
    E4("E-4"),
    E5("E-5"),
    E6("E-6"),
    E7("E-7"),
    E8("E-8"),
    E9("E-9"),
    W1("W-1"),
    W2("W-2"),
    W3("W-3"),
    W4("W-4"),
    W5("W-5"),
    O1("O-1"),
    O2("O-2"),
    O3("O-3"),
    O4("O-4"),
    O5("O-5"),
    O6("O-6"),
    O7("O-7"),
    O8("O-8"),
    O9("O-9"),
    O10("O-10");

    private String militaryGrade;

    MilitaryGrades(String militaryGrade) {
        this.militaryGrade = militaryGrade;
    }

    public String getMilitaryGrade() {
        return this.militaryGrade;
    }
}
