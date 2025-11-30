public class Applicant extends Person {
    private String skills;

    public Applicant(String name, int age, String skills) {
        super(name, age);
        this.skills = skills;
    }

    public String getSkills() {
        return skills;
    }
}
