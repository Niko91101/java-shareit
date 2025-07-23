package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long userId);

    List<ItemRequest> findByRequestorIdNot(Long userId, Pageable pageable);
}
