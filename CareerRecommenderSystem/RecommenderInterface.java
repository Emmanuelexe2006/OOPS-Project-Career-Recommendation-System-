import java.util.List;

public interface RecommenderInterface {
    List<String> recommend(Applicant applicant) throws Exception;
}
