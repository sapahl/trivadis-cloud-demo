def frontend = "cloud-demo-frontend";
def processor = "cloud-demo-processor";
def producer = "cloud-demo-producer";

pipeline {
    agent {
        label 'maven'
    }

    stages {
        stage('build frontend') {
            steps {
                sh script: "cd ${frontend} && mvn -DskipTests clean package"
            }
        }
        stage('build processor') {
            steps {
                sh script: "cd ${processor} && mvn -DskipTests clean package"
            }
        }
        stage('build producer') {
            steps {
                 sh script: "cd ${producer} && mvn -DskipTests clean package"
            }
        }
        stage('unit tests') {
            steps {
                sh script: "cd ${frontend} && mvn test"
                sh script: "cd ${processor} && mvn test"
                sh script: "cd ${producer} && mvn test"
            }
        }
        stage('build frontend image') {
            steps{
                script{
                    openshift.withCluster(){
                        openshift.withProject(){
                            def build = openshift.selector("bc", "frontend");
                            def startedBuild = build.startBuild("--from-dir=\"./${frontend}\"");
                            startedBuild.logs('-f');
                            echo "${frontend} build status: ${startedBuild.object().status}";
                        }
                    }
                }
            }
        }
        stage('build processor image') {
            steps{
                script{
                    openshift.withCluster(){
                        openshift.withProject(){
                            def build = openshift.selector("bc", "processor");
                            def startedBuild = build.startBuild("--from-dir=\"./${processor}\"");
                            startedBuild.logs('-f');
                            echo "${processor} build status: ${startedBuild.object().status}";
                        }
                    }
                }
            }
        }
        stage('build producer image') {
            steps{
                script{
                    openshift.withCluster(){
                        openshift.withProject(){
                            def build = openshift.selector("bc", "producer");
                            def startedBuild = build.startBuild("--from-dir=\"./${producer}\"");
                            startedBuild.logs('-f');
                            echo "${producer} build status: ${startedBuild.object().status}";
                        }
                    }
                }
            }
        }
    }
}