import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CareerEngine implements RecommenderInterface {
    private static final String DB_URL = "jdbc:sqlite:careers.db";
    private static final String LOG_FILE = "activity_log.txt";

    @Override
    public List<String> recommend(Applicant applicant) throws Exception {
        // Map to store Career -> Match Score
        List<CareerScore> scores = new ArrayList<>();
        String userSkillsStr = applicant.getSkills().toLowerCase();
        // Create a list of user skills for better matching
        String[] userSkillArray = userSkillsStr.split(",");
        List<String> userSkillList = new ArrayList<>();
        for (String s : userSkillArray)
            userSkillList.add(s.trim());

        logActivity("Processing recommendation for: " + applicant.getName());

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM careers")) {

            while (rs.next()) {
                String title = rs.getString("title");
                String requiredSkills = rs.getString("skills").toLowerCase();
                String[] reqSkillArray = requiredSkills.split(",");

                int matchCount = 0;
                for (String req : reqSkillArray) {
                    // Check if user has this required skill
                    for (String us : userSkillList) {
                        if (us.equals(req.trim())) {
                            matchCount++;
                            break;
                        }
                    }
                }

                String salary = rs.getString("salary");
                String difficulty = rs.getString("difficulty");

                if (matchCount > 0) {
                    scores.add(new CareerScore(title, matchCount, salary, difficulty));
                }
            }

        } catch (SQLException e) {
            logActivity("Database Error: " + e.getMessage());
            throw new Exception("Database connection failed.");
        }

        if (scores.isEmpty()) {
            logActivity("No careers found for " + applicant.getName());
            throw new NoCareerFoundException("No suitable careers found. Try selecting more skills!");
        }

        // Sort by score (Descending)
        scores.sort((a, b) -> b.score - a.score);

        List<String> recommendations = new ArrayList<>();
        for (CareerScore cs : scores) {
            recommendations.add(cs.title);
        }

        logActivity("Found " + recommendations.size() + " careers for " + applicant.getName());
        return recommendations;
    }

    public List<String> getAllSkills() {
        List<String> skills = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT skills FROM careers")) {

            while (rs.next()) {
                String[] sArray = rs.getString("skills").split(",");
                for (String s : sArray) {
                    String trimmed = s.trim().toLowerCase();
                    if (!skills.contains(trimmed) && !trimmed.isEmpty()) {
                        skills.add(trimmed);
                    }
                }
            }
        } catch (SQLException e) {
            logActivity("Error fetching skills: " + e.getMessage());
        }
        java.util.Collections.sort(skills);
        return skills;
    }

    public void addCareer(String title, String skills, String description) throws Exception {
        String sql = "INSERT INTO careers (title, skills, description) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, skills);
            pstmt.setString(3, description);
            pstmt.executeUpdate();

            logActivity("Added new career: " + title);

        } catch (SQLException e) {
            logActivity("Error adding career: " + e.getMessage());
            throw new Exception("Could not add career: " + e.getMessage());
        }
    }

    private void logActivity(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
                PrintWriter pw = new PrintWriter(fw)) {
            pw.println(LocalDateTime.now() + ": " + message);
        } catch (IOException e) {
            System.err.println("Could not write to log file: " + e.getMessage());
        }
    }

    public void saveHistory(Applicant applicant, String bestMatch) {
        String sql = "INSERT INTO history (user_name, age, skills, best_match) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, applicant.getName());
            pstmt.setInt(2, applicant.getAge());
            pstmt.setString(3, applicant.getSkills());
            pstmt.setString(4, bestMatch);
            pstmt.executeUpdate();

            logActivity("Saved history for: " + applicant.getName());

        } catch (SQLException e) {
            logActivity("Error saving history: " + e.getMessage());
        }
    }

    public List<String> getHistory() {
        List<String> history = new ArrayList<>();
        String sql = "SELECT * FROM history ORDER BY timestamp DESC LIMIT 50";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String entry = String.format("[%s] %s (Age: %d) - Match: %s",
                        rs.getString("timestamp"),
                        rs.getString("user_name"),
                        rs.getInt("age"),
                        rs.getString("best_match"));
                history.add(entry);
            }

        } catch (SQLException e) {
            logActivity("Error fetching history: " + e.getMessage());
        }
        return history;
    }

    public void clearHistory() {
        String sql = "DELETE FROM history";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            logActivity("Cleared all history");
        } catch (SQLException e) {
            logActivity("Error clearing history: " + e.getMessage());
        }
    }

    public String[] getCareerDetails(String careerTitle) {
        String[] details = new String[3]; // [salary, difficulty, description]
        details[0] = "Not available";
        details[1] = "Not available";
        details[2] = "";

        String sql = "SELECT salary, difficulty, description FROM careers WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, careerTitle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                details[0] = rs.getString("salary");
                details[1] = rs.getString("difficulty");
                details[2] = rs.getString("description");
            }
        } catch (SQLException e) {
            logActivity("Error fetching career details: " + e.getMessage());
        }

        return details;
    }

    public String generateExplanation(String career, Applicant applicant) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='width: 350px; color: #e0e0e0; font-family: SansSerif; font-size: 12px;'>");
        sb.append(
                "<div style='background-color: rgba(29, 185, 84, 0.1); padding: 10px; border-radius: 8px; border: 1px solid #1DB954;'>");
        sb.append("<b style='color: #1DB954; font-size: 14px;'>Why this is your best match:</b><br><br>");

        sb.append("<p style='line-height: 150%'>");
        sb.append("Based on your profile, <b>").append(career).append("</b> stands out as an exceptional choice. ");
        sb.append("Your unique combination of skills aligns perfectly with the core requirements of this role. ");

        // Fetch salary and difficulty for this career
        String salary = "Competitive";
        String difficulty = "Moderate";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT salary, difficulty FROM careers WHERE title = ?")) {
            pstmt.setString(1, career);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                salary = rs.getString("salary");
                difficulty = rs.getString("difficulty");
            }
        } catch (SQLException e) {
            logActivity("Error fetching details for explanation: " + e.getMessage());
        }

        // Determine difficulty color
        String difficultyColor = "#ffffff"; // Default white
        if (difficulty != null) {
            String diffLower = difficulty.toLowerCase();
            if (diffLower.contains("low") || diffLower.contains("easy")) {
                difficultyColor = "#4CAF50"; // Green
            } else if (diffLower.contains("medium") || diffLower.contains("moderate")) {
                difficultyColor = "#FFC107"; // Yellow/Amber
            } else if (diffLower.contains("very high")) {
                difficultyColor = "#F44336"; // Red
            } else if (diffLower.contains("high")) {
                difficultyColor = "#FF5722"; // Orange/Red
            } else if (diffLower.contains("extreme")) {
                difficultyColor = "#9C27B0"; // Purple
            }
        }

        sb.append("<br><br>");
        sb.append("<b>Salary Potential:</b> <span style='color: #4CAF50; font-weight: bold;'>").append(salary)
                .append("</span><br>");
        sb.append("<b>Difficulty Level:</b> <span style='color: ").append(difficultyColor)
                .append("; font-weight: bold;'>").append(difficulty).append("</span><br><br>");

        // Difficulty context
        if (difficulty != null && (difficulty.equalsIgnoreCase("High") || difficulty.equalsIgnoreCase("Very High")
                || difficulty.equalsIgnoreCase("Extreme"))) {
            sb.append("This role is challenging but highly rewarding. Given your age of <b>").append(applicant.getAge())
                    .append("</b>, ");
            sb.append("it represents a significant growth opportunity that can define your career trajectory. ");
        } else {
            sb.append("This role offers a balanced entry point with steady growth potential. ");
        }

        // Mention a few skills if they match
        String[] userSkills = applicant.getSkills().split(",");
        if (userSkills.length > 0) {
            sb.append("Specifically, your proficiency in <b>").append(userSkills[0].trim()).append("</b>");
            if (userSkills.length > 1) {
                sb.append(" and <b>").append(userSkills[1].trim()).append("</b>");
            }
            sb.append(" gives you a strong competitive advantage. ");
        }

        sb.append(
                "This career path offers exciting opportunities for growth and leverages your strengths in a meaningful way.");
        sb.append("</p></div></body></html>");
        return sb.toString();
    }

    // Helper class for scoring
    private class CareerScore {
        String title;
        int score;
        String salary;
        String difficulty;

        public CareerScore(String title, int score, String salary, String difficulty) {
            this.title = title;
            this.score = score;
            this.salary = salary;
            this.difficulty = difficulty;
        }
    }
}
