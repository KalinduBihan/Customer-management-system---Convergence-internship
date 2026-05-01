package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nic", name = "UK_CUSTOMER_NIC")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true, length = 20)
    private String nic;

    // One-to-Many: Customer has multiple mobile numbers
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "customer_mobile_numbers", 
                     joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "mobile_number", length = 20)
    @Builder.Default
    private List<String> mobileNumbers = new ArrayList<>();

    // One-to-Many: Customer has multiple addresses
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    // Self-referencing Many-to-Many: Customer has family members (self-referencing)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "customer_family_members",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "family_member_id")
    )
    @Builder.Default
    private List<Customer> familyMembers = new ArrayList<>();

    /**
     * Helper method to add mobile number
     */
    public void addMobileNumber(String mobileNumber) {
        if (this.mobileNumbers == null) {
            this.mobileNumbers = new ArrayList<>();
        }
        this.mobileNumbers.add(mobileNumber);
    }

    /**
     * Helper method to add address
     */
    public void addAddress(Address address) {
        if (this.addresses == null) {
            this.addresses = new ArrayList<>();
        }
        address.setCustomer(this);
        this.addresses.add(address);
    }

    /**
     * Helper method to add family member
     */
    public void addFamilyMember(Customer familyMember) {
        if (this.familyMembers == null) {
            this.familyMembers = new ArrayList<>();
        }
        this.familyMembers.add(familyMember);
    }
}