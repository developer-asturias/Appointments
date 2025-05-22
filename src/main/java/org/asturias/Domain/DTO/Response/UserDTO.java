package org.asturias.Domain.DTO.Response;

public class UserDTO {

    private String name;

    private String email;

    private String phone;

    private String numberDocument;

    private String nameProgram;

    public UserDTO(String name, String email, String phone, String numberDocument, String nameProgram) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.numberDocument = numberDocument;
        this.nameProgram = nameProgram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNameProgram() {
        return nameProgram;
    }

    public void setNameProgram(String nameProgram) {
        this.nameProgram = nameProgram;
    }
}
