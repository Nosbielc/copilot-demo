package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Flux<Room> findAll() {
        return Mono.fromCallable(roomRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Room> findById(Long id) {
        return Mono.fromCallable(() -> roomRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Room> save(Room room) {
        return Mono.fromCallable(() -> roomRepository.save(room))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Room> update(Long id, Room updatedRoom) {
        return Mono.fromCallable(() -> roomRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> {
                    if (opt.isEmpty()) {
                        return Mono.<Room>empty();
                    }
                    Room room = opt.get();
                    room.setName(updatedRoom.getName());
                    room.setNumber(updatedRoom.getNumber());
                    room.setCapacity(updatedRoom.getCapacity());
                    return Mono.fromCallable(() -> roomRepository.save(room))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> roomRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
