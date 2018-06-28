# emrfs_customer_credential

A Account: 

s3 bucket : 1st-s3, there are many folders such as: 2016，2017，the bucket is open to B account
 
B Account，first, there is a role：arn:aws-cn:iam::442337510176:role/demo-rolo which has s3 permission. In the same time, the hadoop user should have permission to consume the role
 
When using this class, the Customer.Name.Set property is needed:

for example:

hadoop jar /usr/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar wordcount -DCustomer.Name.Set=2016 s3://1st-s3/2016/test.sh result3





================================================

emrfs-site.xml
 
sudo vim /usr/share/aws/emr/emrfs/conf/emrfs-site.xml
 
<configuration>
<property>
    <name>fs.s3.customAWSCredentialsProvider</name>
    <value>CredentialDemoOppo</value>
</configuration>
 
 
jar path：
 
/usr/share/aws/emr/emrfs/auxlib/

================================================
 
Test:
 
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar wordcount -DCustomer.Name.Set=2016 s3://1st-s3/2016/test.sh result3
 
OK
 
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar wordcount -DCustomer.Name.Set=2017 s3://1st-s3/year=2016/test.sh result3
 
permission deny
