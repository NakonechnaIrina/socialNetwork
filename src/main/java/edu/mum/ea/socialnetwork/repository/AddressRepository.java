package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.Address;
 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

 // @Repository
public interface AddressRepository extends MongoRepository<Address, String> {
}
