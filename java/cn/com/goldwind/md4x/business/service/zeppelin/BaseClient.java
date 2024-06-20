package cn.com.goldwind.md4x.business.service.zeppelin;

import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;

public class BaseClient {
    protected String getRegion() {
        return new DefaultAwsRegionProviderChain().getRegion();
    }

    protected String getAccountID() {
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.defaultClient();
        return stsClient.getCallerIdentity(new GetCallerIdentityRequest()).getAccount();
    }
}