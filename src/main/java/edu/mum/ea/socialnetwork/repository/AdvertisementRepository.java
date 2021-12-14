package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.Advertisement;
 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

 // @Repository
public interface AdvertisementRepository extends MongoRepository<Advertisement, String> {
}
