pipeline {

    // agent, parameters, environment ...

    stages {

        stage ('Setup') {
            steps {
                script {
                    echo "Logging into ECR"
                    sh (returnStdout: true, script: "eval \$(aws --profile my-profile ecr get-login --region eu-central-1 --no-include-email --registry-ids 123456789)")
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "Building Docker image"
                    sh(returnStdout: true, script: "./gradlew jibDockerBuild --stacktrace")
                }
            }
        }

        stage('Push') {
            steps {
                script {
                    echo "Pushing Docker image"
                    sh(returnStdout: true, script: "docker tag $imageName:latest $dockerRegistry/$ecrRepo:$tag")
                    sh(returnStdout: true, script: "docker push $dockerRegistry/$ecrRepo:$tag")
                }
            }
        }
    }
}
