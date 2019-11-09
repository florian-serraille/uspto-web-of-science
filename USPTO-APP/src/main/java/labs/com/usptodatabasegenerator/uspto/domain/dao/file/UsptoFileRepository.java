package labs.com.usptodatabasegenerator.uspto.domain.dao.file;

import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsptoFileRepository extends JpaRepository<UsptoFile, Integer> {

    @Query("SELECT max(u.publicationDate) from UsptoFile u")
    LocalDate findTopPublicationDate();
    UsptoFile findOneByName(String fileName);
    @Query("SELECT u from UsptoFile u order by u.publicationDate desc ")
    List<UsptoFile> findAllByOrOrderByPublicationDateDesc();
    Boolean deleteByName(String fileName);
}
