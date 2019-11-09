package labs.com.usptodatabasegenerator.uspto.domain.entity.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FILE")
public class UsptoFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "URL")
    private String url;
    @Column(name = "PUBLICATION_DATE")
    private LocalDate publicationDate;
    @Column(name = "DOWNLOAD_DATE")
    private LocalDateTime downloadDate;
    @Column(name = "PROCESS_DATE")
    private LocalDateTime processDate;

    public Boolean hasBeenProcessed(){
        return (this.processDate == null) ? Boolean.FALSE : Boolean.TRUE;
    }

}
