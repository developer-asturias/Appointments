package org.asturias.Domain.Models;

import org.asturias.Infrastructure.Entities.ProgramEntity;


public class Students {


    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private String numberDocument;

    private ProgramEntity program;

    private Long programId;

    public Students(Long id, String name, String lastName, String email, String phone, String numberDocument, ProgramEntity program, Long programId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.numberDocument = numberDocument;
        this.program = program;
        this.programId = programId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }

    public ProgramEntity getProgram() {
        return program;
    }

    public void setProgram(ProgramEntity program) {
        this.program = program;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }
}
