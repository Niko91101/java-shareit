package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

}
