package ee.helmes.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "agreed_to_terms", nullable = false)
    private boolean agreedToTerms;

    @ManyToMany
    @JoinTable(
            name = "user_profile_sector",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    private Set<Sector> sectors = new HashSet<>();

    protected UserProfile() {
    }

    public UserProfile(String name, Set<Sector> sectors, boolean agreedToTerms) {
        update(name, sectors, agreedToTerms);
    }

    public void update(String name, Set<Sector> sectors, boolean agreedToTerms) {
        this.name = name;
        this.sectors.clear();
        this.sectors.addAll(sectors);
        this.agreedToTerms = agreedToTerms;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public Set<Sector> getSectors() {
        return Collections.unmodifiableSet(sectors);
    }
}