package edu.ucsd.cse110.successorator.date.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Flashcard;
import edu.ucsd.cse110.successorator.lib.domain.FlashcardRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class RoomFlashcardRepository implements FlashcardRepository {
    private final FlashcardDao flashcardDao;

    public RoomFlashcardRepository(FlashcardDao flashcardDao) {
        this.flashcardDao = flashcardDao;
    }

    @Override
    public Subject<Flashcard> find(int id) {
        LiveData<FlashcardEntity> entityLiveData = flashcardDao.findAsLiveData(id);
        LiveData<Flashcard> flashcardLiveData = Transformations.map(entityLiveData, FlashcardEntity::toFlashcard);
        return new LiveDataSubjectAdapter<>(flashcardLiveData);
    }

    @Override
    public Subject<List<Flashcard>> findAll() {
        var entitiesLiveData = flashcardDao.findAllAsLiveData();
        var flashcardsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream().map(FlashcardEntity::toFlashcard).collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(flashcardsLiveData);
    }

    @Override
    public void save(Flashcard flashcard) {
        flashcardDao.insert(FlashcardEntity. fromFlashcard(flashcard));
    }

    @Override
    public void save(List<Flashcard> flashcards) {
        var entities = flashcards.stream()
                .map(FlashcardEntity::fromFlashcard)
                .collect(Collectors.toList());
        flashcardDao.insert(entities);
    }

    @Override
    public void append(Flashcard flashcard) {
        flashcardDao.append(FlashcardEntity.fromFlashcard(flashcard));
    }

    @Override
    public void prepend(Flashcard flashcard) {
        flashcardDao.prepend(FlashcardEntity.fromFlashcard(flashcard));
    }

    @Override
    public void remove(int id) {
        flashcardDao.delete(id);
    }

    public void removeFinished(int id) {
        flashcardDao.delete(id);
    }


}
