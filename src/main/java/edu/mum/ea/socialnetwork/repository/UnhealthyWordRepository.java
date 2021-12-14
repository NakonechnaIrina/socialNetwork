package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.UnhealthyWord;
 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

 // @Repository
public interface UnhealthyWordRepository extends MongoRepository<UnhealthyWord, String> {
    void deleteByWordIs(String word);

    boolean existsByWord(String word);
}
