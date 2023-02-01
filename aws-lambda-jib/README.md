# AWS Lambda JIB

Lambda supports
- .zip files from local upload
- .zip files from S3
- **ECR images**


# Build AWS Lambda image with JIB 

Prerequisites:
- create ECR repo
- create AWS Lambda function
- make sure Lambda has permissions to access ECR 

Steps:
- see lambda image notes https://docs.aws.amazon.com/lambda/latest/dg/java-image.html
  - it has `"/lambda-entrypoint.sh"` entrypoint, we need to preserve it with JIB, otherwise it'll get replaced with JIB's assumptions
  - and we need to provide handler function as CMD using JIB
- we need to preserve the same structure in `/var/task`
  - classes inside `var/task`
  - libs inside `/var/task/lib`
- JIB copies everything from `src/main/jib` into container, thus put build artifacts accordingly
  - see the result structure below

Notice [JIB](https://github.com/GoogleContainerTools/jib) does NOT require Docker - theres no need to tinker 
docker privileges in CI, thus we can simply run Jenkins file


Run: `./gradlew clean build`


# Run the image locally
```bash
docker pull 123456789.dkr.ecr.eu-central-1.amazonaws.com/my-lambdas/my-basic-lambda:latest

# run and pass handler as CMD param to the entrypoint
docker run 123456789.dkr.ecr.eu-central-1.amazonaws.com/my-lambdas/my-basic-lambda:latest com.avbelyaev.example.Handler::handleRequest
```

Make sure that ENTRYPOINT and CMD still hold: `docker inspect <image>`

Exec into container and check the contents of `/var/task` - it should have both classes and libs mentioned above
```bash
# classes:

$ pwd
/var/task/com/avbelyaev/example
$ ls -l
-rw-r--r-- 1 root root 9794 Jan  1  1970 Handler.class
...

# and libs:

$ pwd
/var/task/lib
$ ls -l
-rw-r--r-- 1 root root    7515 Jan  1  1970 aws-lambda-java-core-1.2.1.jar
-rw-r--r-- 1 root root  110846 Jan  1  1970 aws-lambda-java-events-2.2.8.jar
...
```