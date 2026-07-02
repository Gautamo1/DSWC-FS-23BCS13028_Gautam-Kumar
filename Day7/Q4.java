import jakarta.persistence.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Entity
class Author {

    @Id
    @GeneratedValue
    Long authorId;

    String username;

    @OneToMany
    @JoinColumn(name = "author_id")
    List<MediaAsset> mediaAssets;
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class MediaAsset {

    @Id
    @GeneratedValue
    Long mediaId;

    String title;
}

@Entity
class VideoAsset extends MediaAsset {

    String resolution;
}

@Entity
class PodcastAsset extends MediaAsset {

    String hostName;
}

interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("""
        SELECT m
        FROM Author a
        JOIN a.mediaAssets m
        WHERE a.authorId = :id
        AND TREAT(m AS VideoAsset).resolution = :resolution
    """)
    List<MediaAsset> getVideos(Long id, String resolution);

    @EntityGraph(attributePaths = "mediaAssets")
    Author findByUsername(String username);
}

public class Q4 {

    public static void main(String[] args) {

        System.out.println("StreamCast System");
    }
}