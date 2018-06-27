# emrfs_customer_credential

A账户 s3 bucket : 1st-s3, 其中有很多文件夹比如2016，2017，该bucket向B账户开放
 
B账户，首先有一个role：arn:aws-cn:iam::442337510176:role/demo-role，这个role有所有s3的权限，同时，hadoop运行的用户必须有assume role的权限，
 
用户提交任务时，需要加入一个Customer.Name.Set的属性，这个可以使app的名字，然后设置完以后，终端用户只有访问名字对应想s3 文件夹的权限，没有访问其他文件夹权限。
 
配置文件：
 
sudo vim /usr/share/aws/emr/emrfs/conf/emrfs-site.xml
 
<configuration>
<property>
    <name>fs.s3.customAWSCredentialsProvider</name>
    <value>CredentialDemoOppo</value>
</configuration>
 
 
jar包拷贝目录：
 
/usr/share/aws/emr/emrfs/auxlib/
 
 
 
 测试:
 
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar wordcount -DCustomer.Name.Set=2016 s3://1st-s3/2016/test.sh result3
 
OK
 
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar wordcount -DCustomer.Name.Set=2017 s3://1st-s3/year=2016/test.sh result3
 
permission deny
