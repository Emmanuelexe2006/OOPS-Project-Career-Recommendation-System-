import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:sqlite:careers.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS careers");
            String sql = "CREATE TABLE careers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "skills TEXT NOT NULL," +
                    "description TEXT," +
                    "salary TEXT," +
                    "difficulty TEXT)";
            stmt.execute(sql);

            String historySql = "CREATE TABLE IF NOT EXISTS history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_name TEXT," +
                    "age INTEGER," +
                    "skills TEXT," +
                    "best_match TEXT," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(historySql);

            // Reset for the massive update
            stmt.execute("DELETE FROM careers");

            insertData(stmt);

            System.out.println("Database initialized with MASSIVE PROFESSIONAL career list.");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertData(Statement stmt) throws SQLException {
        String[] inserts = {
                // --- TECHNOLOGY ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Software Developer', 'Java,Python,Software Engineering,SQL,Algorithms', 'Builds software.', '$80k - $150k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Data Scientist', 'Python,Mathematics,Statistics,Data Analysis,Machine Learning', 'Analyzes data.', '$100k - $180k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Web Developer', 'HTML/CSS,JavaScript,React,Web Design', 'Makes websites.', '$60k - $120k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Cybersecurity Analyst', 'Network Security,Linux,Ethical Hacking,Cryptography', 'Protects systems.', '$90k - $160k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Game Developer', 'C++,C#,Unity,Unreal Engine,Mathematics', 'Creates video games.', '$70k - $130k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Database Administrator', 'SQL,Database Management,Oracle,Linux', 'Manages databases.', '$80k - $140k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Network Engineer', 'Networking,Cisco,Hardware,WiFi', 'Manages networks.', '$75k - $130k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('AI Engineer', 'Python,Mathematics,Artificial Intelligence,Machine Learning', 'Builds AI models.', '$120k - $200k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('UX/UI Designer', 'User Interface Design,Figma,Creativity,Empathy', 'Designs user interfaces.', '$70k - $130k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('DevOps Engineer', 'Linux,Cloud Computing,AWS,Docker,Scripting', 'Manages deployment.', '$100k - $170k', 'High')",

                // --- HEALTHCARE ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Doctor', 'Medicine,Diagnosis,Surgery,Patient Care,Biology', 'Treats patients.', '$180k - $350k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Nurse', 'Patient Care,Medicine,Empathy,Biology', 'Cares for sick people.', '$60k - $100k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Pharmacist', 'Chemistry,Medicine,Mathematics,Detail Oriented', 'Dispenses drugs.', '$110k - $150k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Dentist', 'Dentistry,Medicine,Patient Care,Detail Oriented', 'Fixes teeth.', '$150k - $250k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Surgeon', 'Medicine,Surgery,Focus,Hand-Eye Coordination', 'Performs surgery.', '$300k - $600k', 'Extreme')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Psychologist', 'Psychology,Active Listening,Analysis,Empathy', 'Mental health expert.', '$70k - $120k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Veterinarian', 'Animal Care,Medicine,Science,Empathy', 'Animal doctor.', '$90k - $140k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Physical Therapist', 'Physical Therapy,Anatomy,Patience,Patient Care', 'Helps recovery.', '$80k - $110k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Paramedic', 'Emergency Medicine,Driving,Calmness,Patient Care', 'Emergency responder.', '$40k - $70k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Nutritionist', 'Nutrition,Health Science,Biology,Planning', 'Diet expert.', '$50k - $80k', 'Medium')",

                // --- ENGINEERING & TRADES ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Civil Engineer', 'Civil Engineering,Mathematics,Physics,Construction', 'Builds bridges/roads.', '$70k - $120k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Mechanical Engineer', 'Mechanical Engineering,Physics,Mathematics,CAD', 'Builds machines.', '$75k - $130k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Electrical Engineer', 'Electrical Engineering,Physics,Electronics,Mathematics', 'Designs electronics.', '$80k - $140k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Aerospace Engineer', 'Aerospace Engineering,Physics,Mathematics,Aircraft', 'Designs aircraft.', '$90k - $160k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Architect', 'Architecture,Design,Mathematics,Creativity', 'Designs buildings.', '$70k - $130k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Electrician', 'Wiring,Tools,Safety,Physics', 'Installs electrical.', '$50k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Plumber', 'Plumbing,Tools,Repair,Water Systems', 'Fixes plumbing.', '$50k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Carpenter', 'Carpentry,Woodworking,Mathematics,Construction', 'Builds with wood.', '$40k - $80k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Mechanic', 'Automotive Repair,Tools,Engines,Diagnostics', 'Fixes cars.', '$45k - $80k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Welder', 'Welding,Metalworking,Tools,Safety', 'Joins metal.', '$40k - $80k', 'Medium')",

                // --- BUSINESS & FINANCE ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('CEO', 'Business Management,Leadership,Vision,Strategy', 'Runs companies.', '$200k - $1M+', 'Extreme')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Project Manager', 'Project Management,Leadership,Organization,Planning', 'Manages projects.', '$80k - $140k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Accountant', 'Accounting,Mathematics,Finance,Excel', 'Tracks money.', '$60k - $100k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Financial Analyst', 'Finance,Mathematics,Data Analysis,Stocks', 'Analyzes markets.', '$70k - $120k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Marketing Manager', 'Marketing,Creativity,Social Media,Communication', 'Sells products.', '$70k - $130k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('HR Specialist', 'Human Resources,Communication,Hiring,Empathy', 'Manages staff.', '$50k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Sales Representative', 'Sales,Communication,Persuasion,Negotiation', 'Sells things.', '$40k - $100k+', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Real Estate Agent', 'Real Estate,Sales,Communication,Negotiation', 'Sells homes.', '$40k - $150k+', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Entrepreneur', 'Risk Taking,Business Management,Creativity,Leadership', 'Starts businesses.', '$0 - $1M+', 'Extreme')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Consultant', 'Data Analysis,Advice,Business Strategy,Problem Solving', 'Advises companies.', '$80k - $180k', 'High')",

                // --- ARTS & MEDIA ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Graphic Designer', 'Graphic Design,Photoshop,Creativity,Visual Arts', 'Visual design.', '$45k - $85k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Writer', 'Creative Writing,Research,Storytelling,Grammar', 'Writes books/articles.', '$40k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Journalist', 'Journalism,Writing,Research,Interviewing', 'Reports news.', '$40k - $80k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Photographer', 'Photography,Lighting,Creativity,Visual Arts', 'Takes photos.', '$35k - $80k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Musician', 'Music Theory,Instrument,Performance,Creativity', 'Plays music.', '$30k - $100k+', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Actor', 'Acting,Performance,Memorization,Public Speaking', 'Performs in movies.', '$30k - $1M+', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Fashion Designer', 'Fashion Design,Sewing,Art,Creativity', 'Designs clothes.', '$50k - $100k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Interior Designer', 'Interior Design,Color Theory,Space Planning,Creativity', 'Decorates rooms.', '$50k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Video Editor', 'Video Editing,Premiere Pro,Creativity,Storytelling', 'Edits videos.', '$50k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Animator', 'Animation,Art,Computer Graphics,Creativity', 'Makes cartoons.', '$60k - $110k', 'High')",

                // --- SCIENCE ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Biologist', 'Biology,Science,Research,Nature', 'Studies life.', '$60k - $100k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Chemist', 'Chemistry,Science,Lab Work,Experiments', 'Studies chemicals.', '$60k - $110k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Physicist', 'Physics,Mathematics,Science,Research', 'Studies matter.', '$80k - $140k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Astronomer', 'Astronomy,Physics,Mathematics,Telescope', 'Studies stars.', '$90k - $150k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Geologist', 'Geology,Earth Science,Outdoors,Research', 'Studies earth.', '$60k - $110k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Meteorologist', 'Meteorology,Science,Weather Prediction,Analysis', 'Predicts weather.', '$60k - $100k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Marine Biologist', 'Marine Biology,Oceanography,Swimming,Science', 'Studies sea life.', '$50k - $90k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Archaeologist', 'History,Excavation,Research,Patience', 'Studies history.', '$50k - $80k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Forensic Scientist', 'Forensics,Science,Lab Work,Detail Oriented', 'Solves crimes.', '$60k - $100k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Botanist', 'Botany,Plants,Nature,Science', 'Studies plants.', '$50k - $90k', 'High')",

                // --- LAW & PUBLIC SERVICE ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Lawyer', 'Law,Reading,Argument,Public Speaking', 'Legal expert.', '$100k - $200k+', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Judge', 'Law,Fairness,Decision Making,Active Listening', 'Decides cases.', '$150k - $250k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Police Officer', 'Law Enforcement,Fitness,Bravery,Safety', 'Enforces law.', '$50k - $90k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Firefighter', 'Fire Safety,Fitness,Bravery,Rescue', 'Fights fires.', '$50k - $80k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Teacher', 'Teaching,Patience,Child Care,Education', 'Teaches school.', '$45k - $80k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Professor', 'Teaching,Research,Higher Education,Public Speaking', 'Teaches college.', '$80k - $150k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Librarian', 'Library Science,Organization,Reading,Research', 'Manages library.', '$50k - $80k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Social Worker', 'Social Work,Empathy,People Skills,Patience', 'Helps families.', '$45k - $75k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Politician', 'Public Speaking,Leadership,Law,Strategy', 'Makes laws.', '$50k - $200k+', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Military Officer', 'Leadership,Fitness,Discipline,Strategy', 'Defends country.', '$60k - $120k', 'High')",

                // --- TRANSPORTATION & OTHERS ---
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Pilot', 'Aviation,Physics,Focus,Navigation', 'Flies planes.', '$100k - $250k', 'Very High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Truck Driver', 'Driving,Patience,Travel,Logistics', 'Drives trucks.', '$50k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Train Conductor', 'Rail Operations,Travel,Scheduling,Safety', 'Operates trains.', '$60k - $90k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Ship Captain', 'Maritime Operations,Navigation,Leadership,Ocean', 'Sails ships.', '$80k - $150k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Flight Attendant', 'Customer Service,Travel,Safety,Communication', 'Helps passengers.', '$40k - $80k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Chef', 'Culinary Arts,Food Safety,Taste,Kitchen Management', 'Cooks food.', '$45k - $90k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Baker', 'Baking,Food Safety,Early Riser,Detail Oriented', 'Bakes bread.', '$35k - $60k', 'Medium')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Farmer', 'Agriculture,Nature,Animals,Hard Work', 'Grows food.', '$40k - $80k', 'High')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Athlete', 'Sports,Fitness,Training,Competition', 'Plays sports.', '$30k - $1M+', 'Extreme')",
                "INSERT INTO careers (title, skills, description, salary, difficulty) VALUES ('Coach', 'Sports,Leadership,Strategy,Teaching', 'Coaches sports.', '$40k - $100k', 'High')"
        };

        for (String sql : inserts) {
            stmt.execute(sql);
        }
    }
}
