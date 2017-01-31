# corehunter3-ui
User Interface for Core Hunter 3: a flexible core subset selection tool

## Build instructions

1. Make sure you have checked out the corehunter-3 code and build it using *mvn clean install*
2. Run *mvn clean p2:site* to create plugin versions of the required CoreHunter dependencies 
3. Finally *mvn clean verify* to build and create the installers.

Using *mvn clean deploy* will deploy the installers to an S3 site, but you need to configure the following properties

*s3.bucket.name
*aws.accessKey
*aws.secretKey
