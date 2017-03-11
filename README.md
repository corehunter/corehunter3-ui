# corehunter3-ui
User Interface for Core Hunter 3: a flexible core subset selection tool. For more information on Core Hunter 3 please refer to the [github repository](https://github.com/corehunter/corehunter3) for the source code or the website [www.corehunter.org](http://www.corehunter.org) for instructions on how to run the software.

## Build instructions

1. Make sure you have checked out the corehunter-3 code and built it using *mvn clean install* to build a local copy or *mvn clean deploy* to build and deploy to an s3 site. For the later you will need to provide the s3 bucket and key as follows in your mvn *settings.xml* file. Natually you will need to add your own values for the bucket name, access key and secret key:)
```
		<s3.bucket.name>your.bucket.name</s3.bucket.name>
    <aws.accessKey>THISISYOURACCESSKEY</aws.accessKey>
    <aws.secretKey>THISISYOURSECRETKEY</aws.secretKey>

```

2. You then should be able to find the zip files in `<your-home-directory>/.m2/repository/org/corehunter/ui/org.corehunter.ui.rcp.product/<VERSION>/` or in the case of a s3 bucket in a directory products/<VERSION>/index.html in your bucket. 



