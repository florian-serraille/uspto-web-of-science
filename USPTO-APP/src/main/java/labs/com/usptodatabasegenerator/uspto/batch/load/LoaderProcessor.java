package labs.com.usptodatabasegenerator.uspto.batch.load;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.UsptoPatentServiceImpl;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoaderProcessor implements ItemProcessor<Patent, UsptoPatent> {

    private final UsptoPatentServiceImpl usptoPatentService;

    public LoaderProcessor(UsptoPatentServiceImpl usptoPatentService) {
        this.usptoPatentService = usptoPatentService;
    }

    @Override
    public UsptoPatent process(Patent patent) {

        return usptoPatentService.buildFromPatentFile(patent);
    }
}