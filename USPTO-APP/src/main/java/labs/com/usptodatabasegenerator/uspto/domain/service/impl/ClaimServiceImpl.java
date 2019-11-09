package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.Claim;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.ClaimService;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Claims;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;
import labs.com.usptodatabasegenerator.uspto.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {

    @Override
    public List<Claim> buildClaimListFromPatentFile(Patent patent) {
        Claims claims = patent.getClaims();

        if (claims == null) {
            return new ArrayList<>();
        }

        return claims.getClaimList()
                .stream()
                .map(claim -> Claim.builder()
                        .claim(StringUtil.removeWhiteSpaceAndReturnCarriage(claim.getClaimText()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void setRelashionship(UsptoPatent usptoPatent) {
        usptoPatent.getClaims()
                .forEach(claim -> claim.setUsptoPatent(usptoPatent));
    }
}
