@SpringBootApplication
public class CalmConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalmConnectApplication.class, args);
    }
}

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String type; // article, video, etc.
    private String imageUrl; // Optional: URL of the resource's image
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    // Getters and setters
}

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Resource resource;

    private int rating;
    private String comment;

    // Getters and setters
}

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByType(String type);
    List<Resource> findByTitleContaining(String keyword);
    List<Resource> findAllByOrderByCreatedAtDesc();
}

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.resource = :resource")
    Double findAverageRatingByResource(@Param("resource") Resource resource);
}

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping
    public List<Resource> getAllResources() {
        return resourceRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public Resource getResourceById(@PathVariable Long id) {
        Resource resource = resourceRepository.findById(id).orElse(null);
        resource.setAverageRating(ratingRepository.findAverageRatingByResource(resource));
        return resource;
    }

    // Other methods for creating, updating, and deleting resources
}
