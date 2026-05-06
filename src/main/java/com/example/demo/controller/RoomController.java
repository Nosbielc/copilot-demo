package com.example.demo.controller;

import com.example.demo.entity.Room;
import com.example.demo.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public Flux<Room> listAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Room> findById(@PathVariable Long id) {
        return roomService.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Room not found with id: " + id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Room> create(@Valid @RequestBody Room room) {
        return roomService.save(room);
    }

    @PutMapping("/{id}")
    public Mono<Room> update(@PathVariable Long id, @Valid @RequestBody Room room) {
        return roomService.update(id, room)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Room not found with id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return roomService.delete(id);
    }
}
