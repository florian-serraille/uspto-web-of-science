package labs.com.usptodatabasegenerator.uspto.domain.dao.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsptoPatentRepository extends JpaRepository<UsptoPatent, String> {
}
