package labs.com.usptodatabasegenerator.uspto.domain.dao.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.PatentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentReferenceRepository extends JpaRepository<PatentReference, Long> {
}
