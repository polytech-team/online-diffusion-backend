package team.polytech.online.diffusion.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "_image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String URL;
    private String prompt;
    private String antiPrompt;
    private Integer seed;
    private String model;
    private Publicity publicity;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @CreationTimestamp
    private Instant createdOn;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAntiPrompt() {
        return antiPrompt;
    }

    public void setAntiPrompt(String antiPrompt) {
        this.antiPrompt = antiPrompt;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Publicity getPublicity() {
        return publicity;
    }

    public void setPublicity(Publicity publicity) {
        this.publicity = publicity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Image publicity status
     */
    public enum Publicity {
        /**
         * Generated, yet not present neither in album nor in posts
         */
        UNUSED,
        /**
         * Added by user to their personal photo album
         */
        PRIVATE,
        /**
         * Available both from personal user album and public feed
         */
        PUBLIC
    }
}
