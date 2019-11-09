package labs.com.usptodatabasegenerator.uspto.batch.load;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoPatentService;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.UsptoPatentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@JobScope
@Component
public class LoaderWriter implements ItemWriter<UsptoPatent> {

    public static Long currentItem = 0L;
    private final Logger LOG;
    private final UsptoPatentService usptoPatentService;
    private final String cleanDirectory;

    public LoaderWriter(@Value("${directory.clean}") String cleanDirectory,
                        UsptoPatentService usptoPatentService) {
        this.usptoPatentService = usptoPatentService;
        this.cleanDirectory = cleanDirectory;
        this.LOG = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void write(List<? extends UsptoPatent> items) {

        List<UsptoPatent> usptoPatentList = (List<UsptoPatent>) items;

        LOG.info("Saving patents...");
        usptoPatentService.saveAll(usptoPatentList);

        LOG.info("Deleting processed files patent...");
        usptoPatentList.forEach(usptoPatent -> usptoPatentService.deletePatentFile(usptoPatent, cleanDirectory));
        
        currentItem += UsptoPatentServiceImpl.currentLoadItem();
        UsptoPatentServiceImpl.emptyItemCount();
    }
    
    public static Long getCurentItem(){
        return currentItem + UsptoPatentServiceImpl.currentLoadItem();
    }
}