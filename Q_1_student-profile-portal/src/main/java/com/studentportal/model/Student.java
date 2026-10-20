package com.studentportal.model;

/** One row of the students table. Getter names are used by EL in the JSP (e.g. ${s.name}). */
public class Student {

    private int id;
    private String name;
    private String email;
    private String department;
    private String imagePath;
    private String documentPath;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }

    /** EL: ${s.hasImage} */
    public boolean isHasImage() {
        return imagePath != null && !imagePath.trim().isEmpty();
    }

    /** EL: ${s.hasDocument} */
    public boolean isHasDocument() {
        return documentPath != null && !documentPath.trim().isEmpty();
    }

    /** EL: ${s.documentFileName} - last segment of the stored path, for display only. */
    public String getDocumentFileName() {
        if (!isHasDocument()) {
            return "";
        }
        String p = documentPath.trim().replace('\\', '/');
        int slash = p.lastIndexOf('/');
        return slash >= 0 ? p.substring(slash + 1) : p;
    }

    /** EL: ${s.initials} - used for the placeholder avatar. */
    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(parts[0].charAt(0)));
        if (parts.length > 1) {
            sb.append(Character.toUpperCase(parts[parts.length - 1].charAt(0)));
        }
        return sb.toString();
    }
}
