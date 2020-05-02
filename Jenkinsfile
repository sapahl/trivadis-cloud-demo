def frontend = "cloud-demo-frontend";
def processor = "cloud-demo-processor";
def producer = "cloud-demo-producer";

pipeline{
    agent {
        label 'maven'
    }

    stages{
        stage('build') {
            steps{
                sh script: "cd ${frontend} && mvn -DskipTests clean package"
                sh script: "cd ${processor} && mvn -DskipTests clean package"
                sh script: "cd ${producer} && mvn -DskipTests clean package"
            }
        }
        stage('unit tests') {
            steps{
                sh script: "cd ${frontend} && mvn test"
                sh script: "cd ${processor} && mvn test"
                sh script: "cd ${producer} && mvn test"
            }
        }
    }
}