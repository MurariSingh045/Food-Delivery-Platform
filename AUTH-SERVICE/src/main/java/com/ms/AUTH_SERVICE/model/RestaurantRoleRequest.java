package com.ms.AUTH_SERVICE.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRoleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // assuming one request per user
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String phone;
    private String address;
    private String pan;
    private String aadhar;
    private String licence;
    private String gst;


    @Enumerated(EnumType.STRING)
    private RestaurantRoleStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
