package dongaws;

/**
 * credential Demo 
 * Author: Dong
 *
 */
import java.net.URI;
import java.lang.String;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;


final class CredentialDemoOppo implements AWSCredentialsProvider, Configurable {

    private Configuration configuration;


    public CredentialDemoOppo(URI uri, Configuration conf) {
        this.configuration = conf;
    }

    public CredentialDemoOppo() {

    }

    @Override
    public AWSCredentials getCredentials() {

                AWSCredentials credentials;
                String role_arn = "arn:aws-cn:iam::442337510176:role/demo-role";
                AWSSecurityTokenService   stsClient;
                    Credentials stsCredentials=null;

            ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
            try {
                credentialsProvider.getCredentials();
            } catch (Exception e) {
                    throw new AmazonClientException(
                            "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (/Users/dongaws/.aws/credentials), and is in valid format.",
                            e);
            }

            String customerName="";

            String policy="";
            try{
                customerName=configuration.get("Customer.Name.Set");
                if (customerName==""){
                                //if property not set, deny all visit
                    policy= "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Deny\",\"Action\":[\"s3:*\"],\"Resource\":\"*\"}]}";
                } else {
                                policy= "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Action\":[\"s3:*\"],\"Resource\":\"arn:aws-cn:s3:::1st-s3\",\"Condition\":{\"StringLike\":{\"s3:prefix\":[\""+customerName+"/*\"]}}},{\"Effect\":\"Allow\",\"Action\":[\"s3:*\"],\"Resource\":\"arn:aws-cn:s3:::1st-s3/"+customerName+"/*\"}]}";
                }
             } catch (Exception e) {

                 policy= "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Deny\",\"Action\":[\"s3:*\"],\"Resource\":\"*\"}]}";
             }
             stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                        .withCredentials(credentialsProvider)
                        .withRegion("cn-north-1")
                        .build();

             //Assuming the role in the other account to obtain temporary credentials
             AssumeRoleRequest assumeRequest = new AssumeRoleRequest()
                .withRoleArn(role_arn)
                .withDurationSeconds(3600)
                .withRoleSessionName("demo")
                .withPolicy(policy);

             AssumeRoleResult assumeResult = stsClient.assumeRole(assumeRequest);
             stsCredentials = assumeResult.getCredentials();

             credentials =new BasicSessionCredentials(
                            stsCredentials.getAccessKeyId(),
                            stsCredentials.getSecretAccessKey(),
                            stsCredentials.getSessionToken());
             return credentials;
    }

    @Override
    public void refresh() {}

    @Override
    public void setConf(Configuration conf) {
    }

    @Override
    public Configuration getConf() {
        return configuration;
    }

}
